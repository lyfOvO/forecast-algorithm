package com.server.demo.controller.seir;

import java.util.ArrayList;
import java.util.List;

public class SEIR {
    private float[] N=new float[days];//该地区人口总数
    private float[] I=new float[days];//感染者
    private float[] R=new float[days];//康复者
    private float[] D=new float[days];//死亡者
    private float[] E=new float[days];//潜伏者
    private float[] S=new float[days];//易感人数
    private float r1;//感染者每日接触人数
    private float r2;//潜伏者每日接触人数
    private float b1;//感染者传染概率
    private float b2;//潜伏者传染概率
    private float a;//潜伏者患病概率
    private float v;//感染者康复概率
    private float d;//感染者病死率
    private static int days=300;//总天数
    private List<Integer> y;
    private List<Integer> prediction=new ArrayList<>();

    public SEIR(){

    }

    public SEIR(int r1,float b1,int r2,float b2,float a,float v,float d,int N,List<Integer> y){
        this.r1=(float)r1;
        this.b1=b1;
        this.r2=(float)r2;
        this.b2=b2;
        this.a=a;
        this.v=v;
        this.d=d;
        this.N[0]=(float)N;
        this.y=y;
        System.out.println("传入参数:");
        System.out.println("感染者接触人数："+r1);
        System.out.println("感染者传染概率："+b1);
        System.out.println("潜伏者接触人数："+r2);
        System.out.println("潜伏者传染概率："+b2);
        System.out.println("潜伏者患病概率："+a);
        System.out.println("感染者康复概率："+v);
        System.out.println("感染者病死率："+d);
        System.out.println("该地区总人数："+N);
    }

    public void run(){
        //先处理疫情前期全部为0的数据
        System.out.println("先处理疫情前期全部为0的数据");
        int i=0;
        while(y.get(i)==0){
            I[i]=0;
            R[i]=0;
            D[i]=0;
            E[i]=0;
            N[i]=N[0];
            S[i]=N[i]-I[i];
            System.out.println("第"+(i+1)+"天:人口总数N:"+N[i]+",感染者I:"+I[i]+",康复者T:"+R[i]+",死亡者D:"+D[i]
                                +",潜伏者E:"+E[i]+",易感人数S:"+S[i]);
            i++;
        }
        System.out.println("处理数据结束");
        //初始化数据
        I[i]=(float)y.get(i);
        R[i]=0;
        D[i]=0;
        E[i]=0;
        N[i]=N[0];
        S[i]=N[i]-I[i];
        for(int j=i;j<days-1;j++){
            S[j+1]=S[j]-(r1*b1*I[j]*S[j]/N[j])-(r2*b2*E[j]*S[j]/N[j]);
            if(S[j+1]<0)
                S[j+1]=0;
            E[j+1]=E[j]-(a*E[j])+(r1*b1*I[j]*S[j]/N[j])+(r2*b2*E[j]*S[j]/N[j]);
            if(E[j+1]<0)
                E[j+1]=0;
            I[j+1]=I[j]+(a*E[j])-((v+d)*I[j]);
            R[j+1]=R[j]+(v*I[j]);
            D[j+1]=D[j]+(d*I[j]);
            N[j+1]=N[j]-D[j];
            if(N[j+1]<=0)
                continue;
            System.out.println("第"+(j+1)+"天:人口总数N:"+N[j]+",感染者I:"+I[j]+",康复者R:"+R[j]+",死亡者D:"+D[j]
                    +",潜伏者E:"+E[j]+",易感人数S:"+S[j]);
        }
    }

    public List<Integer> getPrediction(){
        System.out.println("开始进行预测");
        this.run();
        System.out.println("开始添加数据");
        for(int i=0;i<I.length;i++){
            prediction.add((int)I[i]);
        }
        //System.out.println(prediction);
        return prediction;
    }

}
