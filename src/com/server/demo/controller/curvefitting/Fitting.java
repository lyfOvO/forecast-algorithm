package com.server.demo.controller.curvefitting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Fitting {
    boolean hasControl; //是否进行控制
    String startControlDate; //控制开始的时间
    int last; //控制持续的时间
    int controlGrade; //控制等级
    int num; //所需的预测结果数量
    List<Integer> y; //用于预测的疫情数据

    Fitting(){

    }

    Fitting(boolean hasControl,List<Integer> y,int numOfResult){
        this.hasControl=hasControl;
        this.y=y;
        this.num=numOfResult;
    }

    public Fitting(boolean hasControl, String startControlDate, int raiseLastTime, int controlGrade, List<Integer> y, int numOfResult){
        this.hasControl=hasControl;
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
                prediction.add(i);
            System.out.println("确诊人数归零");
            return prediction;
        }
        if(!hasControl){
            //不进行控制的自然增长模型
            System.out.println("without control");
            List<Integer> x=new ArrayList<>();
            for(int i=1;i<=y.size();i++){
                x.add(i);
            }
            SigmoidCurveFitting fit=new SigmoidCurveFitting(x,y,num);
            prediction.addAll(fit.getPrediction(today));
            return prediction;
        }
        else{
            if(today<start){
                //today处于①
                if(start-today>=num){
                    //整个预测时期(start~start+num)都处于①
                    System.out.println("整个预测时期(start~start+num)都处于①");
                    System.out.println("①:"+start+"~"+(start+num));
                    List<Integer> x=new ArrayList<>();
                    for(int i=1;i<=y.size();i++)
                        x.add(i);
                    ExponentialCurveFitting fit=new ExponentialCurveFitting(x,y,num);
                    prediction=fit.getPrediction(today);
                }
                else{
                    //预测时期超过①
                    if(num-(start-today)<=last){
                        //预测时期仅停留在①②
                        System.out.println("预测时期仅停留在①②");
                        //①today~start
                        System.out.println("①:"+today+"~"+start);
                        List<Integer> x1=new ArrayList<>();
                        List<Integer> y1=new ArrayList<>();
                        for(int i=0;i<today;i++){
                            x1.add(i+1);
                            y1.add(y.get(i));
                        }
                        PolynomialCurveFitting fit1=new PolynomialCurveFitting(x1,y1,start-today);
                        if(x1.size()<3){
                            double tx=x1.get(0);
                            double ty=y1.get(0);
                            fit1.addPoint(tx+(double)(last/2),ty*(1+(((double)last)/10)));
                            fit1.addPoint(tx+(double)last,ty);
                        }
                        prediction.addAll(fit1.getPrediction(today));
                        //②start~num+today
                        System.out.println("③:"+(start+1)+"~"+(today+num));
                        double tx=start;
                        double ty=prediction.get(prediction.size()-1);
                        PolynomialCurveFitting fit2=new PolynomialCurveFitting(num-(start-today));
                        fit2.addPoint(tx,ty);
                        fit2.addPoint(tx+(double)(last/2),ty*(1+(((double)last)/10)));
                        fit2.addPoint(tx+(double)last,ty);
                        prediction.addAll(fit2.getPrediction(start));
                    }
                    else{
                        //预测时期处于①②③
                        System.out.println("预测时期处于①②③");
                        //①today~start
                        System.out.println("①:"+today+"~"+start);
                        List<Integer> x1=new ArrayList<>();
                        List<Integer> y1=new ArrayList<>();
                        for(int i=0;i<today;i++){
                            x1.add(i+1);
                            y1.add(y.get(i));
                        }
                        ExponentialCurveFitting fit1=new ExponentialCurveFitting(x1,y1,start-today);
                        prediction.addAll(fit1.getPrediction(today));
                        System.out.println("predictiona:"+prediction);
                        //②start~start+last
                        System.out.println("②:"+start+"~"+(start+last));
                        double tx=start;
                        double ty=prediction.get(prediction.size()-1);
                        PolynomialCurveFitting fit2=new PolynomialCurveFitting(last);
                        fit2.addPoint(tx,ty);
                        fit2.addPoint(tx+(double)(last/2),ty*(1+(((double)last)/10)));
                        fit2.addPoint(tx+(double)last,ty);
                        prediction.addAll(fit2.getPrediction(start));
                        System.out.println("predictiona:"+prediction);
                        //③start+last~num+today
                        System.out.println("③:"+(start+last)+"~"+(today+num));
                        System.out.println("predictiona:"+prediction);
                        tx=(double)start+last;
                        ty=prediction.get(prediction.size()-1);
                        ExponentialCurveFitting fit3=new ExponentialCurveFitting(num+today-start-last);
                        fit3.addPoint(tx,ty);
                        fit3.addPoint(tx+1*15*((double)last)/30,ty/2);
                        fit3.addPoint(tx+2*15*((double)last)/30,ty/4);
                        prediction.addAll(fit3.getPrediction(start+last));
                    }
                }
            }
            else if(today<start+last){
                //今天处于②
                if(today+num>start+last){
                    //预测时期处于②③阶段
                    System.out.println("预测时期处于②③阶段");
                    //②today~start+last
                    System.out.println("②:"+today+"~"+(start+last));
                    List<Integer> x1=new ArrayList<>();
                    List<Integer> y1=new ArrayList<>();
                    for(int i=start;i<today;i++){
                        x1.add(i+1);
                        y1.add(y.get(i));
                    }
                    PolynomialCurveFitting fit1=new PolynomialCurveFitting(x1,y1,(last+start-today));
                    if(x1.size()<3){
                        double tx=x1.get(0);
                        double ty=y1.get(0);
                        fit1.addPoint(tx+(double)(last/2),ty*(1+(((double)last)/10)));
                        fit1.addPoint(tx+(double)last,ty);
                    }
                    prediction.addAll(fit1.getPrediction(today));
                    //③start+last~today+num
                    System.out.println("③:"+(start+last)+"~"+(today+num));
                    double tx=(double)start+last;
                    double ty=prediction.get(prediction.size()-1);
                    ExponentialCurveFitting fit2=new ExponentialCurveFitting(today+num-start-last);
                    fit2.addPoint(tx,ty);
                    fit2.addPoint(tx+1*15*((double)last)/30,ty/2);
                    fit2.addPoint(tx+2*15*((double)last)/30,ty/4);
                    prediction.addAll(fit2.getPrediction(start+last));

                }
                else{
                    //预测时期仅处于②today~today+num
                    System.out.println("②:"+today+"~"+(today+num));
                    List<Integer> x1 =new ArrayList<>();
                    List<Integer> y1=new ArrayList<>();
                    for(int i=start;i<today;i++){
                        x1.add(i+1);
                        y1.add(y.get(i));
                    }
                    PolynomialCurveFitting fit1=new PolynomialCurveFitting(x1,y1,num);
                    if(x1.size()<3){
                        double tx=x1.get(0);
                        double ty=y1.get(0);
                        fit1.addPoint(tx+(double)(last/2),ty*(1+(((double)last)/10)));
                        fit1.addPoint(tx+(double)last,ty);
                    }
                    prediction.addAll(fit1.getPrediction(today));
                }
            }
            else{
                //今天处于③，预测时期也只会处于③today~today+num
                System.out.println("③:"+(today-1)+"~"+(today+num));
                List<Integer> x1= new ArrayList<>();
                List<Integer> y1=new ArrayList<>();
                for(int i=start+last;i<=today;i++){
                    x1.add(i);
                    y1.add(y.get(i-1));
                }
                ExponentialCurveFitting fit=new ExponentialCurveFitting(x1,y1,num);
                if(x1.size()<3){
                    //如果可用数据太少，则需要手动添加数据
                    double tx=x1.get(0);
                    double ty=y1.get(0);
                    fit.addPoint(tx+1*15*((double)last)/30,ty/2);
                    fit.addPoint(tx+2*15*((double)last)/30,ty/4);
                }
                prediction.addAll(fit.getPrediction(today));
            }
        }
        return prediction;
    }

}
