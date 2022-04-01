package com.edso.resume.file.domain.repo;

import com.edso.resume.file.domain.elasticsearch.ElasticFields;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Repository;

@Repository
public class QueryNormalRepo implements QueryBuilderRepo {

    @Override
    public QueryBuilder build(String key) {
        String[] keyComponent = key.trim().split("  *");
        StringBuilder query = new StringBuilder(keyComponent[0]);
        for (int i = 1; i < keyComponent.length; i++) {
            query.append(" AND ").append(keyComponent[i]);
        }
        return QueryBuilders.queryStringQuery(query.toString()).type(MultiMatchQueryBuilder.Type.PHRASE_PREFIX)
                .field(ElasticFields.ID)
                .field(ElasticFields.FULL_NAME, 10)
                .field(ElasticFields.GENDER)
                .field(ElasticFields.PHONE_NUMBER)
                .field(ElasticFields.EMAIL, 5)
                .field(ElasticFields.HOME_TOWN)
                .field(ElasticFields.SCHOOL_ID)
                .field(ElasticFields.SCHOOL_NAME)
                .field(ElasticFields.JOB_ID)
                .field(ElasticFields.JOB_NAME, 5)
                .field(ElasticFields.LEVEL_JOB_ID)
                .field(ElasticFields.LEVEL_JOB_NAME, 5)
                .field(ElasticFields.CV)
                .field(ElasticFields.SOURCE_CV_ID)
                .field(ElasticFields.SOURCE_CV_NAME)
                .field(ElasticFields.HR_REF)
                .field(ElasticFields.CV_TYPE)
                .field(ElasticFields.STATUS_CV_ID)
                .field(ElasticFields.STATUS_CV_NAME)
                .field(ElasticFields.TALENT_POOL_ID)
                .field(ElasticFields.TALENT_POOL_NAME)
                .field(ElasticFields.URL_CV)
                .field(ElasticFields.IMAGE)
                .field(ElasticFields.DEPARTMENT_ID)
                .field(ElasticFields.DEPARTMENT_NAME, 5)
                .field(ElasticFields.EVALUATION)
                .field(ElasticFields.SCHOOL_LEVEL)
                .field(ElasticFields.LAST_APPLY)
                .field(ElasticFields.MAIL_REF)
                .field(ElasticFields.MAIL_REF2)
                .field(ElasticFields.LEVEL_SCHOOL)
                .field(ElasticFields.RECRUITMENT_NAME)
                .field(ElasticFields.AVATAR_COLOR)
                .field(ElasticFields.LINKEDIN, 2)
                .field(ElasticFields.FACEBOOK, 2)
                .field(ElasticFields.SKYPE, 2)
                .field(ElasticFields.GITHUB, 2)
                .field(ElasticFields.OTHER_TECH)
                .field(ElasticFields.WEB)
                .field(ElasticFields.PIC_ID)
                .field(ElasticFields.PIC_NAME)
                .field(ElasticFields.STATUS)
                .field(ElasticFields.COMPANY_ID)
                .field(ElasticFields.COMPANY_NAME)
                .field(ElasticFields.USER_NAME)
                .field(ElasticFields.PIC_MAIL)
                .field(ElasticFields.CONTENT, 3);
    }

}
