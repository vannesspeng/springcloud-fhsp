package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.*;

/**
 * Created by pyy on 2020/1/13.
 */
@Api(value = "CMS页面管理接口", description = "cms页面管理接口，提供增删改查")
public interface CmsPageControllerApi {
    @ApiOperation(value = "分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "path", defaultValue = "int"),
            @ApiImplicitParam(name = "size", value = "每页记录数", required = true, paramType = "path", defaultValue = "int")
    })
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
}