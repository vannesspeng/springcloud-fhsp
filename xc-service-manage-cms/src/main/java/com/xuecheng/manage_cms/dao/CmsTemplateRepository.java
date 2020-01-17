package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by pyy on 2020/1/16.
 */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate, String> {
}