package com.ggboy.exam.controller;

import com.ggboy.exam.common.ResultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/test")
    public ResultResponse test(@RequestParam("node") String node){
        if ("1".equals(node)){
            return ResultResponse.success();
        }
        return ResultResponse.fail();
    }

}
