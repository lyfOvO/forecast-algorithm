package com.server.demo.demo;
import com.server.demo.controller.curvefitting.Fitting;
import com.server.demo.controller.curvefitting.SigmoidCurveFitting;
import com.server.demo.controller.seir.SEIR;

import java.util.Arrays;
import java.util.List;

public class Demo {
    public static void main(String[] args){
        List<Integer> y=Arrays.asList(43,1861,37904,93494,164653,209692,235369,256251,272064);
        List<Integer> x=Arrays.asList(1,2,3,4,5,6,7,8,13, 14,15,16,17);
        //SEIR seir=new SEIR(10,0.2f,10, 0.1f,0.1f,0.2f,0.1f,10000,y);
        //seir.run();
        //System.out.println(seir.getPrediction());
        SigmoidCurveFitting test=new SigmoidCurveFitting(x,y,19,30);
        System.out.println(test.getPrediction());
    }


}
