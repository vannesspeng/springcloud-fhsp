package com.xuecheng.manage_cms.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsConfigModel;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * Created by pyy on 2020/1/14.
 */
@Service
public class CmsPageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        //数据先检查再使用
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 20;
        }
        Pageable pageable = PageRequest.of(page, size);

        //配置查询条件
        CmsPage cmsPage = new CmsPage();
        //站点id
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //模版id
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        //页面别名
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        //调用dao进行条件查询
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);

        QueryResult queryResult = new QueryResult();
        queryResult.setTotal(all.getTotalElements());
        queryResult.setList(all.getContent());

        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    public CmsPageResult add(CmsPage cmsPage) {
        //查询页面是否存在，这里以页面名称，站点id，页面path作为唯一主键来查询页面是否存在
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndAndSiteIdAndPageWebPath(cmsPage.getPageName(),
                cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if(cmsPage1 != null){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        cmsPage.setPageId(null);//添加页面主键由spring data 自动生成
        cmsPageRepository.save(cmsPage);
        //返回结果
        CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS,cmsPage);
        return cmsPageResult;
    }

    public CmsPage getById(String id) {
        Optional<CmsPage> cmsPage = cmsPageRepository.findById(id);
        if(cmsPage.isPresent()){
            return cmsPage.get();
        }
        return null;
    }

    public CmsPageResult update(String id, CmsPage cmsPage){
        CmsPage updatePage = this.getById(id);
        if(updatePage != null){
            //更新提交上来的页面信息
            updatePage.setPageName(cmsPage.getPageName());
            updatePage.setTemplateId(cmsPage.getTemplateId());
            updatePage.setSiteId(cmsPage.getSiteId());
            updatePage.setPageAliase(cmsPage.getPageAliase());
            updatePage.setPageWebPath(cmsPage.getPageWebPath());
            updatePage.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            updatePage.setDataUrl(cmsPage.getDataUrl());
            CmsPage save = cmsPageRepository.save(updatePage);
            if(save != null){
                //说明更新成功
                CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, save);
                return cmsPageResult;
            }
        }
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    public ResponseResult delete(String id){
        CmsPage cmsPage = this.getById(id);
        if(cmsPage != null){
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    //页面静态化方法
    public String getPageHtml(String pageId){
        //1、根据dataurl获取模版页面数据（包含了templateId, dataUrl）
        Map model = this.getModelByPageId(pageId);
        if(model == null){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        //2、根据templateId获取获取页面对应的模版
        String templateContent = this.getTemplateByPageId(pageId);
        if(StringUtils.isEmpty(templateContent)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //3、使用1中获取到的数据进行模版渲染，返回页面html信息
        String html = generateHtml(templateContent, model);
        return html;
    }

    private String generateHtml(String templateContent, Map model) {
        //1、生成配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //2、模版加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template", templateContent);
        //3、配置模版加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        //4、获取模版
        try {
            Template template = configuration.getTemplate("template");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            return html;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTemplateByPageId(String pageId) {
        CmsPage cmsPage = this.getById(pageId);
        if(cmsPage == null){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String templateId = cmsPage.getTemplateId();
        if(StringUtils.isEmpty(templateId)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if(optional.isPresent()){
            CmsTemplate cmsTemplate = optional.get();
            String templateFileId = cmsTemplate.getTemplateFileId();
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, downloadStream);
            try {
                String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Map getModelByPageId(String pageId) {
        CmsPage cmsPage = this.getById(pageId);
        if(cmsPage == null){
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String dataUrl = cmsPage.getDataUrl();
        if(StringUtils.isEmpty(dataUrl)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;
    }

}