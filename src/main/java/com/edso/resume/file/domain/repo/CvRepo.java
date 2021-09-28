package com.edso.resume.file.domain.repo;

import com.alibaba.fastjson.JSON;
import com.edso.resume.file.config.ElasticClient;
import com.edso.resume.file.domain.entities.CV;
import com.edso.resume.file.domain.request.DeleteCVRequest;
import com.edso.resume.file.domain.request.UpdateCVRequest;
import com.edso.resume.file.service.BaseService;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

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

    public Map<String, Object> findById(UUID id) {
        GetResponse response = elasticClient.getClient()
                .prepareGet("resume", "cv", String.valueOf(id))
                .get();
        return response.getSource();
    }

    public List<CV> multiMatchQuery(String key) throws IOException {
        MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(key,
                "id",
                "name",
                "profileID",
                "pathFile",
                "content");
        SearchResponse response = elasticClient.getClient()
                .prepareSearch("resume")
                .setRouting("cv")
                .setQuery(multiMatchQuery)
                .execute()
                .actionGet();
        SearchHit[] searchHits = response
                .getHits()
                .getHits();
        return Arrays.stream(searchHits)
                .map(hit -> JSON.parseObject(hit.getSourceAsString(), CV.class))
                .collect(Collectors.toList());
    }

    public String delete(DeleteCVRequest deleteCVRequest) {
        DeleteResponse deleteResponse = elasticClient.getClient()
                .prepareDelete("resume", "cv", deleteCVRequest.getId())
                .get();
        return deleteResponse.getResult().toString();
    }

    public String update(UpdateCVRequest updateCVRequest) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("resume")
                .type("cv")
                .id(String.valueOf(updateCVRequest.getId()))
                .doc(jsonBuilder()
                        .startObject()
                        .field("name", updateCVRequest.getName())
                        .field("profileId", updateCVRequest.getProfileId())
                        .field("pathFile", updateCVRequest.getPathFile())
                        .field("content", updateCVRequest.getContent())
                        .field("update_at", System.currentTimeMillis())
                        .endObject());
        try {
            UpdateResponse updateResponse = elasticClient.getClient().update(updateRequest).get();
            logger.info(updateResponse.status().toString());
            return updateResponse.status().toString();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Ex: ", e);
        }
        return "Id không tồn tại";
    }

    public void save(CV cv) {
        try {
            IndexResponse response = elasticClient.getClient()
                    .prepareIndex("resume", "cv", cv.getId())
                    .setSource(jsonBuilder()
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
