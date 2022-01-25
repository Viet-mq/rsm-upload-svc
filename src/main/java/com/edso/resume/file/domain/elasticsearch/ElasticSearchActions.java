package com.edso.resume.file.domain.elasticsearch;

import com.edso.resume.file.config.ElasticClient;
import com.edso.resume.file.domain.entities.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ElasticSearchActions {
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchActions.class);

    private final ElasticClient elasticClient;

    public ElasticSearchActions(ElasticClient elasticClient) {
        this.elasticClient = elasticClient;
    }

    public List<Profile> customQuery(String filter, String key) {


        return null;
    }

}
