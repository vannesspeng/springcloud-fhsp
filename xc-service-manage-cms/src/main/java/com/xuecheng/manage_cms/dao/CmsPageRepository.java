package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by pyy on 2020/1/14.
 */
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {
    public CmsPage findByPageNameAndAndSiteIdAndPageWebPath(String pageName, String siteId, String pageWebPath);
}