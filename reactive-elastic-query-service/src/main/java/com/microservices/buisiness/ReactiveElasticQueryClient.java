package com.microservices.buisiness;

import com.microservices.index.IndexModel;
import com.microservices.index.impl.TwitterIndexModel;
import reactor.core.publisher.Flux;

public interface ReactiveElasticQueryClient<T extends IndexModel> {
    Flux<TwitterIndexModel> getIndexModelByText(String text);

}
