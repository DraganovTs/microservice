package com.microservices.service;

import com.microservices.model.ElasticQueryWebClientRequestModel;
import com.microservices.model.ElasticQueryWebClientResponseModel;
import reactor.core.publisher.Flux;

public interface ElasticQueryWebClient {
    Flux<ElasticQueryWebClientResponseModel> getDataByText(ElasticQueryWebClientRequestModel request);

}
