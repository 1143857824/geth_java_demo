package com.wanliu.admin.entity;

import java.util.List;

/**
 * 分页助手,封装分页数据
 * @author 梁涛
 * @param <T>
 */
public class ResoultPage<T> {
    private Integer totalPage;
    private Integer total;
    private Integer currentPage;
    private List<T> students;

    public ResoultPage() {
    }

    public ResoultPage(Integer totalPage, List<T> students, Integer currentPage) {
        this.totalPage = totalPage;
        this.currentPage = currentPage;
        this.students = students;
    }



    public ResoultPage(Integer totalPage, Integer total, List<T> students) {
        this.totalPage = totalPage;
        this.total = total;
        this.students = students;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getStudents() {
        return students;
    }

    public void setStudents(List<T> students) {
        this.students = students;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
}
