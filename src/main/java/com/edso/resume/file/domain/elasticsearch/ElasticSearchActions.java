package com.edso.resume.file.domain.elasticsearch;

import com.edso.resume.file.domain.request.UploadCVRequest;
import com.edso.resume.file.service.UploadCVService;
import com.edso.resume.lib.response.BaseResponse;
import lombok.SneakyThrows;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class ElasticSearchActions {
    @Autowired
    UploadCVRequest uploadCVRequest;

    @Autowired
    Client client;

    @SneakyThrows
    public void insertTextIntoElasticsearch(String content){
        IndexResponse indexResponse = client.prepareIndex("cv", "_doc", uploadCVRequest.getProfileId())
                .setSource(jsonBuilder()
                        .startObject()
                        .field("content", content)
                        .endObject()

                ).get();
        System.out.println("response id: " + indexResponse.getId());
    }

}
