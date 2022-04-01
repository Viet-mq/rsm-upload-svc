package com.edso.resume.file.domain.repo;

import com.alibaba.fastjson.JSON;
import com.edso.resume.file.config.ElasticClient;
import com.edso.resume.file.domain.elasticsearch.ElasticFields;
import com.edso.resume.file.domain.elasticsearch.KeywordProcessing;
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
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.edso.resume.file.domain.elasticsearch.Elasticsearch.INDEX;
import static com.edso.resume.file.domain.elasticsearch.Elasticsearch.TYPE;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Repository
public class CvRepoImpl extends BaseService implements CvRepo {

    private final ElasticClient elasticClient;

    private final KeywordProcessing keywordProcessing;

    public CvRepoImpl(ElasticClient elasticClient, KeywordProcessing keywordProcessing) {
        this.elasticClient = elasticClient;
        this.keywordProcessing = keywordProcessing;
    }

    public List<Profile> findAll() {
        SearchResponse response = elasticClient.getClient()
                .prepareSearch(INDEX)
                .setRouting(TYPE)
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
                .prepareGet(INDEX, TYPE, String.valueOf(id))
                .get();
        return response.getSource();
    }

    public List<Profile> multiMatchQuery(String key, int size) {
        QueryBuilder query = keywordProcessing.queryKey(key);
        SearchResponse response = elasticClient.getClient()
                .prepareSearch(INDEX)
                .setRouting(TYPE)
                .setSource(new SearchSourceBuilder().query(query).size(size).minScore(2.0F))
                .execute()
                .actionGet();
        SearchHit[] searchHits = response
                .getHits()
                .getHits();
        return Arrays.stream(searchHits)
                .map(hit -> JSON.parseObject(hit.getSourceAsString(), Profile.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Profile> matchQuery(String key, int size) {
        QueryBuilder query = keywordProcessing.queryKey(key);
        SearchResponse response = elasticClient.getClient()
                .prepareSearch(INDEX)
                .setRouting(TYPE)
                .setSource(new SearchSourceBuilder().query(query).size(size))
                .execute()
                .actionGet();
        SearchHit[] searchHits = response
                .getHits()
                .getHits();
        return Arrays.stream(searchHits)
                .map(hit -> JSON.parseObject(hit.getSourceAsString(), Profile.class))
                .collect(Collectors.toList());
    }

    public Profile searchById(String id) {
        MultiMatchQueryBuilder query = QueryBuilders.multiMatchQuery(id, ElasticFields.ID);
        SearchResponse response = elasticClient.getClient()
                .prepareSearch(INDEX)
                .setRouting(TYPE)
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
                .prepareDelete(INDEX, TYPE, deleteCVRequest.getId())
                .get();
        return deleteResponse.getResult().toString();
    }

    public String update(UpdateCVRequest updateCVRequest) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(INDEX)
                .type(TYPE)
                .id(String.valueOf(updateCVRequest.getId()))
                .doc(jsonBuilder()
                        .startObject()
                        .field(ElasticFields.ID, updateCVRequest.getId())
                        .field(ElasticFields.FULL_NAME, updateCVRequest.getFullName())
                        .field(ElasticFields.GENDER, updateCVRequest.getGender())
                        .field(ElasticFields.PHONE_NUMBER, updateCVRequest.getPhoneNumber())
                        .field(ElasticFields.EMAIL, updateCVRequest.getEmail())
                        .field(ElasticFields.DATE_OF_BIRTH, updateCVRequest.getDateOfBirth())
                        .field(ElasticFields.HOME_TOWN, updateCVRequest.getHometown())
                        .field(ElasticFields.SCHOOL_ID, updateCVRequest.getSchoolId())
                        .field(ElasticFields.SCHOOL_NAME, updateCVRequest.getSchoolName())
                        .field(ElasticFields.JOB_ID, updateCVRequest.getJobId())
                        .field(ElasticFields.JOB_NAME, updateCVRequest.getJobName())
                        .field(ElasticFields.LEVEL_JOB_ID, updateCVRequest.getLevelJobId())
                        .field(ElasticFields.LEVEL_JOB_NAME, updateCVRequest.getLevelJobName())
                        .field(ElasticFields.CV, updateCVRequest.getCv())
                        .field(ElasticFields.SOURCE_CV_ID, updateCVRequest.getSourceCVId())
                        .field(ElasticFields.SOURCE_CV_NAME, updateCVRequest.getSourceCVName())
                        .field(ElasticFields.HR_REF, updateCVRequest.getHrRef())
                        .field(ElasticFields.DATE_OF_APPLY, updateCVRequest.getDateOfApply())
                        .field(ElasticFields.CV_TYPE, updateCVRequest.getCvType())
                        .field(ElasticFields.TALENT_POOL_ID, updateCVRequest.getTalentPoolId())
                        .field(ElasticFields.TALENT_POOL_NAME, updateCVRequest.getTalentPoolName())
                        .field(ElasticFields.LAST_APPLY, updateCVRequest.getLastApply())
                        .field(ElasticFields.MAIL_REF, updateCVRequest.getMailRef())
                        .field(ElasticFields.MAIL_REF2, updateCVRequest.getMailRef2())
                        .field(ElasticFields.LEVEL_SCHOOL, updateCVRequest.getLevelSchool())
                        .field(ElasticFields.RECRUITMENT_NAME, updateCVRequest.getRecruitmentName())
                        .field(ElasticFields.AVATAR_COLOR, updateCVRequest.getAvatarColor())
                        .field(ElasticFields.LINKEDIN, updateCVRequest.getLinkedin())
                        .field(ElasticFields.FACEBOOK, updateCVRequest.getFacebook())
                        .field(ElasticFields.SKYPE, updateCVRequest.getSkype())
                        .field(ElasticFields.GITHUB, updateCVRequest.getGithub())
                        .field(ElasticFields.OTHER_TECH, updateCVRequest.getOtherTech())
                        .field(ElasticFields.WEB, updateCVRequest.getWeb())
                        .field(ElasticFields.PIC_ID, updateCVRequest.getPicId())
                        .field(ElasticFields.PIC_NAME, updateCVRequest.getPicName())
                        .field(ElasticFields.STATUS, updateCVRequest.getStatus())
                        .field(ElasticFields.COMPANY_ID, updateCVRequest.getCompanyId())
                        .field(ElasticFields.COMPANY_NAME, updateCVRequest.getCompanyName())
                        .field(ElasticFields.USER_NAME, updateCVRequest.getUsername())
                        .field(ElasticFields.PIC_MAIL, updateCVRequest.getPicMail())
                        .field(ElasticFields.UPDATE_AT, System.currentTimeMillis())
                        .endObject());
        try {
            UpdateResponse updateResponse = elasticClient.getClient().update(updateRequest).get();
            return updateResponse.status().toString();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Ex: ", e);
        }
        return "Id không tồn tại";
    }

    public void updateStatus(String id, String statusId, String statusName) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(INDEX)
                .type(TYPE)
                .id(id)
                .doc(jsonBuilder()
                        .startObject()
                        .field(ElasticFields.STATUS_CV_ID, statusId)
                        .field(ElasticFields.STATUS_CV_NAME, statusName)
                        .field(ElasticFields.UPDATE_AT, System.currentTimeMillis())
                        .endObject());
        elasticClient.getClient().update(updateRequest);
    }

    public void updateImages(String id, String url) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(INDEX)
                .type(TYPE)
                .id(id)
                .doc(jsonBuilder()
                        .startObject()
                        .field(ElasticFields.IMAGE, url)
                        .field(ElasticFields.UPDATE_AT, System.currentTimeMillis())
                        .endObject());

        elasticClient.getClient().update(updateRequest);

    }

    public void deleteImages(String id) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(INDEX)
                .type(TYPE)
                .id(id)
                .doc(jsonBuilder()
                        .startObject()
                        .field(ElasticFields.IMAGE, "")
                        .field(ElasticFields.UPDATE_AT, System.currentTimeMillis())
                        .endObject());

        elasticClient.getClient().update(updateRequest);
    }

    public void save(Profile profile) {
        try {
            IndexResponse response = elasticClient.getClient()
                    .prepareIndex(INDEX, TYPE, profile.getId())
                    .setSource(jsonBuilder()
                            .startObject()
                            .field(ElasticFields.ID, profile.getId())
                            .field(ElasticFields.FULL_NAME, profile.getFullName() != null ? profile.getFullName() : null)
                            .field(ElasticFields.PHONE_NUMBER, profile.getPhoneNumber() != null ? profile.getPhoneNumber() : null)
                            .field(ElasticFields.GENDER, profile.getGender() != null ? profile.getGender() : null)
                            .field(ElasticFields.EMAIL, profile.getEmail() != null ? profile.getEmail() : null)
                            .field(ElasticFields.DATE_OF_BIRTH, profile.getDateOfBirth() != null ? profile.getDateOfBirth().toString() : null)
                            .field(ElasticFields.HOME_TOWN, profile.getHometown() != null ? profile.getHometown() : null)
                            .field(ElasticFields.SCHOOL_ID, profile.getSchoolId() != null ? profile.getSchoolId() : null)
                            .field(ElasticFields.SCHOOL_NAME, profile.getSchoolName() != null ? profile.getSchoolName() : null)
                            .field(ElasticFields.JOB_ID, profile.getJobId() != null ? profile.getJobId() : null)
                            .field(ElasticFields.JOB_NAME, profile.getJobName() != null ? profile.getJobName() : null)
                            .field(ElasticFields.LEVEL_JOB_ID, profile.getLevelJobId() != null ? profile.getLevelJobId() : null)
                            .field(ElasticFields.LEVEL_JOB_NAME, profile.getLevelJobName() != null ? profile.getLevelJobName() : null)
                            .field(ElasticFields.CV, profile.getCv() != null ? profile.getCv() : null)
                            .field(ElasticFields.SOURCE_CV_ID, profile.getSourceCVId() != null ? profile.getSourceCVId() : null)
                            .field(ElasticFields.SOURCE_CV_NAME, profile.getSourceCVName() != null ? profile.getSourceCVName() : null)
                            .field(ElasticFields.HR_REF, profile.getHrRef() != null ? profile.getHrRef() : null)
                            .field(ElasticFields.DATE_OF_APPLY, profile.getDateOfApply() != null ? profile.getDateOfApply().toString() : null)
                            .field(ElasticFields.CV_TYPE, profile.getCvType() != null ? profile.getCvType() : null)
                            .field(ElasticFields.STATUS_CV_ID, profile.getStatusCVId() != null ? profile.getStatusCVId() : null)
                            .field(ElasticFields.STATUS_CV_NAME, profile.getStatusCVName() != null ? profile.getStatusCVName() : null)
                            .field(ElasticFields.TALENT_POOL_ID, profile.getTalentPoolId() != null ? profile.getTalentPoolId() : null)
                            .field(ElasticFields.TALENT_POOL_NAME, profile.getTalentPoolName() != null ? profile.getTalentPoolName() : null)
                            .field(ElasticFields.CONTENT, profile.getContent() != null ? profile.getContent() : null)
                            .field(ElasticFields.URL_CV, profile.getUrlCV() != null ? profile.getUrlCV() : null)
                            .field(ElasticFields.IMAGE, profile.getImage() != null ? profile.getImage() : null)
                            .field(ElasticFields.FILE_NAME, profile.getFileName() != null ? profile.getFileName() : null)
                            .field(ElasticFields.SCHOOL_LEVEL, profile.getSchoolLevel() != null ? profile.getSchoolLevel() : null)
                            .field(ElasticFields.EVALUATION, profile.getEvaluation() != null ? profile.getEvaluation() : null)
                            .field(ElasticFields.DEPARTMENT_ID, profile.getDepartmentId() != null ? profile.getDepartmentId() : null)
                            .field(ElasticFields.DEPARTMENT_NAME, profile.getDepartmentName() != null ? profile.getDepartmentName() : null)
                            .field(ElasticFields.LAST_APPLY, profile.getLastApply() != null ? profile.getLastApply() : null)
                            .field(ElasticFields.MAIL_REF, profile.getMailRef() != null ? profile.getMailRef() : null)
                            .field(ElasticFields.MAIL_REF2, profile.getMailRef2() != null ? profile.getMailRef2() : null)
                            .field(ElasticFields.LEVEL_SCHOOL, profile.getLevelSchool() != null ? profile.getLevelSchool() : null)
                            .field(ElasticFields.RECRUITMENT_NAME, profile.getRecruitmentName() != null ? profile.getRecruitmentName() : null)
                            .field(ElasticFields.AVATAR_COLOR, profile.getAvatarColor() != null ? profile.getAvatarColor() : null)
                            .field(ElasticFields.LINKEDIN, profile.getLinkedin() != null ? profile.getLinkedin() : null)
                            .field(ElasticFields.FACEBOOK, profile.getFacebook() != null ? profile.getFacebook() : null)
                            .field(ElasticFields.SKYPE, profile.getSkype() != null ? profile.getSkype() : null)
                            .field(ElasticFields.GITHUB, profile.getGithub() != null ? profile.getGithub() : null)
                            .field(ElasticFields.OTHER_TECH, profile.getOtherTech() != null ? profile.getOtherTech() : null)
                            .field(ElasticFields.WEB, profile.getWeb() != null ? profile.getWeb() : null)
                            .field(ElasticFields.PIC_ID, profile.getPicId() != null ? profile.getPicId() : null)
                            .field(ElasticFields.PIC_NAME, profile.getPicName() != null ? profile.getPicName() : null)
                            .field(ElasticFields.STATUS, profile.getStatus() != null ? profile.getStatus() : null)
                            .field(ElasticFields.COMPANY_ID, profile.getCompanyId() != null ? profile.getCompanyId() : null)
                            .field(ElasticFields.COMPANY_NAME, profile.getCompanyName() != null ? profile.getCompanyName() : null)
                            .field(ElasticFields.USER_NAME, profile.getUsername() != null ? profile.getUsername() : null)
                            .field(ElasticFields.PIC_MAIL, profile.getPicMail() != null ? profile.getPicMail() : null)
                            .field(ElasticFields.CREATE_AT, System.currentTimeMillis())
                            .field(ElasticFields.UPDATE_AT, System.currentTimeMillis())
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
                    .prepareIndex(INDEX, TYPE, profile.getId())
                    .setSource(jsonBuilder()
                            .startObject()
                            .field(ElasticFields.ID, profile.getId())
                            .field(ElasticFields.CONTENT, profile.getContent())
                            .field(ElasticFields.URL_CV, profile.getUrlCV())
                            .field(ElasticFields.CV, profile.getFileName())
                            .field(ElasticFields.FILE_NAME, profile.getFileName())
                            .field(ElasticFields.CREATE_AT, System.currentTimeMillis())
                            .field(ElasticFields.UPDATE_AT, System.currentTimeMillis())
                            .endObject()
                    )
                    .get();
            logger.info(response.toString());
        } catch (IOException e) {
            logger.error("Ex: ", e);
        }
    }

}
