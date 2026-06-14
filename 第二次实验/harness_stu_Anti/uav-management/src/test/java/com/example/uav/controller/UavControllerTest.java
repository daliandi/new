package com.example.uav.controller;

import com.example.uav.common.PageResult;
import com.example.uav.domain.dto.UavCreateRequest;
import com.example.uav.domain.dto.UavDTO;
import com.example.uav.exception.BusinessException;
import com.example.uav.service.AiAttributeService;
import com.example.uav.service.UavService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UavController 切片测试（@WebMvcTest），验证请求参数校验和响应格式。
 */
@WebMvcTest(UavController.class)
class UavControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UavService uavService;

    @MockBean
    private AiAttributeService aiAttributeService;

    @MockBean
    private com.example.uav.config.UavUserRealm uavUserRealm;

    // ====== GET /api/uav/list ======

    @Test
    void should_return200WithPage_when_listApiCalled() throws Exception {
        PageResult<UavDTO> page = PageResult.of(
                new com.github.pagehelper.PageInfo<>(java.util.Collections.singletonList(buildDto(1L, "UAV-001"))));
        when(uavService.listUav(any())).thenReturn(page);

        mockMvc.perform(get("/api/uav/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists());
    }

    // ====== GET /api/uav/{id} ======

    @Test
    void should_return200WithUav_when_uavExists() throws Exception {
        when(uavService.getUavById(1L)).thenReturn(buildDto(1L, "UAV-001"));

        mockMvc.perform(get("/api/uav/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.uavCode").value("UAV-001"));
    }

    @Test
    void should_return500_when_uavNotFound() throws Exception {
        when(uavService.getUavById(99L)).thenThrow(new BusinessException(404, "无人机不存在"));

        mockMvc.perform(get("/api/uav/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404));
    }

    // ====== POST /api/uav ======

    @Test
    void should_return200_when_createRequestIsValid() throws Exception {
        UavCreateRequest req = new UavCreateRequest();
        req.setUavCode("UAV-TEST");
        req.setModel("测试型号");
        doNothing().when(uavService).createUav(any());

        mockMvc.perform(post("/api/uav")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void should_return400_when_uavCodeIsBlank() throws Exception {
        UavCreateRequest req = new UavCreateRequest();
        req.setUavCode("");       // 触发 @NotBlank 校验
        req.setModel("型号");

        mockMvc.perform(post("/api/uav")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400));
    }

    // ====== DELETE /api/uav/{id} ======

    @Test
    void should_return200_when_deleteSuccess() throws Exception {
        doNothing().when(uavService).deleteUav(1L);

        mockMvc.perform(delete("/api/uav/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    // ====== GET /api/uav/ai-generate ======

    @Test
    void should_returnGeneratedDto_when_aiGenerateCalled() throws Exception {
        UavDTO dto = buildDto(null, null);
        dto.setModel("工业级");
        when(aiAttributeService.generateAttributes("工业级")).thenReturn(dto);

        mockMvc.perform(get("/api/uav/ai-generate").param("model", "工业级"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.model").value("工业级"));
    }

    private UavDTO buildDto(Long id, String uavCode) {
        return UavDTO.builder()
                .id(id).uavCode(uavCode).model("测试型号").status(1).build();
    }
}
