package com.xuecheng.framework.domain.cms.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by pyy on 2020/1/13.
 */
@Data
public class QueryPageRequest {
    //站点id
    @ApiModelProperty("站点id")
    private String siteId;
    //页面id
    @ApiModelProperty("页面id")
    private String pageId;
    //页面名称
    @ApiModelProperty("页面名称")
    private String pageName;
    //别名
    @ApiModelProperty("别名")
    private String pageAliase;
    //模版id
    @ApiModelProperty("模版id")
    private String templateId;
}