package cn.core.job;

import cn.core.beans.InInfoDO;
import cn.core.beans.MedicineDO;
import cn.core.daos.InInfoDao;
import cn.core.daos.MedicineDao;
import cn.core.daos.PurchaseDao;
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

//@Component
@Slf4j
public class dataFitting implements SimpleJob {

    @Autowired
    private MedicineDao medicineDao;

    @Autowired
    private InInfoDao inInfoDao;

    @Autowired
    private PurchaseDao purchaseDao;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("定时库存消耗开始");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        date = calendar.getTime();
        List<MedicineDO> list = medicineDao.findLateHour(date, new PageReq().toPageable()).getContent();
        List<InInfoDO> data;
        InInfoDO obj;
        int offSet = 0;
        List<Double> xList = new ArrayList<>();
        List<Double> yList = new ArrayList<>();
        LeastSquare square = new LeastSquare();
        double avg;
        for (MedicineDO medicineDO : list) {
            data = inInfoDao.findAllByKeyword(medicineDO.getMedicineNumber(), new PageReq().toPageable()).getContent();
            for (int j = 0; j < data.size(); j++) {
                obj = data.get(j);
                if (j == 0) {
                    xList.add(0D);
                    yList.add(obj.getAmount().doubleValue());
                } else {
                    yList.add(obj.getAmount().doubleValue());
                    calendar.setTime(obj.getInDate());
                    int day1 = calendar.get(java.util.Calendar.DAY_OF_YEAR);
                    calendar.setTime(data.get(j - 1).getInDate());
                    int day2 = calendar.get(java.util.Calendar.DAY_OF_YEAR);
                    offSet = offSet + day1 - day2;
                    xList.add((double) offSet);
                }
                avg = (double) (offSet / xList.size());
                int rank = 50;
                square.generateFormula(xList, yList, rank);
                double result = 0;
                for (int i = 0; i <= rank; i++) {
                    result += square.factors[i] * pow(avg, i);
                }
                medicineDO.setUpdateTime(new Date());
                calendar.setTime(data.get(data.size() - 1).getInDate());
                calendar.add(java.util.Calendar.DAY_OF_YEAR, (int) avg);
                Date forecast = calendar.getTime();
                medicineDO.setPurchaseDate(forecast);
                calendar.add(java.util.Calendar.DAY_OF_YEAR, 15);
                medicineDO.setUsableTime(calendar.getTime());
                medicineDao.save(medicineDO);
                purchaseDao.saveByMedicineNumber(medicineDO.getMedicineNumber(), result, forecast, new PageReq().toPageable());
            }
        }
        log.info("定时消耗预测结束");
    }

    public static void main(String[] args) {
        LeastSquare square = new LeastSquare();
        double[] a = {0, 90, 180, 270, 360, 450, 540, 630, 720, 810, 900, 990, 1080, 1170, 90, 180, 270, 360, 450, 540, 630, 720, 810, 900, 990, 1080, 1170, 90, 180, 270, 360, 450, 540, 630, 720, 810, 900, 990, 1080, 1170};
        double[] b = {1232, 333, 1234, 504, 1202, 393, 1204, 599, 1132, 233, 1034, 644, 1332, 433, 333, 1234, 504, 1202, 393, 1204, 599, 1132, 233, 1034, 644, 1332, 433, 333, 1234, 504, 1202, 393, 1204, 599, 1132, 233, 1034, 644, 1332, 433};
        //square.generateFormula(a, b, 50);
        for (double factor : square.factors) {
            System.out.println(factor);
        }
    }

    public static class LeastSquare {
        private double[][] matrixA;
        private double[] arrayB;
        private double[] factors;
        private int order;

        /*
         * 实例化后，计算前，先要输入参数并生成公式 arrayX为采样点的x轴坐标，按照采样顺序排列
         * arrayY为采样点的y轴坐标，按照采样顺序与x一一对应排列 order
         * 为进行拟合的阶数。用低阶来拟合高阶曲线时可能会不准确，但阶数过高会导致计算缓慢
         */
        public boolean generateFormula(List<Double> arrayX, List<Double> arrayY, int order) {
            if (arrayX.size() != arrayY.size())
                return false;
            this.order = order;
            int len = arrayX.size();
            // 拟合运算中的x矩阵和y矩阵
            matrixA = new double[order + 1][order + 1];
            arrayB = new double[order + 1];
            // 生成y矩阵以及x矩阵中幂<=order的部分
            for (int i = 0; i < order + 1; i++) {
                double sumX = 0;
                for (int j = 0; j < len; j++) {
                    double tmp = pow(arrayX.get(j), i);
                    sumX += tmp;
                    arrayB[i] += tmp * arrayY.get(j);
                }
                for (int j = 0; j <= i; j++)
                    matrixA[j][i - j] = sumX;
            }
            // 生成x矩阵中幂>order的部分
            for (int i = order + 1; i <= order * 2; i++) {
                double sumX = 0;
                for (Double arrayX1 : arrayX) sumX += pow(arrayX1, i);
                for (int j = i - order; j < order + 1; j++) {
                    matrixA[i - j][j] = sumX;
                }
            }
            // 实例化PolynomiaSoluter并解方程组，得到各阶的系数序列factors
            PolynomialSolution soluter = new PolynomialSolution();
            factors = soluter.getResult(matrixA, arrayB);
            return factors != null;
        }

        // 根据输入坐标，以及系数序列factors计算指定坐标的结果
        public double calculate(double x) {
            double result = factors[0];
            for (int i = 1; i <= order; i++)
                result += factors[i] * pow(x, i);
            return result;
        }
    }


    public static class PolynomialSolution {
        private double[][] matrix;
        private double[] result;
        private int order;

        // 检查输入项长度并生成增广矩阵
        private boolean init(double[][] matrixA, double[] arrayB) {
            order = arrayB.length;
            if (matrixA.length != order)
                return false;
            matrix = new double[order][order + 1];
            for (int i = 0; i < order; i++) {
                if (matrixA[i].length != order)
                    return false;
                for (int j = 0; j < order; j++) {
                    matrix[i][j] = matrixA[i][j];
                }
                matrix[i][order] = arrayB[i];
            }
            result = new double[order];
            return true;
        }

        public double[] getResult(double[][] matrixA, double[] arrayB) {
            if (!init(matrixA, arrayB))
                return null;
            // 高斯消元-正向
            for (int i = 0; i < order; i++) {
                // 如果当前行对角线项为0则与后面的同列项非0的行交换
                if (!swithIfZero(i))
                    return null;
                // 消元
                for (int j = i + 1; j < order; j++) {
                    if (matrix[j][i] == 0)
                        continue;
                    double factor = matrix[j][i] / matrix[i][i];
                    for (int l = i; l < order + 1; l++)
                        matrix[j][l] = matrix[j][l] - matrix[i][l] * factor;
                }
            }
            // 高斯消元-反向-去掉了冗余计算
            for (int i = order - 1; i >= 0; i--) {
                result[i] = matrix[i][order] / matrix[i][i];
                for (int j = i - 1; j > -1; j--)
                    matrix[j][order] = matrix[j][order] - result[i] * matrix[j][i];
            }
            return result;
        }

        private boolean swithIfZero(int i) {
            if (matrix[i][i] == 0) {
                int j = i + 1;
                // 找到对应位置非0的列
                while (j < order && matrix[j][i] == 0)
                    j++;
                // 若对应位置全为0则无解
                if (j == order)
                    return false;
                else
                    switchRows(i, j);
            }
            return true;
        }

        private void switchRows(int i, int j) {
            double[] tmp = matrix[i];
            matrix[i] = matrix[j];
            matrix[j] = tmp;
        }
    }
}
