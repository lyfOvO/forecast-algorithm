package com.server.demo.controller.curvefitting;

import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;

import java.util.ArrayList;
import java.util.List;

public class ExponentialCurveFitting implements CurveFitting{
    private double[] coefficient;//拟合结果，即多项式系数
    private List<Integer> prediction=new ArrayList<>();//预测结果数组，用于输出
    private WeightedObservedPoints points=new WeightedObservedPoints();
    private int numOfPoints =0;//即观察点的个数
    private int begin;
    private int end;


    public ExponentialCurveFitting(){

    }

    public ExponentialCurveFitting(int begin,int end){
        this.begin=begin;
        this.end=end;

    }

    public ExponentialCurveFitting(List<Integer> x,List<Integer> y,int begin,int end){
        this.begin=begin;
        this.end=end;
        //将x,y数列添加到观察点序列中
        for(int i=0;i<Math.min(x.size(),y.size());i++){
            double tx=(double)x.get(i);
            double ty=(double)y.get(i);
            points.add(tx,Math.log(ty));
            numOfPoints++;
            System.out.println("添加到观察点序列中:("+tx+","+ty
                    +"),当前共有观察点"+ numOfPoints +"个");
        }
    }

    @Override
    public void run(){
        PolynomialCurveFitter fitter=PolynomialCurveFitter.create(1);
        coefficient=fitter.fit(points.toList());
        coefficient[0]=Math.pow(Math.E,coefficient[0]);//A--->a
        //输出拟合得到的指数函数
        System.out.println("拟合所得指数函数为:y="+coefficient[0]+"*e^"+coefficient[1]+"x");
    }

    @Override
    public List<Integer> getPrediction(){
        this.run();
        for(int i=begin;i<end;i++){
            prediction.add(function(i));
            System.out.println("预测结果:("+i+","+function(i)+")");
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
        points.add(x,Math.log(y));
        numOfPoints++;
        System.out.println("添加到观察点序列中:("+x+","+y+"),当前共有观察点"+ numOfPoints +"个");
    }

    @Override
    public void addPoints(List<Integer> x, List<Integer> y) {
        //将x,y数列添加到观察点序列中
        for(int i=0;i<Math.min(x.size(),y.size());i++){
            points.add((double)x.get(i),Math.log((double)y.get(i)));
            numOfPoints++;
            System.out.println("添加到观察点序列中:("+(double)x.get(i)+","+(double)y.get(i)
                    +"),当前共有观察点"+ numOfPoints +"个");
        }
    }
}
