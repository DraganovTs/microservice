package com.microservices.api;

import com.microservices.buisiness.ElasticQueryService;
import com.microservices.model.ElasticQueryServiceAnalyticsResponseModel;
import com.microservices.model.ElasticQueryServiceRequestModel;
import com.microservices.model.ElasticQueryServiceResponseModel;
import com.microservices.model.ElasticQueryServiceResponseModelV2;
import com.microservices.security.TwitterQueryUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping(value = "/documents", produces = "application/vnd.api.v1+json")
public class ElasticDocumentController {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticDocumentController.class);

    private final ElasticQueryService elasticQueryService;

    @Value("${server.port}")
    private String port;
    public ElasticDocumentController(ElasticQueryService queryService) {
        this.elasticQueryService = queryService;
    }

    @PostAuthorize("hasPermission(returnObject, 'READ')")
    @Operation(summary = "Get all elastic documents")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Successful response", content = {
                            @Content(mediaType = "application/vnd.api.v1+json",
                                    schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Not Found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    @GetMapping("/")
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments() {
        List<ElasticQueryServiceResponseModel> response = elasticQueryService.getAllDocuments();
        LOG.info("Elasticsearch returned {} of documents", response.size());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasPermission(#id, 'ElasticQueryServiceResponseModel', 'READ')")
    @Operation(summary = "Get elastic document by id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Successful response", content = {
                            @Content(mediaType = "application/vnd.api.v1+json",
                                    schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Not Found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            } )
    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModel> getDocumentById(@PathVariable @NotEmpty String id) {
        ElasticQueryServiceResponseModel elasticQueryServiceResponseModel =
                elasticQueryService.getDocumentById(id);
        LOG.info("Elasticsearch returned document with id {}", id);
        return ResponseEntity.ok(elasticQueryServiceResponseModel);
    }

    @PreAuthorize("hasRole('APP_USER_ROLE') || hasRole('APP_SUPER_USER_ROLE')")
    @PostAuthorize("hasPermission(returnObject, 'READ')")
    @Operation(summary = "Get all documents by text")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Successful response", content = {
                            @Content(mediaType = "application/vnd.api.v1+json",
                                    schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Not Found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    @PostMapping("/get-document-by-text")
    public @ResponseBody
    ResponseEntity<ElasticQueryServiceAnalyticsResponseModel>
    getDocumentByText(@RequestBody @Valid ElasticQueryServiceRequestModel elasticQueryServiceRequestModel,
                      @AuthenticationPrincipal TwitterQueryUser principal,
                      @RegisteredOAuth2AuthorizedClient("keycloak")
                      OAuth2AuthorizedClient oAuth2AuthorizedClient) {
        LOG.info("User {} querying documents for text {}", principal.getUsername(),
                elasticQueryServiceRequestModel.getText());

        ElasticQueryServiceAnalyticsResponseModel response =
                elasticQueryService.getDocumentByText(elasticQueryServiceRequestModel.getText(),
                        oAuth2AuthorizedClient.getAccessToken().getTokenValue());
        LOG.info("Elasticsearch returned {} of documents on port {}",
                response.getQueryResponseModels().size(), port);
        return ResponseEntity.ok(response);
    }

    /*
    In case we do a breaking change in the API we add the produces that needs to be specificied in negotiation
         @Operation(summary = "Get all elastic documents")
    @ApiResponse(
            value = {
        @ApiResponses(responseCode =  "200", description = "Successful response", content = {
                @Content(mediaType = "application/vnd.api.v2+json",
                schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))
        }),
        @ApiResponse(responseCode = "400", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
       })
     @GetMapping(value = "/{id}", produces = "application/vnd.api.v2+json")
     @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModel> getDocumentById(@PathVariable @NotEmpty String id) {
        ElasticQueryServiceResponseModel elasticQueryServiceResponseModel =
                elasticQueryService.getDocumentById(id);
        LOG.info("Elasticsearch returned document with id {}", id);
        return ResponseEntity.ok(elasticQueryServiceResponseModel);
    }
     */
}
