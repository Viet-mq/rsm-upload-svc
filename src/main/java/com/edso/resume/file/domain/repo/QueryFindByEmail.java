package com.edso.resume.file.domain.repo;

import com.edso.resume.file.domain.elasticsearch.ElasticFields;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Repository;

@Repository
public class QueryFindByEmail implements QueryBuilderRepo {

    @Override
    public QueryBuilder build(String key) {
        String emailKey = key.substring(0, key.indexOf("@")) + " AND " + key.substring(key.indexOf("@") + 1);
        return QueryBuilders.queryStringQuery(emailKey).type(MultiMatchQueryBuilder.Type.PHRASE_PREFIX)
                .field(ElasticFields.EMAIL)
                .field(ElasticFields.CONTENT);
    }

}
