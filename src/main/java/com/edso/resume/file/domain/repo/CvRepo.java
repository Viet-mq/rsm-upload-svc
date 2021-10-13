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
                "fullName",
                "phoneNumber",
                "email",
                //"dateOfBirth",
                "hometown",
                "school",
                "job",
                "levelJob",
                "cv",
                "sourceCV",
                "hrRef",
                //"dateOfApply",
                "cvType",
                "statusCV",
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

    public CV searchById(String id) throws IOException {
        MultiMatchQueryBuilder query = QueryBuilders.multiMatchQuery(id, "id");
        SearchResponse response = elasticClient.getClient()
                .prepareSearch("resume")
                .setRouting("cv")
                .setQuery(query)
                .execute()
                .actionGet();
        SearchHit[] searchHits = response
                .getHits()
                .getHits();
        List<CV> CVList = Arrays.stream(searchHits)
                .map(hit -> JSON.parseObject(hit.getSourceAsString(), CV.class))
                .collect(Collectors.toList());
        if (CVList.isEmpty()) return null;
        return CVList.get(0);
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
                        .field("id", updateCVRequest.getId())
                        .field("fullName", updateCVRequest.getFullName())
                        .field("phoneNumber", updateCVRequest.getPhoneNumber())
                        .field("email", updateCVRequest.getEmail())
                        .field("dateOfBirth", updateCVRequest.getDateOfBirth())
                        .field("hometown", updateCVRequest.getHometown())
                        .field("school", updateCVRequest.getSchool())
                        .field("job", updateCVRequest.getJob())
                        .field("levelJob", updateCVRequest.getLevelJob())
                        .field("cv", updateCVRequest.getCv())
                        .field("sourceCV", updateCVRequest.getSourceCV())
                        .field("hrRef", updateCVRequest.getHrRef())
                        .field("dateOfApply", updateCVRequest.getDateOfApply())
                        .field("cvType", updateCVRequest.getCvType())
                        .field("update_at", System.currentTimeMillis())
                        .endObject());
        try {
            UpdateResponse updateResponse = elasticClient.getClient().update(updateRequest).get();
            return updateResponse.status().toString();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Ex: ", e);
        }
        return "Id không tồn tại";
    }

    public void updateStatus(String id, String status) throws IOException{
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("resume")
                .type("cv")
                .id(id)
                .doc(jsonBuilder()
                        .startObject()
                        .field("statusCV", status)
                        .field("update_at", System.currentTimeMillis())
                        .endObject());
        try {
            UpdateResponse updateResponse = elasticClient.getClient().update(updateRequest).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Ex: ", e);
        }
    }

    public void save(CV cv) {
        try {
            IndexResponse response = elasticClient.getClient()
                    .prepareIndex("resume", "cv", cv.getId())
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("id", cv.getId())
                            .field("fullName", cv.getFullName())
                            .field("phoneNumber", cv.getPhoneNumber())
                            .field("email", cv.getEmail())
                            .field("dateOfBirth", cv.getDateOfBirth().toString())
                            .field("hometown", cv.getHometown())
                            .field("school", cv.getSchool())
                            .field("job", cv.getJob())
                            .field("levelJob", cv.getLevelJob())
                            .field("cv", cv.getCv())
                            .field("sourceCV", cv.getSourceCV())
                            .field("hrRef", cv.getHrRef())
                            .field("dateOfApply", cv.getDateOfApply().toString())
                            .field("cvType", cv.getCvType())
                            .field("statusCV", cv.getStatusCV())
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

    public void saveContent(CV cv) {
        try {
            IndexResponse response = elasticClient.getClient()
                    .prepareIndex("resume", "cv", cv.getId())
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("id", cv.getId())
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
