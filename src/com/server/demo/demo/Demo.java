package com.server.demo.demo;
import com.server.demo.controller.curvefitting.Fitting;
import com.server.demo.controller.seir.SEIR;

import java.util.Arrays;
import java.util.List;

public class Demo {
    public static void main(String[] args){
        List<Integer> y= Arrays.asList(0,0,0,0,1,2,3);
        SEIR seir=new SEIR(10,0.5f,10, 0.5f,0.5f,0.5f,0.5f,10000,y);
        //seir.run();
        System.out.println(seir.getPrediction());
    }


}
