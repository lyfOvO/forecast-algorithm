package curvefitting;

import java.util.Arrays;
import java.util.List;

public class Demo {
    public static void main(String[] args){
        List<Integer> y= Arrays.asList(7,24,30,32,48,61,79,89,95,100,114,128,156,182,200,198,201,177,180,169);
        String startControlDate="2020-9-20";
        int num=10;
        Fitting fit=new Fitting(true,startControlDate,7,1,y,num);
        List<Integer> prediction=fit.fitting();

    }


}
