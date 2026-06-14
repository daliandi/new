package com.md.basePlatform.controller;

/**
 * DroneController - 无人机控制层
 * 
 * 【功能】：处理HTTP请求，返回视图或JSON数据
 * 【路由】：@RequestMapping("/drones") 作为基础路径
 * 【页面接口】：list/add/edit/detail/save/delete
 * 【REST API】：/api (GET/POST) /api/{id} (GET/PUT/DELETE)
 * 【技术】：@Controller + @GetMapping/@PostMapping等
 * 【响应封装】：REST API 使用统一响应类 R
 */

import com.md.basePlatform.common.R;
import com.md.basePlatform.domain.Drone;
import com.md.basePlatform.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/drones")
public class DroneController {

    @Autowired
    private DroneService droneService;

    // 页面接口
    @GetMapping
    public String list(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Drone> drones = droneService.search(keyword);
        model.addAttribute("drones", drones);
        model.addAttribute("keyword", keyword);
        return "drone/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        Drone drone = droneService.generateDroneProperties();
        model.addAttribute("drone", drone);
        return "drone/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Drone drone = droneService.findById(id);
        model.addAttribute("drone", drone);
        return "drone/edit";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Drone drone = droneService.findById(id);
        model.addAttribute("drone", drone);
        return "drone/detail";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("drone") Drone drone, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("drone", drone);
            return drone.getId() == null || drone.getId() == 0 ? "drone/add" : "drone/edit";
        }
        if (drone.getId() == null || drone.getId() == 0) {
            droneService.save(drone);
        } else {
            droneService.update(drone);
        }
        return "redirect:/drones";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        if (id != null && id > 0) {
            droneService.delete(id);
        }
        return "redirect:/drones/";
    }

    // RESTful API
    @GetMapping("/api")
    @ResponseBody
    public R<List<Drone>> apiList() {
        return R.ok(droneService.findAll());
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public R<Drone> apiFindById(@PathVariable Integer id) {
        return R.ok(droneService.findById(id));
    }

    @PostMapping("/api")
    @ResponseBody
    public R<Void> apiSave(@RequestBody Drone drone) {
        droneService.save(drone);
        return R.ok("保存成功", null);
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public R<Void> apiUpdate(@PathVariable Integer id, @RequestBody Drone drone) {
        drone.setId(id);
        droneService.update(drone);
        return R.ok("更新成功", null);
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public R<Void> apiDelete(@PathVariable Integer id) {
        droneService.delete(id);
        return R.ok("删除成功", null);
    }

    @GetMapping("/api/search")
    @ResponseBody
    public R<List<Drone>> apiSearch(@RequestParam String keyword) {
        return R.ok(droneService.search(keyword));
    }

    @GetMapping("/api/generate")
    @ResponseBody
    public R<Drone> apiGenerate() {
        return R.ok(droneService.generateDroneProperties());
    }
}