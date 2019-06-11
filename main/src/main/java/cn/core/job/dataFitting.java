package cn.core.job;

import cn.core.beans.InInfoDO;
import cn.core.beans.MedicineDO;
import cn.core.beans.PurchaseDO;
import cn.core.daos.InInfoDao;
import cn.core.daos.MedicinePageDao;
import cn.core.daos.PurchaseDao;
import cn.core.daos.PurchasePageDao;
import cn.core.req.PageReq;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.Math.pow;

@Slf4j
public class dataFitting implements SimpleJob {

    @Autowired
    private MedicinePageDao medicinePageDao;

    @Autowired
    private PurchaseDao purchaseDao;

    @Autowired
    private InInfoDao inInfoDao;

    @Autowired
    private PurchasePageDao purchasePageDao;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("库存消耗预测开始");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        date = calendar.getTime();

        List<InInfoDO> data;
        InInfoDO obj;
        PurchaseDO purchaseDO;
        long offSet = 0;
        List<Double> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();
        LeastSquare square = new LeastSquare();
        double avg;
        PageReq pageReq = new PageReq();
        pageReq.setPageSize(100);
        List<MedicineDO> list = medicinePageDao.findAll(new PageReq().toPageable()).getContent();
        for (MedicineDO medicineDO : list) {
            data = inInfoDao.findAllByKeyword1(medicineDO.getMedicineNumber(), pageReq.toPageable()).getContent();
            if (data.size() == 0) {
//                purchaseDO = purchaseDao.findByKeyword(medicineDO.getMedicineNumber());
//                if (purchaseDO != null) {
//                    purchaseDO.setUpdateTime(new Date());
//                    purchaseDO.setForecast(medicineDO.getForecast());
//                    purchaseDO.setMedicineName(medicineDO.getMedicineName());
//                    purchaseDO.setMedicineNumber(medicineDO.getMedicineNumber());
//                    purchaseDO.setSupplier(medicineDO.getSupplier());
//                    purchaseDO.setUnit(medicineDO.getStockUnit());
//                    purchaseDao.save(purchaseDO);
//                } else {
//                    purchaseDO = new PurchaseDO();
//                    purchaseDO.setId(medicineDO.getId());
//                    purchaseDO.setUpdateTime(new Date());
//                    purchaseDO.setForecast(medicineDO.getForecast());
//                    purchaseDO.setMedicineName(medicineDO.getMedicineName());
//                    purchaseDO.setMedicineNumber(medicineDO.getMedicineNumber());
//                    purchaseDO.setSupplier(medicineDO.getSupplier());
//                    purchaseDO.setUnit(medicineDO.getStockUnit());
//                    purchaseDao.save(purchaseDO);
//                }
                continue;
            }
            for (int j = 0; j < data.size(); j++) {
                obj = data.get(j);
                if (j == 0) {
                    xList.add(0D);
                    yList.add(obj.getAmount().doubleValue());
                } else {
                    yList.add(obj.getAmount().doubleValue());
                    calendar.setTime(obj.getInDate());
                    long day1 = calendar.getTimeInMillis();
                    calendar.setTime(data.get(j - 1).getInDate());
                    long day2 = calendar.getTimeInMillis();
                    offSet = offSet + (day1 - day2) / (24 * 60 * 60 * 1000);
                    xList.add((double) offSet);
                }
            }
            //avg = (double) (offSet/data.size());
            int rank = 50;
            square.generateFormula(xList, yList, rank);

            double result = 0;
            if (square.factors != null) {
                result = square.calculate(medicineDO.getPeriod() * 30);
            }

            medicineDO.setUpdateTime(new Date());
            calendar.setTime(data.get(data.size() - 1).getInDate());
            // 最后入库日期加上订购周期得到订货提前期
            calendar.add(Calendar.MONTH, medicineDO.getPeriod());
            Date forecast = calendar.getTime();
            // 完全消耗日期
            medicineDO.setUsableTime(forecast);
            medicinePageDao.save(medicineDO);

            purchaseDO = purchaseDao.findByKeyword(medicineDO.getMedicineNumber());
            if (purchaseDO != null) {
                purchaseDO.setUpdateTime(new Date());
                purchaseDO.setForecast(medicineDO.getForecast());
                if (purchaseDO.getForecast() == 1) {
                    purchaseDO.setAmount((result == 0 ? data.get(data.size() - 1).getAmount() : (int) result));
                } else {
                    purchaseDO.setAmount(null);
                }
                calendar.add(Calendar.DAY_OF_YEAR, -15);
                purchaseDO.setPurchaseDate(calendar.getTime());
                purchaseDao.save(purchaseDO);
            } else {
                purchaseDO = new PurchaseDO();
                purchaseDO.setId(medicineDO.getId());
                purchaseDO.setUpdateTime(new Date());
                if (purchaseDO.getForecast() == 1) {
                    purchaseDO.setAmount((result == 0 ? data.get(data.size() - 1).getAmount() : (int) result));
                } else {
                    purchaseDO.setAmount(null);
                }
                purchaseDO.setForecast(medicineDO.getForecast());
                purchaseDO.setMedicineName(medicineDO.getMedicineName());
                purchaseDO.setMedicineNumber(medicineDO.getMedicineNumber());
                purchaseDO.setPurchaseDate(calendar.getTime());
                purchaseDO.setSupplier(medicineDO.getSupplier());
                purchaseDO.setUnit(medicineDO.getStockUnit());
                purchaseDao.save(purchaseDO);
            }
        }
        log.info("定时消耗预测结束");
    }

    public class LeastSquare {
        private double[][] xMatrix;
        private double[] yMatrix;
        private double[] factors;
        private int factorial;

        /*
         * 参数arrayX为采样点的x轴坐标，按照数据顺序排列
         * arrayY为采样点的y轴坐标，按照数据顺序与x一一对应排列factorial为进行拟合的阶数。
         * 用低阶来拟合高阶曲线时可能会不准确，但阶数过高会导致计算缓慢
         */
        void generateFormula(List<Double> xArray, List<Double> yArray, int factorial) {
            if (xArray.size() != yArray.size() || xArray.size() == 0)
                return;
            this.factorial = factorial;
            int len = xArray.size();
            // 声明运算中的x矩阵和y矩阵
            xMatrix = new double[factorial + 1][factorial + 1];
            yMatrix = new double[factorial + 1];
            // 生成y矩阵以及x矩阵中幂<=阶乘factorial的部分
            for (int i = 0; i <= factorial; i++) {
                double sum = 0;
                for (int j = 0; j < len; j++) {
                    double temp = pow(xArray.get(j), i);
                    sum += temp;
                    yMatrix[i] += temp * yArray.get(j);
                }
                for (int j = 0; j <= i; j++) {
                    xMatrix[j][i - j] = sum;
                }
            }
            // 生成x矩阵中幂>阶乘的部分
            for (int i = factorial + 1; i <= factorial * 2; i++) {
                double sum = 0;
                for (Double x : xArray) {
                    sum += pow(x, i);
                }
                for (int j = i - factorial; j < factorial + 1; j++) {
                    xMatrix[i - j][j] = sum;
                }
            }
            // 用调用PolyMatrix并解方程组，得到各阶的系数序列factors
            PolyMatrix polyMatrix = new PolyMatrix();
            factors = polyMatrix.getResult(xMatrix, yMatrix);
        }

        // 根据输入坐标，以及系数序列factors计算指定坐标的结果
        double calculate(double x) {
            double result = 0;
            for (int i = 0; i <= factorial; i++)
                result += factors[i] * pow(x, i);
            return result;
        }
    }

    public static class PolyMatrix {
        private double[][] augmentedMatrix;
        private double[] result;
        private int factorial;

        // 检查输入项长度并生成增广矩阵
        private boolean init(double[][] xMatrix, double[] yMatrix) {
            factorial = yMatrix.length;
            if (xMatrix.length != factorial)
                return false;
            augmentedMatrix = new double[factorial][factorial + 1];
            for (int i = 0; i < factorial; i++) {
                if (xMatrix[i].length != factorial)
                    return false;
                System.arraycopy(xMatrix[i], 0, augmentedMatrix[i], 0, factorial);
                augmentedMatrix[i][factorial] = yMatrix[i];
            }
            result = new double[factorial];
            return true;
        }

        double[] getResult(double[][] matrixA, double[] arrayB) {
            if (!init(matrixA, arrayB))
                return null;
            // 高斯消元-正向
            for (int i = 0; i < factorial; i++) {
                // 如果当前行对角线项为0则与后面的同列项非0的行交换
                if (!switchZero(i))
                    return null;
                // 消元
                for (int j = i + 1; j < factorial; j++) {
                    if (augmentedMatrix[j][i] == 0)
                        continue;
                    double factor = augmentedMatrix[j][i] / augmentedMatrix[i][i];
                    for (int l = i; l < factorial + 1; l++)
                        augmentedMatrix[j][l] = augmentedMatrix[j][l] - augmentedMatrix[i][l] * factor;
                }
            }
            // 高斯消元
            for (int i = factorial - 1; i >= 0; i--) {
                result[i] = augmentedMatrix[i][factorial] / augmentedMatrix[i][i];
                for (int j = i - 1; j > -1; j--)
                    augmentedMatrix[j][factorial] = augmentedMatrix[j][factorial] - result[i] * augmentedMatrix[j][i];
            }
            return result;
        }

        private boolean switchZero(int i) {
            if (augmentedMatrix[i][i] == 0) {
                int j = i + 1;
                // 找到对应位置非0的列
                while (j < factorial && augmentedMatrix[j][i] == 0) {
                    j++;
                }
                // 若对应位置全为0则无解
                if (j == factorial) {
                    return false;
                } else {
                    double[] tmp = augmentedMatrix[i];
                    augmentedMatrix[i] = augmentedMatrix[j];
                    augmentedMatrix[j] = tmp;
                }
            }
            return true;
        }
    }
}
