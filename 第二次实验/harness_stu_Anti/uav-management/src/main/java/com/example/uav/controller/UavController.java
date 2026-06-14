package com.example.uav.controller;

import com.example.uav.common.PageResult;
import com.example.uav.common.R;
import com.example.uav.domain.dto.UavCreateRequest;
import com.example.uav.domain.dto.UavDTO;
import com.example.uav.domain.dto.UavUpdateRequest;
import com.example.uav.domain.query.UavQuery;
import com.example.uav.service.AiAttributeService;
import com.example.uav.service.UavService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 无人机信息管理控制器。
 * 页面路由：GET 方法返回 Thymeleaf 视图；
 * REST API：POST/PUT/DELETE 返回 JSON（供 Ajax 调用）。
 */
@Controller
@RequiredArgsConstructor
public class UavController {

    private final UavService uavService;
    private final AiAttributeService aiAttributeService;

    // ==================== 页面路由 ====================

    /**
     * 无人机列表页。
     *
     * @param query 查询条件（URL 参数绑定）
     * @param model Thymeleaf 模型
     * @return 列表视图
     */
    @GetMapping("/uav/list")
    public String list(UavQuery query, Model model) {
        PageResult<UavDTO> page = uavService.listUav(query);
        model.addAttribute("page", page);
        model.addAttribute("query", query);
        return "uav/list";
    }

    /**
     * 新增无人机表单页。
     *
     * @param model Thymeleaf 模型
     * @return 新增视图
     */
    @GetMapping("/uav/add")
    public String addPage(Model model) {
        model.addAttribute("uav", new UavCreateRequest());
        return "uav/add";
    }

    /**
     * 编辑无人机表单页。
     *
     * @param id    无人机主键
     * @param model Thymeleaf 模型
     * @return 编辑视图
     */
    @GetMapping("/uav/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        UavDTO uav = uavService.getUavById(id);
        model.addAttribute("uav", uav);
        return "uav/edit";
    }

    // ==================== REST API ====================

    /**
     * 分页查询无人机列表（Ajax）。
     *
     * @param query 查询条件
     * @return 分页数据
     */
    @GetMapping("/api/uav/list")
    @ResponseBody
    public R<PageResult<UavDTO>> apiList(UavQuery query) {
        return R.ok(uavService.listUav(query));
    }

    /**
     * 根据 ID 查询无人机详情（Ajax）。
     *
     * @param id 无人机主键
     * @return 无人机详情
     */
    @GetMapping("/api/uav/{id}")
    @ResponseBody
    public R<UavDTO> getById(@PathVariable Long id) {
        return R.ok(uavService.getUavById(id));
    }

    /**
     * 新增无人机（Ajax 提交）。
     *
     * @param request 新增请求（JSON 格式）
     * @return 操作结果
     */
    @PostMapping("/api/uav")
    @ResponseBody
    public R<Void> create(@Valid @RequestBody UavCreateRequest request) {
        uavService.createUav(request);
        return R.ok("新增成功", null);
    }

    /**
     * 修改无人机（Ajax 提交）。
     *
     * @param id      无人机主键
     * @param request 修改请求（JSON 格式）
     * @return 操作结果
     */
    @PutMapping("/api/uav/{id}")
    @ResponseBody
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody UavUpdateRequest request) {
        request.setId(id);
        uavService.updateUav(request);
        return R.ok("修改成功", null);
    }

    /**
     * 逻辑删除无人机（Ajax）。
     *
     * @param id 无人机主键
     * @return 操作结果
     */
    @DeleteMapping("/api/uav/{id}")
    @ResponseBody
    public R<Void> delete(@PathVariable Long id) {
        uavService.deleteUav(id);
        return R.ok("删除成功", null);
    }

    /**
     * AI 自动生成无人机属性。
     *
     * @param model 型号描述（URL 参数）
     * @return 生成的属性 DTO
     */
    @GetMapping("/api/uav/ai-generate")
    @ResponseBody
    public R<UavDTO> aiGenerate(@RequestParam String model) {
        UavDTO dto = aiAttributeService.generateAttributes(model);
        return R.ok(dto);
    }

    /**
     * 登录页。
     *
     * @return 登录视图
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * 登录提交（Thymeleaf 版本）。
     *
     * @param username 用户名
     * @param password 密码
     * @param model    Thymeleaf 模型
     * @return 重定向到列表页或返回登录页（含错误信息）
     */
    @PostMapping("/login")
    public String login(String username, String password, Model model) {
        try {
            org.apache.shiro.SecurityUtils.getSubject()
                    .login(new org.apache.shiro.authc.UsernamePasswordToken(username, password));
            return "redirect:/uav/list";
        } catch (org.apache.shiro.authc.AuthenticationException e) {
            model.addAttribute("error", "用户名或密码错误");
            return "login";
        }
    }

    /**
     * 登录 API（供 Vue 前端调用）。
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    @PostMapping("/api/login")
    @ResponseBody
    public R<Void> apiLogin(@RequestParam String username, @RequestParam String password) {
        try {
            org.apache.shiro.SecurityUtils.getSubject()
                    .login(new org.apache.shiro.authc.UsernamePasswordToken(username, password));
            return R.ok("登录成功", null);
        } catch (org.apache.shiro.authc.AuthenticationException e) {
            return R.fail(401, "用户名或密码错误");
        }
    }
}
