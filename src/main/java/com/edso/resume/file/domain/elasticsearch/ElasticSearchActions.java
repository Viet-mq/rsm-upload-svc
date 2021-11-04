package com.edso.resume.file.domain.elasticsearch;

import com.edso.resume.file.config.ElasticClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticSearchActions {
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchActions.class);

    private final ElasticClient elasticClient;

    public ElasticSearchActions(ElasticClient elasticClient){
        this.elasticClient = elasticClient;
    }

}
