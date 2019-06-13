package cn.core.job;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MyLineRegression {
    /**
     * 最小二乘法
     *
     * @param X
     * @param Y
     * @return y = ax + b, r
     */
    public Map<String, Double> lineRegression(double[] X, double[] Y) {
        if (null == X || null == Y || 0 == X.length
                || 0 == Y.length || X.length != Y.length) {
            throw new RuntimeException();
        }

        // x平方差和
        double Sxx = varianceSum(X);
        // y平方差和
        double Syy = varianceSum(Y);
        // xy协方差和
        double Sxy = covarianceSum(X, Y);

        double xAvg = arraySum(X) / X.length;
        double yAvg = arraySum(Y) / Y.length;

        double a = Sxy / Sxx;
        double b = yAvg - a * xAvg;

        // 相关系数
        double r = Sxy / Math.sqrt(Sxx * Syy);
        Map<String, Double> result = new HashMap<>();
        result.put("a", a);
        result.put("b", b);
        result.put("r", r);

        return result;
    }

    /**
     * 计算方差和
     *
     * @param X
     * @return
     */
    private double varianceSum(double[] X) {
        double xAvg = arraySum(X) / X.length;
        return arraySqSum(arrayMinus(X, xAvg));
    }

    /**
     * 计算协方差和
     *
     * @param X
     * @param Y
     * @return
     */
    private double covarianceSum(double[] X, double[] Y) {
        double xAvg = arraySum(X) / X.length;
        double yAvg = arraySum(Y) / Y.length;
        return arrayMulSum(arrayMinus(X, xAvg), arrayMinus(Y, yAvg));
    }

    /**
     * 数组减常数
     *
     * @param X
     * @param x
     * @return
     */
    private double[] arrayMinus(double[] X, double x) {
        int n = X.length;
        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            result[i] = X[i] - x;
        }

        return result;
    }

    /**
     * 数组求和
     *
     * @param X
     * @return
     */
    private double arraySum(double[] X) {
        double s = 0;
        for (double x : X) {
            s = s + x;
        }
        return s;
    }

    /**
     * 数组平方求和
     *
     * @param X
     * @return
     */
    private double arraySqSum(double[] X) {
        double s = 0;
        for (double x : X) {
            s = s + Math.pow(x, 2);
            ;
        }
        return s;
    }

    /**
     * 数组对应元素相乘求和
     *
     * @param X
     * @return
     */
    private double arrayMulSum(double[] X, double[] Y) {
        double s = 0;
        for (int i = 0; i < X.length; i++) {
            s = s + X[i] * Y[i];
        }
        return s;
    }

    public static void main(String[] args) {
        Random random = new Random();
//        double[] X = new double[20];
//        double[] Y = new double[20];
//
//        for(int i = 0; i < 20; i++)
//        {
//            X[i] = Double.valueOf(Math.floor(random.nextDouble() * 97));
//            Y[i] = Double.valueOf(Math.floor(random.nextDouble() * 997));
//        }

        double[] xList = new double[]{0.0, 123.0, 215.0, 304.0, 396.0, 488.0, 579.0, 668.0, 760.0, 852.0, 944.0, 1033.0, 1123.0};
        double[] yList = new double[]{4468.0, 2865.0, 2240.0, 2641.0, 1839.0, 1468.0, 6034.0, 1582.0, 5914.0, 7869.0, 3552.0, 5134.0, 3212.0};
        Map<String, Double> map = new MyLineRegression().lineRegression(xList, yList);
        double a = 0.0;
        for (String s : map.keySet()) {
            System.out.println(s + "\t:" + map.get(s));
        }
        System.out.println(map.get("a")*1200 + map.get("b"));
    }
//    public static void main(String[] args) {
//        LeastSquare square = new LeastSquare();
//        List<Double> xList = asList(0.0, 123.0,215.0,304.0, 396.0,488.0,579.0,668.0,760.0,852.0,944.0,1033.0,1123.0);
//        List<Double> yList = asList(4468.0, 2865.0,2240.0,2641.0,1839.0,1468.0,6034.0,1582.0,5914.0,7869.0,3552.0,5134.0,3212.0);
//        square.generateFormula(xList, yList, xList.size()+1);
//        int result = (int) square.calculate(150);
//        System.out.println(result);
//    }
}

