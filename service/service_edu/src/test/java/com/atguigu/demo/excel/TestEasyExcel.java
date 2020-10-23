package com.atguigu.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {
    public static void main(String[] args) {
        // 实现excel写的操作
        // 1 设置写入文件夹地址和excel文件名称
        //String filename="C:\\Users\\sins0\\Desktop\\mf.xlsx";

        // 2 调用EasyExcel里面的方法来实现写操作
        // write两个参数：文件路径及名称，参数实体类class
        //EasyExcel.write(filename,DemoData.class).sheet("学生列表").doWrite(getData());

        ////////////////////////

        // 实现excel读的操作
        String filename="C:\\Users\\sins0\\Desktop\\mf.xlsx";

        EasyExcel.read(filename,DemoData.class,new ExcelListener()).sheet().doRead();
    }

    // 创建方法返回List集合
    private static List<DemoData> getData(){
        List<DemoData> list=new ArrayList<>();
        for(int i=0;i<10;++i){
            DemoData demoData=new DemoData();
            demoData.setSno(i);
            demoData.setSname("Lucy"+i);
            list.add(demoData);
        }
        return list;
    }
}
