package com.microservices.service;

import com.microservices.model.ElasticQueryWebClientAnalyticsResponseModel;
import com.microservices.model.ElasticQueryWebClientRequestModel;

public interface ElasticQueryWebClient {
    ElasticQueryWebClientAnalyticsResponseModel getDataByText(ElasticQueryWebClientRequestModel requestModel);

}
