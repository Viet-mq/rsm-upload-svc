package com.edso.resume.file.domain.repo;

import com.edso.resume.file.domain.elasticsearch.ElasticFields;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Repository;

@Repository
public class MatchQuery implements QueryBuilderRepo{

    @Override
    public QueryBuilder build(String key) {
        String query = key.replaceAll("\"", "");
        return QueryBuilders.matchQuery(query, ElasticFields.FULL_NAME);
    }
}
