package com.dyj.controller;


import com.dyj.annotation.SystemLog;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.dto.AddLinkDto;
import com.dyj.domain.vo.LinkVo;
import com.dyj.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    @SystemLog(BusinessName = "分页查询友链列表")
    public ResponseResult listAllLink(Integer pageNum, Integer pageSize, String name, String status) {
        return linkService.listAllLink(pageNum, pageSize, name, status);
    }

    @PostMapping
    @SystemLog(BusinessName = "新增友链")
    public ResponseResult addLink(@RequestBody AddLinkDto addLinkDto) {
        return linkService.addLink(addLinkDto);
    }

    @GetMapping("/{id}")
    @SystemLog(BusinessName = "回显友链信息")
    public ResponseResult<LinkVo> getLinkEcho(@PathVariable("id") Long linkId) {
        return linkService.getLinkEcho(linkId);
    }

    @PutMapping
    @SystemLog(BusinessName = "修改友链")
    public ResponseResult updateLink(@RequestBody LinkVo linkVo) {
        return linkService.updateLink(linkVo);
    }

    @DeleteMapping("/{id}")
    @SystemLog(BusinessName = "删除友链")
    public ResponseResult deleteLink(@PathVariable("id") Long linkId) {
        return linkService.deleteLink(linkId);
    }

}
