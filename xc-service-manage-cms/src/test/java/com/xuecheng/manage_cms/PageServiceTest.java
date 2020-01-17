package com.xuecheng.manage_cms;

import com.xuecheng.manage_cms.service.CmsPageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by pyy on 2020/1/17.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class PageServiceTest {
    @Autowired
    private CmsPageService cmsPageService;

    @Test
    public void testGetPageHtml(){
        String pageHtml = cmsPageService.getPageHtml("5a795ac7dd573c04508f3a56");
        System.out.println(pageHtml);
    }
}