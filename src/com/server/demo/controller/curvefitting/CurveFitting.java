package com.server.demo.controller.curvefitting;

import java.util.List;

public interface CurveFitting {
    public void run() throws Exception;
    public List<Integer> getPrediction(int today);
    public int function(int x);
    public void addPoint(double x,double y);
    public void addPoints(List<Integer> x,List<Integer> y);
}
