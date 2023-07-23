package com.microservices.buisiness;

import com.microservices.model.ElasticQueryServiceAnalyticsResponseModel;
import com.microservices.model.ElasticQueryServiceResponseModel;

import java.util.List;

public interface ElasticQueryService {

    ElasticQueryServiceResponseModel getDocumentById(String id);

    ElasticQueryServiceAnalyticsResponseModel getDocumentByText(String text, String accessToken);

    List<ElasticQueryServiceResponseModel> getAllDocuments();
}
