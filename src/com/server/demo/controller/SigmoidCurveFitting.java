package com.server.demo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SigmoidCurveFitting implements CurveFitting{
    private List<Integer> x=new ArrayList();
    private List<Integer> y=new ArrayList();
    private List<Integer> prediction=new ArrayList<>();//预测结果数组，用于输出
    private int numOfResult;//预测结果的天数
    private int beginOfPrediction=0;//预测结果的开始，即观察点的个数

    public SigmoidCurveFitting(){

    }

    public SigmoidCurveFitting(int numOfResult){
        this.numOfResult=numOfResult;
    }

    public SigmoidCurveFitting(List<Integer> x,List<Integer> y,int numOfResult){
        this.x=x;
        this.y=y;
        this.numOfResult=numOfResult;
    }

    @Override
    public void run(){
        System.out.println("start python");
        Process proc;
        try {
            proc = Runtime.getRuntime().exec("python ./SigmoidCurveFitting.py");// 执行py文件
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
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
    public List<Integer> getPrediction(int today) {

        return prediction;
    }

    @Override
    public int function(int x) {

        return 0;
    }

    @Override
    public void addPoint(double x, double y) {

    }

    @Override
    public void addPoints(List<Integer> x, List<Integer> y) {

    }
}
