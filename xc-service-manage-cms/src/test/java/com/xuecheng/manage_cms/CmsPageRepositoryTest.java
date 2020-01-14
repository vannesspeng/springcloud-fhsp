package com.xuecheng.manage_cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsPageParam;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by pyy on 2020/1/14.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {
    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Test
    public void testFindList(){
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }

    //添加
    @Test
    public void insert(){
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId("01");
        cmsPage.setPageName("测试页面");
        cmsPage.setPageCreateTime(new Date());
        List<CmsPageParam> cmsPageParams = new ArrayList<>();
        CmsPageParam cmsPageParam = new CmsPageParam();
        cmsPageParam.setPageParamValue("value1");
        cmsPageParam.setPageParamName("param1");
        cmsPageParams.add(cmsPageParam);
        cmsPage.setPageParams(cmsPageParams);
        cmsPageRepository.save(cmsPage);
        System.out.println(cmsPage);
    }

    @Test
    public void update(){
        Optional<CmsPage> optionalCmsPage = cmsPageRepository.findById("5e1d2873060c83242c7ffdb1");
        if(optionalCmsPage.isPresent()){
            CmsPage cmsPage = optionalCmsPage.get();
            cmsPage.setPageName("第一次修改测试的测试页面");
            cmsPageRepository.save(cmsPage);
        }
    }

    @Test
    public void delete(){
        cmsPageRepository.deleteById("5e1d2873060c83242c7ffdb1");
    }
}