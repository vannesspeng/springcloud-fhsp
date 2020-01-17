package com.xuecheng.manage_cms.service;


import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by pyy on 2020/1/14.
 */
@Service
public class CmsConfigService {
    @Autowired
    private CmsConfigRepository cmsConfigRepository;

    public CmsConfig getConfigById(String id){
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

}