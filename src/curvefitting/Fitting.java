package curvefitting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        int distance=0;
        int start=0;
        //获取开始进行政策控制的日期与今天日期的天数差
        try {
            Date date=new SimpleDateFormat("yyyy-MM-dd").parse(startControlDate);
            Date todayDate=new Date();
            System.out.println("today:"+todayDate);
            System.out.println("start:"+date);
            distance= (int) (date.getTime()-todayDate.getTime())/(24*60*60*1000);
            System.out.println("controlStartDate - today="+distance);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return start;
    }


}
