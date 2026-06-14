package com.example.uav.common;

import com.github.pagehelper.PageInfo;
import lombok.Getter;

import java.util.List;

/**
 * 分页结果封装。
 *
 * @param <T> 列表数据类型
 */
@Getter
public class PageResult<T> {

    /** 总记录数 */
    private final long total;

    /** 当前页数据列表 */
    private final List<T> rows;

    /** 总页数 */
    private final int pages;

    /** 当前页码 */
    private final int pageNum;

    /** 每页条数 */
    private final int pageSize;

    private PageResult(long total, List<T> rows, int pages, int pageNum, int pageSize) {
        this.total = total;
        this.rows = rows;
        this.pages = pages;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    /**
     * 从 PageHelper 的 PageInfo 构建分页结果。
     *
     * @param pageInfo PageHelper 分页信息
     * @return 分页结果
     */
    public static <T> PageResult<T> of(PageInfo<T> pageInfo) {
        return new PageResult<>(
                pageInfo.getTotal(),
                pageInfo.getList(),
                pageInfo.getPages(),
                pageInfo.getPageNum(),
                pageInfo.getPageSize()
        );
    }
}
