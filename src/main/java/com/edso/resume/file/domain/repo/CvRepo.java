package com.edso.resume.file.domain.repo;

import com.alibaba.fastjson.JSON;
import com.edso.resume.file.config.ElasticClient;
import com.edso.resume.file.domain.entities.Profile;
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

    public List<Profile> findAll() {
        SearchResponse response = elasticClient.getClient()
                .prepareSearch("resume")
                .setRouting("profile")
                .execute()
                .actionGet();
        SearchHit[] searchHits = response
                .getHits()
                .getHits();
        return Arrays.stream(searchHits)
                .map(hit -> JSON.parseObject(hit.getSourceAsString(), Profile.class))
                .collect(Collectors.toList());
    }

    public Map<String, Object> findById(UUID id) {
        GetResponse response = elasticClient.getClient()
                .prepareGet("resume", "profile", String.valueOf(id))
                .get();
        return response.getSource();
    }

    public List<Profile> multiMatchQuery(String key) throws IOException {
        MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(key,
                "id",
                "fullName",
                "phoneNumber",
                "email",
                "hometown",
                "schoolId",
                "schoolName",
                "jobId",
                "jobName",
                "levelJobId",
                "levelJobName",
                "cv",
                "sourceCVId",
                "sourceCVName",
                "hrRef",
                "cvType",
                "statusCVId",
                "statusCVName",
                "content");
        SearchResponse response = elasticClient.getClient()
                .prepareSearch("resume")
                .setRouting("profile")
                .setQuery(multiMatchQuery)
                .execute()
                .actionGet();
        SearchHit[] searchHits = response
                .getHits()
                .getHits();
        return Arrays.stream(searchHits)
                .map(hit -> JSON.parseObject(hit.getSourceAsString(), Profile.class))
                .collect(Collectors.toList());
    }

    public Profile searchById(String id) throws IOException {
        MultiMatchQueryBuilder query = QueryBuilders.multiMatchQuery(id, "id");
        SearchResponse response = elasticClient.getClient()
                .prepareSearch("resume")
                .setRouting("profile")
                .setQuery(query)
                .execute()
                .actionGet();
        SearchHit[] searchHits = response
                .getHits()
                .getHits();
        List<Profile> ProfileList = Arrays.stream(searchHits)
                .map(hit -> JSON.parseObject(hit.getSourceAsString(), Profile.class))
                .collect(Collectors.toList());
        if (ProfileList.isEmpty()) return null;
        return ProfileList.get(0);
    }

    public String delete(DeleteCVRequest deleteCVRequest) {
        DeleteResponse deleteResponse = elasticClient.getClient()
                .prepareDelete("resume", "profile", deleteCVRequest.getId())
                .get();
        return deleteResponse.getResult().toString();
    }

    public String update(UpdateCVRequest updateCVRequest) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("resume")
                .type("profile")
                .id(String.valueOf(updateCVRequest.getId()))
                .doc(jsonBuilder()
                        .startObject()
                        .field("id", updateCVRequest.getId())
                        .field("fullName", updateCVRequest.getFullName())
                        .field("phoneNumber", updateCVRequest.getPhoneNumber())
                        .field("email", updateCVRequest.getEmail())
                        .field("dateOfBirth", updateCVRequest.getDateOfBirth())
                        .field("hometown", updateCVRequest.getHometown())
                        .field("schoolId", updateCVRequest.getSchoolId())
                        .field("schoolName", updateCVRequest.getSchoolName())
                        .field("jobId", updateCVRequest.getJobId())
                        .field("jobName", updateCVRequest.getJobName())
                        .field("levelJobId", updateCVRequest.getLevelJobId())
                        .field("levelJobName", updateCVRequest.getLevelJobName())
                        .field("cv", updateCVRequest.getCv())
                        .field("sourceCVId", updateCVRequest.getSourceCVId())
                        .field("sourceCVName", updateCVRequest.getSourceCVName())
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

    public void updateStatus(String id, String statusId, String statusName) throws IOException{
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("resume")
                .type("profile")
                .id(id)
                .doc(jsonBuilder()
                        .startObject()
                        .field("statusCVId", statusId)
                        .field("statusCVName", statusName)
                        .field("update_at", System.currentTimeMillis())
                        .endObject());
        try {
            UpdateResponse updateResponse = elasticClient.getClient().update(updateRequest).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Ex: ", e);
        }
    }

    public void save(Profile profile) {
        try {
            IndexResponse response = elasticClient.getClient()
                    .prepareIndex("resume", "profile", profile.getId())
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("id", profile.getId())
                            .field("fullName", profile.getFullName() != null?profile.getFullName():null)
                            .field("phoneNumber", profile.getPhoneNumber()!=null?profile.getPhoneNumber():null)
                            .field("email", profile.getEmail()!=null?profile.getEmail():null)
                            .field("dateOfBirth", profile.getDateOfBirth()!=null?profile.getDateOfBirth().toString():null)
                            .field("hometown", profile.getHometown()!=null?profile.getHometown():null)
                            .field("schoolId", profile.getSchoolId()!=null?profile.getSchoolId():null)
                            .field("schoolName", profile.getSchoolName()!=null?profile.getSchoolName():null)
                            .field("jobId", profile.getJobId()!=null?profile.getJobId():null)
                            .field("jobName", profile.getJobName()!=null?profile.getJobName():null)
                            .field("levelJobId", profile.getLevelJobId()!=null?profile.getLevelJobId():null)
                            .field("levelJobName", profile.getLevelJobName()!=null?profile.getLevelJobName():null)
                            .field("cv", profile.getCv()!=null?profile.getCv():null)
                            .field("sourceCVId", profile.getSourceCVId()!=null?profile.getSourceCVId():null)
                            .field("sourceCVName", profile.getSourceCVName()!=null?profile.getSourceCVName():null)
                            .field("hrRef", profile.getHrRef()!=null?profile.getHrRef():null)
                            .field("dateOfApply", profile.getDateOfApply()!=null?profile.getDateOfApply().toString():null)
                            .field("cvType", profile.getCvType()!=null?profile.getCvType():null)
                            .field("statusCVId", profile.getStatusCVId()!=null?profile.getStatusCVId():null)
                            .field("statusCVName", profile.getStatusCVName()!=null?profile.getStatusCVName():null)
                            .field("content", profile.getContent()!=null?profile.getContent():null)
                            .field("url", profile.getUrl()!=null?profile.getUrl():null)
                            .field("fileName", profile.getFileName()!=null?profile.getFileName():null)
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

    public void saveContent(Profile profile) {
        try {
            IndexResponse response = elasticClient.getClient()
                    .prepareIndex("resume", "profile", profile.getId())
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("id", profile.getId())
                            .field("content", profile.getContent())
                            .field("url", profile.getUrl())
                            .field("fileName", profile.getFileName())
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
