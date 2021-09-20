package com.edso.resume.file.config;

import com.alibaba.fastjson.JSON;
import com.edso.resume.file.domain.entities.CV;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElasticClient {

    @Value("${elasticsearch.clustername}")
    private String clusterName;
    @Value("${elasticsearch.host}")
    private String host;
    @Value("${elasticsearch.port}")
    private int port;

    private PreBuiltTransportClient client;

    public PreBuiltTransportClient getClient() {
        return client;
    }

    @PostConstruct
    public void setUpClient() throws UnknownHostException {
        final Settings esSettings = Settings.builder().put("cluster.name", clusterName).build();
        client = new PreBuiltTransportClient(esSettings);
        client.addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
    }

    public void searchAll() throws UnknownHostException {
        SearchResponse response = getClient()
                .prepareSearch()
                .execute()
                .actionGet();
        SearchHit[] searchHits = response
                .getHits()
                .getHits();
        System.out.println("searchAll searchHits:" + searchHits[0]);
        List<CV> results = Arrays.stream(searchHits)
                .map(hit -> JSON.parseObject(hit.getSourceAsString(), CV.class))
                .collect(Collectors.toList());
        System.out.println("results:" + results);
        System.out.println("--------------------------------------------------");
    }

    public static void main(String[] args) throws UnknownHostException {
        ElasticClient client = new ElasticClient();
        client.setUpClient();
        client.searchAll();
    }

}
