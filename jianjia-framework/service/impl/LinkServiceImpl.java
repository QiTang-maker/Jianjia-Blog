package com.dyj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dyj.constants.SystemConstants;
import com.dyj.domain.ResponseResult;
import com.dyj.domain.dto.AddLinkDto;
import com.dyj.domain.entity.Link;
import com.dyj.domain.vo.LinkVo;
import com.dyj.domain.vo.PageVo;
import com.dyj.mapper.LinkMapper;
import com.dyj.service.LinkService;
import com.dyj.utils.BeanCopyUtils;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author KevinD
 * @since 2023-12-07 09:46:46
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        // 查询所有审核通过的友链
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_APPROVED);
        List<Link> links = list();
        // 转换成VO
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        // 封装返回
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult listAllLink(Integer pageNum, Integer pageSize, String name, String status) {
        // 封装查询条件
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        // 根据友链名称进行模糊查询
        queryWrapper.like(StringUtils.hasText(name), Link::getName, name);
        // 根据状态进行查询
        queryWrapper.eq(StringUtils.hasText(status), Link::getStatus, status);
        // 分页查询
        Page<Link> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        // 封装数据并返回
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(page.getRecords(), LinkVo.class);
        return ResponseResult.okResult(new PageVo(linkVos, page.getTotal()));
    }

    @Override
    public ResponseResult addLink(AddLinkDto addLinkDto) {
        Link link = BeanCopyUtils.copyBean(addLinkDto, Link.class);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<LinkVo> getLinkEcho(Long linkId) {
        Link link = getById(linkId);
        LinkVo linkVo = BeanCopyUtils.copyBean(link, LinkVo.class);
        return ResponseResult.okResult(linkVo);
    }

    @Override
    public ResponseResult updateLink(LinkVo linkVo) {
        Link link = BeanCopyUtils.copyBean(linkVo, Link.class);
        updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteLink(Long linkId) {
        removeById(linkId);
        return ResponseResult.okResult();
    }
}
