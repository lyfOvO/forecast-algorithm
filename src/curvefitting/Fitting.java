package curvefitting;

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
        List<Integer> prediction=new ArrayList<>();
        if(!hasControl){
            //不进行控制的自然增长模型
        }
        else{
            int today=y.size();
            System.out.println("today:"+today);
            int start=this.getStartX(startControlDate,today);
            if(today<start){
                //today处于①
                if(start-today>=num){
                    //整个预测时期(start~start+num)都处于①
                }
                else{
                    //预测时期超过①
                    if(num-(start-today)<=last){
                        //预测时期仅停留在①②
                        //①today~start
                        //②start~num-(start-today)
                    }
                    else{
                        //预测时期处于①②③
                        //①today~start
                        //②start~start+last
                        //③start+last~num+today
                    }
                }
            }
            else if(today<start+last){
                //今天处于②
                if(today+num>start+last){
                    //预测时期处于②③阶段
                    //②today~start+last
                    //③start+last~today+num
                }
                else{
                    //预测时期仅处于②today~today+num
                }
            }
            else{
                //今天处于③，预测时期也只会处于③today~today+num
            }
        }

    }

    Fitting(boolean hasControl,List<Integer> y,int numOfResult){
        this.hasControl=hasControl;
        this.y=y;
        this.num=numOfResult;
    }

    Fitting(boolean hasControl,String startControlDate,int raiseLastTime,int controlGrade,List<Integer> y,int numOfResult){
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
        System.out.println("control start date:"+(today+distance));
        return start;
    }


}
