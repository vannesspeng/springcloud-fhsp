package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by pyy on 2020/1/14.
 */
@Service
public class CmsPageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;

    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        //数据先检查再使用
        if(queryPageRequest == null){
            queryPageRequest = new QueryPageRequest();
        }
        if(page <= 0){
            page = 1;
        }
        page = page -1;
        if(size <= 0){
            size = 20;
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);

        QueryResult queryResult = new QueryResult();
        queryResult.setTotal(all.getTotalElements());
        queryResult.setList(all.getContent());

        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }
}