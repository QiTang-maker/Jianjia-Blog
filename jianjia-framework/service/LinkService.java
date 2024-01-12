package com.dyj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.dto.AddLinkDto;
import com.dyj.domain.entity.Link;
import com.dyj.domain.vo.LinkVo;


/**
 * 友链(Link)表服务接口
 *
 * @author KevinD
 * @since 2023-12-07 09:46:46
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult listAllLink(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addLink(AddLinkDto addLinkDto);

    ResponseResult<LinkVo> getLinkEcho(Long linkId);

    ResponseResult updateLink(LinkVo linkVo);

    ResponseResult deleteLink(Long linkId);
}
