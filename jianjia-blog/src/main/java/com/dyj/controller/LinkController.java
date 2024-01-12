package com.dyj.controller;

import com.dyj.annotation.SystemLog;
import com.dyj.domain.ResponseResult;
import com.dyj.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/getAllLink")
    @SystemLog(BusinessName = "查询所有友链")
    public ResponseResult getAllLink() {
        return linkService.getAllLink();
    }
}
