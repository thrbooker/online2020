package com.atguigu.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class testEasyExcel {
    public static void main(String[] args) {
        //实验excel写的操作

        //1.设置写入文件夹地址和excel文件名称
        //String filename = "C:\\Users\\cuish\\Desktop\\guliTest.xlsx";
        //EasyExcel.write(filename,DemoData.class).sheet("学生列表").doWrite(getData());


        //实现excel读操作
        String filename = "C:\\Users\\cuish\\Desktop\\guliTest.xlsx";

        EasyExcel.read(filename,DemoData.class,new ExcelListener()).sheet().doRead();

    }

    private static List<DemoData> getData(){
        List<DemoData> list = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            DemoData data = new DemoData();
            data.setSno(i);
            data.setSname("Tom"+1);
            list.add(data);
        }
        return list;
    }

}
