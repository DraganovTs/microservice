package com.microservices.service.impl;

import com.microservices.index.impl.TwitterIndexModel;
import com.microservices.repository.TwitterElasticsearchIndexRepository;
import com.microservices.service.ElasticIndexClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//  DOES THE SAME OF TWITTERELASTICINDEXCLIENT CLASS BUT WITH LESS CONTROL ON THE QUERY BUILD
/*
a.) ElasticsearchRepository provides convenient methods like save, find etc.

b.) ElasticsearchOperations gives chance of running low level queries like elasticsearch bool, must etc.

c.) ElasticsearchOperations requires to convert the input object to query objects
*/
@Service
@ConditionalOnProperty(name = "elastic-config.is-repository", havingValue = "true", matchIfMissing = true)
public class TwitterElasticRepositoryIndexClient implements ElasticIndexClient<TwitterIndexModel> {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticRepositoryIndexClient.class);

    private final TwitterElasticsearchIndexRepository twitterElasticsearchIndexRepository;

    public TwitterElasticRepositoryIndexClient(TwitterElasticsearchIndexRepository twitterElasticsearchIndexRepository) {
        this.twitterElasticsearchIndexRepository = twitterElasticsearchIndexRepository;
    }


    @Override
    public List<String> save(List<TwitterIndexModel> documents) {
        List<TwitterIndexModel> repositoryResponse =
                (List<TwitterIndexModel>) twitterElasticsearchIndexRepository.saveAll(documents);
        List<String> ids = repositoryResponse.stream().map(TwitterIndexModel::getId).collect(Collectors.toList());
        LOG.info("Documents indexed successfully with type: {} and ids: {}", TwitterIndexModel.class.getName(), ids);
        return ids;
    }
}
