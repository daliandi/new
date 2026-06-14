package com.example.uav.service;

import com.example.uav.common.PageResult;
import com.example.uav.domain.dto.UavCreateRequest;
import com.example.uav.domain.dto.UavDTO;
import com.example.uav.domain.dto.UavUpdateRequest;
import com.example.uav.domain.query.UavQuery;

/**
 * 无人机信息管理业务服务接口。
 */
public interface UavService {

    /**
     * 分页查询无人机列表。
     *
     * @param query 查询条件（型号、注册编号、状态、分页参数）
     * @return 分页结果
     */
    PageResult<UavDTO> listUav(UavQuery query);

    /**
     * 根据 ID 查询无人机详情。
     *
     * @param id 无人机主键
     * @return 无人机 DTO，不存在则抛出 BusinessException
     */
    UavDTO getUavById(Long id);

    /**
     * 新增无人机信息。
     *
     * @param request 新增请求（包含注册编号、型号等字段）
     */
    void createUav(UavCreateRequest request);

    /**
     * 修改无人机信息。
     *
     * @param request 修改请求（必须包含 id）
     */
    void updateUav(UavUpdateRequest request);

    /**
     * 逻辑删除无人机信息。
     *
     * @param id 无人机主键
     */
    void deleteUav(Long id);
}
