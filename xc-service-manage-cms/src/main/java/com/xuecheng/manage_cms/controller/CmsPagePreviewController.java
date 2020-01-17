package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import javax.servlet.ServletOutputStream;
import java.io.IOException;

/**
 * Created by pyy on 2020/1/17.
 */
@Controller
public class CmsPagePreviewController extends BaseController {

    @Autowired
    private CmsPageService cmsPageService;

    @GetMapping("/cms/preview/{pageId}")
    public void preview(@PathVariable("pageId") String pageId){
        String pageHtml = cmsPageService.getPageHtml(pageId);
        if(StringUtils.isNotEmpty(pageHtml)){
            try{
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(pageHtml.getBytes("utf-8"));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}