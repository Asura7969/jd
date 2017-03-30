package com.taotao.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.pojo.BasePojo;

public abstract class BaseService<T extends BasePojo> {

    //public abstract Mapper<T> getMapper();
    @Autowired
    private Mapper<T> mapper;

    /*
     * 根据id查询
     * 
     */
    public T queryById(Long id) {
        return this.mapper.selectByPrimaryKey(id);
    }


    /*
     * 查询所有
     * 
     */
    public List<T> queryAll() {
        return this.mapper.select(null);
    }

    /*
     * 根据条件查询一条数据，
     * 如果有多条数据会抛异常
     * */
    public T queryOne(T record) {
        return this.mapper.selectOne(record);
    }

    /*
     * 根据条件查询数据
     * 
     */
    public List<T> queryListByWhere(T record) {
        return this.mapper.select(record);
    }


    /**
     * 分页查询
     *
     * @param page
     * @param rows
     * @param record
     * @return
     */

    public PageInfo<T> queryPageListByWhere(Integer page, Integer rows, T record) {
        PageHelper.startPage(page, rows);
        List<T> list = this.queryListByWhere(record);
        return new PageInfo<T>(list);
    }

    /**
     * 添加数据
     * 返回成功的条数
     */
    public Integer save(T record) {
        record.setCreated(new Date());
        record.setUpdated(record.getCreated());
        return this.mapper.insert(record);
    }

    /**
     * 新增数据，使用不为null的insert
     */

    public Integer saveSelective(T record) {
        record.setCreated(new Date());
        record.setUpdated(record.getCreated());
        return this.mapper.insertSelective(record);
    }

    /**
     * 修改数据，返回成功条数
     *
     * @param record
     * @return
     */
    public Integer update(T record) {
        record.setUpdated(new Date());
        return this.mapper.updateByPrimaryKey(record);
    }

    /**
     * 修改数据，使用不为null的字段，返回成功条数
     */
    public Integer updateSelective(T record) {
        record.setCreated(null);
        record.setUpdated(new Date());
        return this.mapper.updateByPrimaryKeySelective(record);

    }

    /**
     * 删除数据
     */
    public Integer deleteById(Long id) {

        return this.mapper.deleteByPrimaryKey(id);
    }

    /**
     * 批量删除
     */

    public Integer deleteByIds(Class<T> clazz, String property, List<Object> values) {
        Example example = new Example(clazz);
        example.createCriteria().andIn(property, values);
        return this.mapper.deleteByExample(example);
    }

    /**
     * 根据条件做删除
     */
    public Integer deleteByWhere(T record) {
        return this.mapper.delete(record);
    }


}
