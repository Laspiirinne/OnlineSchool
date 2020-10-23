package com.atguigu.demo.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

// 创建监听类读取excel信息
public class ExcelListener extends AnalysisEventListener<DemoData> {

    // 按行读取excel内容,demoData是每行的内容
    @Override
    public void invoke(DemoData demoData, AnalysisContext analysisContext) {
        System.out.println("****"+demoData);
    }

    // 读表头
    @Override
    public void invokeHeadMap(Map<Integer,String> headMap, AnalysisContext context){
        System.out.println("表头"+headMap);
    }

    // 读取完之后
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {}
}
