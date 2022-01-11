package com.edso.resume.file.domain.repo;

import org.elasticsearch.index.query.QueryBuilder;

public interface QueryBuilderRepo {
    QueryBuilder build(String key);
}
