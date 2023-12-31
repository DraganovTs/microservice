package com.microservices.buisiness.impl;

import com.microservices.buisiness.AnalyticsService;
import com.microservices.dataaccess.repository.AnalyticsRepository;
import com.microservices.model.AnalyticsResponseModel;
import com.microservices.transformer.EntityToResponseModelTransformer;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TwitterAnalyticsService implements AnalyticsService {
    private final AnalyticsRepository analyticsRepository;
    private final EntityToResponseModelTransformer entityToResponseModelTransformer;

    public TwitterAnalyticsService(AnalyticsRepository analyticsRepository, EntityToResponseModelTransformer entityToResponseModelTransformer) {
        this.analyticsRepository = analyticsRepository;
        this.entityToResponseModelTransformer = entityToResponseModelTransformer;
    }

    @Override
    public Optional<AnalyticsResponseModel> getWordAnalytics(String word) {
        return entityToResponseModelTransformer.getResponseModel(
                analyticsRepository.getAnalyticsEntitiesByWord(word, PageRequest.of(0, 1))
                        .stream().findFirst().orElse(null));
    }
}
