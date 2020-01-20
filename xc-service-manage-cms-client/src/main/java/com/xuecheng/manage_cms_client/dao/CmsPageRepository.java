package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by pyy on 2020/1/19.
 */
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {
}