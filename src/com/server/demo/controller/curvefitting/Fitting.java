package com.server.demo.controller.curvefitting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Fitting {
    int controlType; //是否进行控制
    String startControlDate; //控制开始的时间
    int last; //控制持续的时间
    int controlGrade; //控制等级
    int num; //所需的预测结果数量
    List<Integer> y; //用于预测的疫情数据

    Fitting(){

    }

    Fitting(int controlType, List<Integer> y, int numOfResult){
        this.controlType = controlType;
        this.y=y;
        this.num=numOfResult;
    }

    public Fitting(int controlType, String startControlDate, int raiseLastTime, int controlGrade, List<Integer> y, int numOfResult){
        this.controlType = controlType;
        this.startControlDate=startControlDate;
        this.controlGrade=controlGrade;
        switch(controlGrade){
            case 1:this.last=7;
            case 2:this.last=14;
            case 3:this.last=21;
            case 0:this.last=raiseLastTime;
        }
        this.y=y;
        this.num=numOfResult;
    }

    public int getStartX(String startControlDate,int today){
        long distance=0;
        int start=0;
        //获取开始进行政策控制的日期与今天日期的天数差
        try {
            Date date=new SimpleDateFormat("yyyy-MM-dd").parse(startControlDate);
            Date todayDate=new Date();
            distance= (long) (date.getTime()-todayDate.getTime())/(24*60*60*1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        start= (int) (today+distance);
        System.out.println("control start date:"+start);
        return start;
    }

    public List<Integer> fitting(){
        List<Integer> prediction=new ArrayList<>();
        int today=y.size();
        System.out.println("today:"+today);
        int start=this.getStartX(startControlDate,today);
        System.out.println("start:"+start);
        //先判断是否全部为0
        List<Integer> end;
        if(y.get(y.size()-1)==0){
            for(int i=0;i<num;i++)
                prediction.add(0);
            System.out.println("确诊人数归零");
            return prediction;
        }
        if(controlType==1){
            //不进行控制的自然增长模型
            System.out.println("without control");
            List<Integer> x=new ArrayList<>();
            for(int i=1;i<=y.size();i++){
                x.add(i);
            }
            SigmoidCurveFitting fit=new SigmoidCurveFitting(x,y,today,num);
            prediction.addAll(fit.getPrediction());
            return prediction;
        }
        else if(controlType==2){
            //进行控制的函数拟合模型
            if(today<start){
                if(today+num<=start){
                    System.out.println("today<start,today+num<=start");
                    System.out.println("---①---");
                    List<Integer> x1=new ArrayList<>();
                    List<Integer> y1=new ArrayList<>();
                    for(int i=1;i<=today;i++){
                        x1.add(i);
                        y1.add(y.get(i-1));
                    }
                    ExponentialCurveFitting fit1=new ExponentialCurveFitting(x1,y1,1,today+num);
                    prediction.addAll(fit1.getPrediction());
                    System.out.println("--end--");
                }
                else if(today+num<=start+last){
                    System.out.println("today<start,today+num<=start+last");
                    System.out.println("---①---");
                    List<Integer> x1=new ArrayList<>();
                    List<Integer> y1=new ArrayList<>();
                    for(int i=1;i<=today;i++){
                        x1.add(i);
                        y1.add(y.get(i-1));
                    }
                    ExponentialCurveFitting fit1=new ExponentialCurveFitting(x1,y1,1,start);
                    prediction.addAll(fit1.getPrediction());
                    System.out.println("---②---");
                    PolynomialCurveFitting fit2=new PolynomialCurveFitting(start+1,today+num);
                    double tx=(double)start;
                    double ty=prediction.get(prediction.size()-1);
                    fit2.addPoint(tx,ty);
                    fit2.addPoint(tx+((double)last/2),ty*1.5);
                    fit2.addPoint(tx+(double)last,ty);
                    prediction.addAll(fit2.getPrediction());
                    System.out.println("--end--");
                }
                else{
                    System.out.println("today<start,today+num>start+last");
                    System.out.println("---①---");
                    List<Integer> x1=new ArrayList<>();
                    List<Integer> y1=new ArrayList<>();
                    for(int i=1;i<=today;i++){
                        x1.add(i);
                        y1.add(y.get(i-1));
                    }
                    System.out.println("---②---");
                    PolynomialCurveFitting fit2=new PolynomialCurveFitting(start+1,start+last);
                    double tx=(double)start;
                    double ty=prediction.get(prediction.size()-1);
                    fit2.addPoint(tx,ty);
                    fit2.addPoint(tx+((double)last/2),ty*1.5);
                    fit2.addPoint(tx+(double)last,ty);
                    prediction.addAll(fit2.getPrediction());
                    System.out.println("---③---");
                    ExponentialCurveFitting fit3=new ExponentialCurveFitting(start+last+1,today+num);
                    tx=(double)(start+last);
                    ty=prediction.get(prediction.size()-1);
                    fit3.addPoint(tx,ty);
                    fit3.addPoint(tx+30,ty/2);
                    fit3.addPoint(tx+60,ty/4);
                    prediction.addAll(fit3.getPrediction());
                    System.out.println("--end--");
                }
            }
            else if(today==start){
                if(num<=last){
                    System.out.println("today=start,today+num<=start+last");
                    System.out.println("---②---");
                    PolynomialCurveFitting fit=new PolynomialCurveFitting(today+1,today+num);
                    double tx=(double)start;
                    double ty=(double)y.get(start-1);
                    fit.addPoint(tx,ty);
                    fit.addPoint(tx+((double)last/2),ty*1.5);
                    fit.addPoint(tx+(double)last,ty);
                    prediction.addAll(fit.getPrediction());
                    System.out.println("--end--");
                }
                else{
                    System.out.println("today=start,today+num>start+last");
                    System.out.println("---②---");
                    PolynomialCurveFitting fit1=new PolynomialCurveFitting(today+1,start+last);
                    double tx=(double)start;
                    double ty=(double)y.get(start-1);
                    fit1.addPoint(tx,ty);
                    fit1.addPoint(tx+((double)last/2),ty*1.5);
                    fit1.addPoint(tx+(double)last,ty);
                    prediction.addAll(fit1.getPrediction());
                    System.out.println("---③---");
                    ExponentialCurveFitting fit2=new ExponentialCurveFitting(start+last+1,today+num);
                    tx=(double)start+last;
                    ty=(double)prediction.get(prediction.size()-1);
                    fit2.addPoint(tx,ty);
                    fit2.addPoint(tx+30,ty/2);
                    fit2.addPoint(tx+60,ty/4);
                    prediction.addAll(fit2.getPrediction());
                    System.out.println("--end--");
                }
            }
            else if(today<start+last){
                if(today+num<=start+last){
                    System.out.println("start<today<start+last,today+num<=start+last");
                    System.out.println("---②---");
                    PolynomialCurveFitting fit=new PolynomialCurveFitting(today+1,today+num);
                    List<Integer> x=new ArrayList<>();
                    List<Integer> y=new ArrayList<>();
                    for(int i=start;i<=today;i++){
                        x.add(i);
                        y.add(this.y.get(i-1));
                    }
                    fit.addPoints(x,y);
                    if(x.size()<3){
                        double tx=(double)start;
                        double ty=(double)this.y.get(start-1);
                        fit.addPoint(tx+((double)last/2),ty*1.5);
                        fit.addPoint(tx+(double)last,ty);
                    }
                    prediction.addAll(fit.getPrediction());
                    System.out.println("--end--");
                }
                else{
                    System.out.println("start<today<start+last,today+num>start+last");
                    System.out.println("---②---");
                    PolynomialCurveFitting fit1=new PolynomialCurveFitting(today+1,start+last);
                    List<Integer> x=new ArrayList<>();
                    List<Integer> y=new ArrayList<>();
                    double tx=(double)start;
                    double ty=(double)this.y.get(start-1);
                    for(int i=start;i<=today;i++){
                        x.add(i);
                        y.add(this.y.get(i-1));
                    }
                    fit1.addPoints(x,y);
                    if(x.size()<3){
                        fit1.addPoint(tx+((double)last/2),ty*1.5);
                        fit1.addPoint(tx+(double)last,ty);
                    }
                    prediction.addAll(fit1.getPrediction());
                    System.out.println("---③---");
                    tx=(double)(start+last);
                    ty=(double)(prediction.get(prediction.size()-1));
                    ExponentialCurveFitting fit2=new ExponentialCurveFitting(start+last+1,today+num);
                    fit2.addPoint(tx,ty);
                    fit2.addPoint(tx+30,ty/2);
                    fit2.addPoint(tx+60,ty/4);
                    prediction.addAll(fit2.getPrediction());
                    System.out.println("--end--");
                }

            }
            else if(today==start+last){
                System.out.println("today=start+last");
                System.out.println("---③---");
                ExponentialCurveFitting fit=new ExponentialCurveFitting(today+1,today+num);
                double tx=(double)today;
                double ty=(double)y.get(today-1);
                fit.addPoint(tx,ty);
                fit.addPoint(tx+30,ty/2);
                fit.addPoint(tx+60,ty/4);
                prediction.addAll(fit.getPrediction());
                System.out.println("--end--");
            }
            else{
                System.out.println("today>start+last");
                System.out.println("---③---");
                ExponentialCurveFitting fit=new ExponentialCurveFitting(today+1,today+num);
                double tx=(double)today;
                double ty=(double)y.get(today-1);
                List<Integer> x=new ArrayList<>();
                List<Integer> y=new ArrayList<>();
                for(int i=start+last;i<=today;i++){
                    x.add(i);
                    y.add(this.y.get(i-1));
                }
                fit.addPoints(x,y);
                if(x.size()<3) {
                    fit.addPoint(tx, ty);
                    fit.addPoint(tx + 30, ty / 2);
                    fit.addPoint(tx + 60, ty / 4);
                }
                prediction.addAll(fit.getPrediction());
                System.out.println("--end--");
            }
        }
        else if(controlType==3){
            //用户输入变量控制的SEIR模型

        }
        return prediction;
    }

}
