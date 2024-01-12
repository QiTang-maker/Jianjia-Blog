package com.dyj.domain.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "分页查询标签dto")
public class TagListDto {

    @ApiModelProperty(name = "标签名")
    private String name;
    @ApiModelProperty(name = "标签备注")
    private String remark;
}
