package com.atguigu.demo.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.demo.eduservice.entity.EduTeacher;
import com.atguigu.demo.eduservice.entity.vo.TeacherQuery;
import com.atguigu.demo.eduservice.service.EduTeacherService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author Li Jiale
 * @since 2020-09-02
 */

@Api(description = "讲师管理")
@RestController // 把controller交给spring进行管理，并返回jason数据
@RequestMapping("/eduservice/edu-teacher")
//@CrossOrigin // 跨域
public class EduTeacherController {

    // 访问地址:http://localhost:8001/eduservice/edu-teacher/findAll
    // 把service(service的实现类继承了mapper)注入到controller,mapper要注入到service中
    @Autowired
    private EduTeacherService eduTeacherService;

    // 1 查询讲师表中的所有数据
    /*
        rest风格:查 get
                增 post
                改 put
                删 delete
     */

    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll") // 起个名？
    public R findAllTeacher(){
        //调用service中的方法实现查询所有的操作
        List<EduTeacher> list= eduTeacherService.list(null);
        return R.ok().data("items",list);
    }

    // 2 逻辑删除讲师的方法
    @ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("{id}") // id值需要通过路径进行传递
    public R removeTeacher(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id){ // 注解是为了得到路径中的id值
        boolean flag=eduTeacherService.removeById(id);
        if(flag)
            return R.ok();
        else
            return R.error();
    }

    // 3 分页查询讲师方法
    @ApiOperation(value = "分页查询讲师方法")
    @GetMapping("PageTeacher/{current}/{limit}") // id值需要通过路径进行传递
    public R PageListTeacher(@PathVariable long current,
                             @PathVariable long limit){ // 注解是为了得到路径中的id值
        // 创建page对象,current:当前页,size:每页的记录数
        Page<EduTeacher> pageTeacher=new Page<>(current,limit);

        try{
            int i=10/0;
        }
        catch (Exception e){
            // 执行自定义异常
            throw new GuliException(20001,"执行了自定义异常处理");
        }


        // 调用方法实现分页
        // 调用方法的时候，底层封装，把分页的所有数据封装到pageTeacher对象中
        eduTeacherService.page(pageTeacher,null);

        long total=pageTeacher.getTotal(); // 总记录数
        List<EduTeacher> records=pageTeacher.getRecords(); // 数据list

        /**        法一：
         *         Map map=new HashMap();
         *         map.put("total",total);
         *         map.put("rows",records);
         *         return R.ok().data(map);
         */

         return R.ok().data("total",total).data("rows",records);
    }
    
    // 4 条件查询带分页的方法
    @ApiOperation(value = "条件查询带分页方法")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery){
        /**
         * @RequestBody使用json传递数据，把json数据封装到teacherQuery中
         * 但是需要使用post方式提交
         * required = false,表示这个可以没有
         */

        // 创建page对象
        Page<EduTeacher> pageTeacher=new Page<>(current,limit);

        // 构建条件
        QueryWrapper<EduTeacher> wrapper=new QueryWrapper<>();

        // 多条件组合查询
        // 法一：mybatis动态sql
        // 此处使用法二：判断条件值是否为空，如果不为空拼接条件
        String name=teacherQuery.getName();
        Integer level=teacherQuery.getLevel();
        String begin=teacherQuery.getBegin();
        String end=teacherQuery.getEnd();
        if(!StringUtils.isEmpty(name)){
            // 构建条件:模糊查询
            wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)){
            wrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin)){
            // 要写表中的字段名称
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)){
            wrapper.le("gmt_create",end);
        }

        // 排序：按创建日期降序
        wrapper.orderByDesc("gmt_create");

        // 调用方法实现条件查询分页;底层封装，把分页的所有数据封装到pageTeacher对象中
        eduTeacherService.page(pageTeacher,wrapper);
        long total=pageTeacher.getTotal();
        List<EduTeacher> records=pageTeacher.getRecords(); // 数据list
        return R.ok().data("total",total).data("rows",records);
    }

    // 5 添加讲师接口的方法
    @ApiOperation(value = "添加讲师方法")
    @PostMapping("addTeacher")
    // @RequestBody使用json传递数据，把json数据封装到eduTeacher中
    public R addTeacher(@RequestBody(required = true) EduTeacher eduTeacher){
        boolean save=eduTeacherService.save(eduTeacher);
        if(save)
            return R.ok();
        return R.error();
    }

    // 6 根据讲师id进行查询,为了修改的时候做数据回显
    @ApiOperation(value = "根据ID查询讲师")
    @GetMapping("getTeacher/{id}") // id通过路径传递
    public R getTeacher(@ApiParam(name = "id", value = "讲师ID", required = true)
                        @PathVariable String id){ // 注解是为了得到路径中的id值
        EduTeacher eduTeacher=eduTeacherService.getById(id);
        return R.ok().data("teacher",eduTeacher);
    }

    // 7 讲师修改
    @ApiOperation(value = "根据ID修改讲师")
    @PostMapping("updateTeacher")
    // @RequestBody使用json传递数据，把json数据封装到eduTeacher中
    public R updateTeacher(@RequestBody(required = true) EduTeacher eduTeacher){
        boolean update=eduTeacherService.updateById(eduTeacher);
        if(update)
            return R.ok();
        return R.error();
    }
}

