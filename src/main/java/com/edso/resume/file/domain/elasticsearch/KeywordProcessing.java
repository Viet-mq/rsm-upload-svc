package com.edso.resume.file.domain.elasticsearch;

import com.edso.resume.file.domain.repo.QueryBuilderRepo;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class KeywordProcessing {

    private final QueryBuilderRepo normalRepo;
    private final QueryBuilderRepo emailRepo;
    private final QueryBuilderRepo matchRepo;

    public KeywordProcessing(
            @Qualifier("queryNormalRepo") QueryBuilderRepo normalRepo,
            @Qualifier("queryFindByEmail") QueryBuilderRepo emailRepo,
            @Qualifier("matchQuery") QueryBuilderRepo matchRepo) {
        this.normalRepo = normalRepo;
        this.emailRepo = emailRepo;
        this.matchRepo = matchRepo;
    }

    public QueryBuilder queryKey(String key) {
        if (key.contains("@"))
            return emailRepo.build(key);
        if (key.contains("\""))
            return matchRepo.build(key);
        return normalRepo.build(key);
    }

    private static MultiMatchQueryBuilder queryOrdinaryKey(String key) {

        return QueryBuilders.multiMatchQuery(key).type(MultiMatchQueryBuilder.Type.PHRASE_PREFIX)
                .field(ElasticFields.ID)
                .field(ElasticFields.FULL_NAME, 3)
                .field(ElasticFields.GENDER)
                .field(ElasticFields.PHONE_NUMBER)
                .field(ElasticFields.EMAIL, 3)
                .field(ElasticFields.HOME_TOWN)
                .field(ElasticFields.SCHOOL_ID)
                .field(ElasticFields.SCHOOL_NAME)
                .field(ElasticFields.JOB_ID)
                .field(ElasticFields.JOB_NAME, 3)
                .field(ElasticFields.LEVEL_JOB_ID)
                .field(ElasticFields.LEVEL_JOB_NAME, 3)
                .field(ElasticFields.CV, 2)
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
                .field(ElasticFields.DEPARTMENT_NAME, 3)
                .field(ElasticFields.EVALUATION)
                .field(ElasticFields.SCHOOL_LEVEL)
                .field(ElasticFields.LAST_APPLY)
                .field(ElasticFields.MAIL_REF)
                .field(ElasticFields.MAIL_REF2)
                .field(ElasticFields.LEVEL_SCHOOL)
                .field(ElasticFields.RECRUITMENT_NAME)
                .field(ElasticFields.AVATAR_COLOR)
                .field(ElasticFields.LINKEDIN)
                .field(ElasticFields.FACEBOOK)
                .field(ElasticFields.SKYPE)
                .field(ElasticFields.GITHUB)
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
