package curvefitting;

import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;

import java.util.ArrayList;
import java.util.List;

public class ExponentialCurveFitting implements CurveFitting{
    private double[] coefficient;//拟合结果，即多项式系数
    private List<Integer> prediction=new ArrayList<>();//预测结果数组，用于输出
    private int numOfResult;//预测结果的天数
    private WeightedObservedPoints points;
    private int beginOfPrediction=0;//预测结果的开始，即观察点的个数

    public ExponentialCurveFitting(){

    }

    public ExponentialCurveFitting(int numOfResult){
        this.numOfResult=numOfResult;
    }

    public ExponentialCurveFitting(List<Integer> x,List<Integer> y,int numOfResult){
        this.numOfResult=numOfResult;
        //将x,y数列添加到观察点序列中
        for(int i=0;i<Math.min(x.size(),y.size());i++){
            System.out.println("添加到观察点序列中:("+(double)x.get(i)+","+(double)y.get(i)
                    +"),当前共有观察点"+beginOfPrediction+"个");
            points.add((double)x.get(i),Math.log((double)y.get(i)));
        }
    }

    @Override
    public void run(){
        PolynomialCurveFitter fitter=PolynomialCurveFitter.create(2);
        coefficient=fitter.fit(points.toList());
        coefficient[0]=Math.pow(Math.E,coefficient[0]);
        //输出拟合得到的指数函数
        System.out.println("拟合所得指数函数为:y="+coefficient[0]+"*e^"+coefficient[1]+"x");
    }

    @Override
    public List<Integer> getPrediction(){
        this.run();
        int date=beginOfPrediction;
        System.out.println("此阶段预测得到的结果个数为:"+numOfResult);
        for(int i=0;i<numOfResult;i++){
            prediction.add(function(date));
            System.out.println("预测结果:("+date+","+function(date)+")");
            date++;
        }
        return prediction;
    }

    @Override
    public int function(int x) {
        double y=0;
        double a=coefficient[0];
        double b=coefficient[1];
        y=a*Math.pow(Math.E,b*x);
        return (int)(y*10+5)/10;
    }

    @Override
    public void addPoint(double x, double y) {
        System.out.println("添加到观察点序列中:("+x+","+y+")");
        points.add(x,Math.log(y));
    }

    @Override
    public void addPoints(List<Integer> x, List<Integer> y) {
        //将x,y数列添加到观察点序列中
        for(int i=0;i<Math.min(x.size(),y.size());i++){
            System.out.println("添加到观察点序列中:("+(double)x.get(i)+","+(double)y.get(i)
                +"),当前共有观察点"+beginOfPrediction+"个");
            points.add((double)x.get(i),Math.log((double)y.get(i)));
        }
    }
}
