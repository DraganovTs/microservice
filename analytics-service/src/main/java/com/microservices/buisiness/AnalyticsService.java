package com.microservices.buisiness;

import com.microservices.model.AnalyticsResponseModel;

import java.util.Optional;

public interface AnalyticsService {
    Optional<AnalyticsResponseModel> getWordAnalytics(String word);

}
