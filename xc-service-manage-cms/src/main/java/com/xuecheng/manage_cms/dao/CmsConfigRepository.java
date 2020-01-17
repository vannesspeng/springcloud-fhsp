package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by pyy on 2020/1/14.
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfig, String> {

}