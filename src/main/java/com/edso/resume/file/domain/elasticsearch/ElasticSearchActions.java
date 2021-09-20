package com.edso.resume.file.domain.elasticsearch;

import com.edso.resume.file.domain.request.UploadCVRequest;
import com.edso.resume.file.service.UploadCVService;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;

public class ElasticSearchActions {
    @Autowired
    UploadCVRequest uploadCVRequest;

    @Autowired
    UploadCVService uploadCVService;

    public void insertTextIntoElasticsearch(UploadCVRequest uploadCVRequest){
        uploadCVService.uploadCV(uploadCVRequest);

    }

}
