package com.example.uav.service.impl;

import com.example.uav.common.PageResult;
import com.example.uav.domain.dto.UavCreateRequest;
import com.example.uav.domain.dto.UavDTO;
import com.example.uav.domain.dto.UavUpdateRequest;
import com.example.uav.domain.entity.Uav;
import com.example.uav.domain.query.UavQuery;
import com.example.uav.exception.BusinessException;
import com.example.uav.mapper.UavMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * UavServiceImpl 单元测试。
 */
@ExtendWith(MockitoExtension.class)
class UavServiceImplTest {

    @Mock
    private UavMapper uavMapper;

    @InjectMocks
    private UavServiceImpl uavService;

    // ====== listUav ======

    @Test
    void should_returnEmptyPage_when_noDataExists() {
        UavQuery query = new UavQuery();
        when(uavMapper.selectList(any(UavQuery.class))).thenReturn(java.util.Collections.emptyList());

        PageResult<UavDTO> result = uavService.listUav(query);

        assertThat(result).isNotNull();
        assertThat(result.getRows()).isEmpty();
    }

    @Test
    void should_returnPage_when_dataExists() {
        UavQuery query = new UavQuery();
        Uav uav = buildUav(1L, "UAV-001", "DJI Mini 3");
        when(uavMapper.selectList(any())).thenReturn(java.util.Collections.singletonList(uav));

        PageResult<UavDTO> result = uavService.listUav(query);

        assertThat(result.getRows()).hasSize(1);
        assertThat(result.getRows().get(0).getUavCode()).isEqualTo("UAV-001");
    }

    // ====== getUavById ======

    @Test
    void should_returnDto_when_uavExists() {
        Uav uav = buildUav(1L, "UAV-001", "DJI Mini 3");
        when(uavMapper.selectById(1L)).thenReturn(uav);

        UavDTO dto = uavService.getUavById(1L);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getModel()).isEqualTo("DJI Mini 3");
    }

    @Test
    void should_throwBusinessException_when_uavNotFound() {
        when(uavMapper.selectById(99L)).thenReturn(null);

        assertThatThrownBy(() -> uavService.getUavById(99L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("不存在");
    }

    // ====== createUav ======

    @Test
    void should_insertUav_when_uavCodeIsUnique() {
        UavCreateRequest req = new UavCreateRequest();
        req.setUavCode("UAV-NEW");
        req.setModel("工业级无人机");
        when(uavMapper.selectByUavCode("UAV-NEW")).thenReturn(null);
        when(uavMapper.insert(any(Uav.class))).thenReturn(1);

        uavService.createUav(req);

        verify(uavMapper, times(1)).insert(any(Uav.class));
    }

    @Test
    void should_throwBusinessException_when_uavCodeAlreadyExists() {
        UavCreateRequest req = new UavCreateRequest();
        req.setUavCode("DUPLICATE");
        req.setModel("型号A");
        when(uavMapper.selectByUavCode("DUPLICATE")).thenReturn(buildUav(1L, "DUPLICATE", "型号A"));

        assertThatThrownBy(() -> uavService.createUav(req))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("已存在");

        verify(uavMapper, never()).insert(any());
    }

    // ====== updateUav ======

    @Test
    void should_updateUav_when_uavExists() {
        UavUpdateRequest req = new UavUpdateRequest();
        req.setId(1L);
        req.setModel("更新型号");
        when(uavMapper.selectById(1L)).thenReturn(buildUav(1L, "UAV-001", "旧型号"));
        when(uavMapper.updateById(any(Uav.class))).thenReturn(1);

        uavService.updateUav(req);

        verify(uavMapper, times(1)).updateById(any(Uav.class));
    }

    @Test
    void should_throwBusinessException_when_updateNonExistingUav() {
        UavUpdateRequest req = new UavUpdateRequest();
        req.setId(99L);
        req.setModel("型号");
        when(uavMapper.selectById(99L)).thenReturn(null);

        assertThatThrownBy(() -> uavService.updateUav(req))
                .isInstanceOf(BusinessException.class);
    }

    // ====== deleteUav ======

    @Test
    void should_deleteUav_when_uavExists() {
        when(uavMapper.selectById(1L)).thenReturn(buildUav(1L, "UAV-001", "型号"));
        when(uavMapper.deleteById(1L)).thenReturn(1);

        uavService.deleteUav(1L);

        verify(uavMapper, times(1)).deleteById(1L);
    }

    @Test
    void should_throwBusinessException_when_deleteNonExistingUav() {
        when(uavMapper.selectById(99L)).thenReturn(null);

        assertThatThrownBy(() -> uavService.deleteUav(99L))
                .isInstanceOf(BusinessException.class);
    }

    // ====== 工具方法 ======

    private Uav buildUav(Long id, String uavCode, String model) {
        return Uav.builder()
                .id(id)
                .uavCode(uavCode)
                .model(model)
                .status(1)
                .aiGenerated(0)
                .deleted(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
