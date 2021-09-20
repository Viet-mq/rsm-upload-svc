package com.edso.resume.file.domain.repo;

import com.alibaba.fastjson.JSON;
import com.edso.resume.file.config.ElasticClient;
import com.edso.resume.file.domain.entities.CV;
import com.edso.resume.file.service.BaseService;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CvRepo extends BaseService {

    private final ElasticClient elasticClient;

    public CvRepo(ElasticClient elasticClient) {
        this.elasticClient = elasticClient;
    }

    public List<CV> findAll() {
        SearchResponse response = elasticClient.getClient()
                .prepareSearch("resume")
                .setRouting("cv")
                .execute()
                .actionGet();
        SearchHit[] searchHits = response
                .getHits()
                .getHits();
        return Arrays.stream(searchHits)
                .map(hit -> JSON.parseObject(hit.getSourceAsString(), CV.class))
                .collect(Collectors.toList());
    }

    public void save(CV cv) {
        try {
            IndexResponse response = elasticClient.getClient()
                    .prepareIndex("resume", "cv", cv.getId())
                    .setSource(XContentFactory.jsonBuilder()
                            .startObject()
                            .field("pathFile", cv.getPathFile())
                            .field("profileId", cv.getProfileId())
                            .field("name", cv.getName())
                            .field("content", cv.getContent())
                            .field("create_at", System.currentTimeMillis())
                            .field("update_at", System.currentTimeMillis())
                            .endObject()
                    )
                    .get();
            logger.info(response.toString());
        } catch (IOException e) {
            logger.error("Ex: ", e);
        }
    }

}
