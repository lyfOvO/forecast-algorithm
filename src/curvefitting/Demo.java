package curvefitting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Demo {
    public static void main(String[] args){
        List<Integer> y= Arrays.asList(156,152,140,129,110,106,89,72,59,41,30,25);
        List<Integer> x=Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
        String startControlDate="2020-8-31";
        int num=20;
        Fitting fit=new Fitting(true,startControlDate,7,1,y,num);
        List<Integer> prediction=fit.fitting();

    }


}
