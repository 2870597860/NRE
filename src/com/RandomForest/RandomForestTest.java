package com.RandomForest;
/*package com.RandomForest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

*//**
 * 随机森林  回归问题
 * @author ysh   1208706282
 *
 *//*
public class RandomForest {
    List<Sample> mSamples;
    List<Cart> mCarts;
    double mFeatureRate;
    int mMaxDepth;
    int mMinLeaf;
    Random mRandom;
    *//**
     * 加载数据   回归树
     * @param path
     * @param regex
     * @throws Exception
     *//*
    public  void loadData(String path,String regex) throws Exception{
        mSamples = new ArrayList<Sample>();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = null;
        String splits[] = null;
        Sample sample = null;
        while(null != (line=reader.readLine())){
            splits = line.split(regex);
            sample = new Sample();
            sample.label = Double.valueOf(splits[0]);
            sample.feature = new ArrayList<Double>(splits.length-1);
            for(int i=0;i<splits.length-1;i++){
                sample.feature.add(new Double(splits[i+1]));
            }
            mSamples.add(sample);
        }
        reader.close();
    }
    public void train(int iters){
        mCarts = new ArrayList<Cart>(iters);
        Cart cart = null;
        for(int iter=0;iter<iters;iter++){
            cart = new Cart();
            cart.mFeatureRate = mFeatureRate;
            cart.mMaxDepth = mMaxDepth;
            cart.mMinLeaf = mMinLeaf;
            cart.mRandom = mRandom;
            List<Sample> s = new ArrayList<Sample>(mSamples.size());
            for(int i=0;i<mSamples.size();i++){
                s.add(mSamples.get(cart.mRandom.nextInt(mSamples.size())));
            }
            cart.setData(s);
            cart.train();
            mCarts.add(cart);
            System.out.println("iter: "+iter);
            s = null;
        }
    }
    *//**
     * 回归问题简单平均法  分类问题多数投票法
     * @param sample
     * @return
     *//*
    public double classify(Sample sample){
        double val = 0;
        for(Cart cart:mCarts){
            val += cart.classify(sample);
        }
        return val/mCarts.size();
    }
    *//**
     * @param args
     * @throws Exception 
     *//*
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        RandomForest forest = new RandomForest();
        forest.loadData("F:/2016-contest/20161001/train_data_1.csv", ",");
        forest.mFeatureRate = 0.8;
        forest.mMaxDepth = 3;
        forest.mMinLeaf = 1;
        forest.mRandom = new Random();
        forest.mRandom.setSeed(100);
        forest.train(100);
        
        List<Sample> samples = Cart.loadTestData("F:/2016-contest/20161001/valid_data_1.csv", true, ",");
        double sum = 0;
        for(Sample s:samples){
            double val = forest.classify(s);
            sum += (val-s.label)*(val-s.label);
            System.out.println(val+"  "+s.label);
        }
        System.out.println(sum/samples.size()+"  "+sum);
        System.out.println(System.currentTimeMillis());
    }

}

*/