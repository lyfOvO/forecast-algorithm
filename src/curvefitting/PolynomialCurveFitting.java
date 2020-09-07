package curvefitting;

import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;

import java.util.ArrayList;
import java.util.List;

public class PolynomialCurveFitting implements CurveFitting{
    private double[] coefficient;//拟合结果，即多项式系数
    private List<Integer> prediction=new ArrayList<>();//预测结果数组，用于输出
    private int numOfResult;//预测结果的天数
    private WeightedObservedPoints points=new WeightedObservedPoints();
    private int numOfPoints =0;//预测结果的开始，即观察点的个数
    private int today;

    public PolynomialCurveFitting(int today){
        this.today=today;
    }

    public PolynomialCurveFitting(int numOfResult,int today){
        this.numOfResult=numOfResult;
        this.today=today;
    }

    public PolynomialCurveFitting(List<Integer> x,List<Integer> y,int numOfResult,int today){
        this.numOfResult=numOfResult;
        this.today=today;
        //将x,y数列添加到观察点序列中
        for(int i=0;i<Math.min(x.size(),y.size());i++){
            points.add((double)x.get(i),(double)y.get(i));
            System.out.println("添加到观察点序列中:("+(double)x.get(i)+","+(double)y.get(i)
                    +"),当前共有观察点"+ numOfPoints +"个");
        }
    }

    @Override
    public void run(){
        PolynomialCurveFitter fitter=PolynomialCurveFitter.create(2);
        coefficient=fitter.fit(points.toList());
        //输出拟合得到的多项式
        System.out.println("拟合所得多项式为:y="+coefficient[0]+"+"+coefficient[1]+"x^2");
    }

    @Override
    public List<Integer> getPrediction(){
        this.run();
        System.out.println("此阶段预测得到的结果个数为:"+numOfResult);
        for(int i=0;i<numOfResult;i++){
            prediction.add(function(today+1));
            System.out.println("预测结果:("+today+","+function(today)+")");
            today++;
        }
        return prediction;
    }

    @Override
    public int function(int x) {
        double y=0;
        for(int i=0;i<=2;i++){
            y+=coefficient[i]*Math.pow(x,i);//系数*(x^i)
        }
        return (int)(y*10+5)/10;
    }

    @Override
    public void addPoint(double x, double y) {
        points.add(x,y);
        System.out.println("添加到观察点序列中:("+x+","+y+")");
    }

    @Override
    public void addPoints(List<Integer> x, List<Integer> y) {
        //将x,y数列添加到观察点序列中
        for(int i=0;i<Math.min(x.size(),y.size());i++){
            points.add((double)x.get(i),(double)y.get(i));
            System.out.println("添加到观察点序列中:("+(double)x.get(i)+","+(double)y.get(i)
                    +"),当前共有观察点"+ numOfPoints+"个");
        }
    }
}
