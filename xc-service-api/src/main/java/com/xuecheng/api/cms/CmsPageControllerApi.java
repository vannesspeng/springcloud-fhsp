package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
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

    @ApiOperation(value = "添加页面接口")
    public CmsPageResult add(CmsPage cmsPage);

    @ApiOperation(value = "根据id查询页面")
    public CmsPage findById(String id);

    @ApiOperation(value = "更新页面信息")
    public CmsPageResult edit(String id, CmsPage cmsPage);

    @ApiOperation(value = "删除页面")
    public ResponseResult delete(String id);
}