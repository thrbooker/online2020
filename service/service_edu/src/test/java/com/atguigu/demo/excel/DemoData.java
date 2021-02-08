package com.atguigu.demo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

@Data
public class DemoData {
    //设置excel表头名称
    @ExcelProperty(value = "学生名称",index = 0)
    private Integer sno;

    @ExcelProperty(value = "学生姓名",index = 1)
    private String sname;

}
