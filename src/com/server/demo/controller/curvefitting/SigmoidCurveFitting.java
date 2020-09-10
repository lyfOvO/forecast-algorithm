package com.server.demo.controller.curvefitting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SigmoidCurveFitting implements CurveFitting{
    private List<Integer> x=new ArrayList();
    private List<Integer> y=new ArrayList();
    private List<Integer> prediction=new ArrayList<>();//预测结果数组，用于输出
    private double[] coefficient=new double[3];
    private int num;//预测结果的个数
    private int today;

    public SigmoidCurveFitting(){

    }

    public SigmoidCurveFitting(int today,int num){
        this.today=today;
        this.num=num;
    }

    public SigmoidCurveFitting(List<Integer> x,List<Integer> y,int today,int num){
        this.x=x;
        this.y=y;
        this.today=today;
        this.num=num;
    }

    @Override
    public void run(){
        System.out.println("y:"+y);
        System.out.println("start python");
        Process proc;
        try {
            String filePython="./src/com/server/demo/controller/curvefitting/SigmoidCurveFitting.py ";
            proc = Runtime.getRuntime().exec("python "+filePython+y);// 执行py文件并传递数组y
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                //将读入结果分割为拟合的三个系数
                String result[]=line.split(" ");
                for(int i=0;i<3;i++)
                    coefficient[i]=Double.parseDouble(result[i]);
                System.out.println("a:"+coefficient[0]);
                System.out.println("b:"+coefficient[1]);
                System.out.println("c:"+coefficient[2]);
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Integer> getPrediction() {
        run();
        for(int i=1;i<today+num;i++){
            prediction.add(function(i));
            System.out.println("预测结果:("+i+","+function(i)+")");
        }
        return prediction;
    }

    @Override
    public int function(int x) {
        double y=0;
        //y=c/(1+np.exp(a*x+b))
        double a=coefficient[0];
        double b=coefficient[1];
        double c=coefficient[2];
        y=c/(1+Math.pow(Math.E,a*(double)x+b));
        //System.out.println(y);
        return (int)y;
    }

    @Override
    public void addPoint(double x, double y) {

    }

    @Override
    public void addPoints(List<Integer> x, List<Integer> y) {

    }
}
