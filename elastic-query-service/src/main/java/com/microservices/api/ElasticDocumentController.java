package com.microservices.api;

import com.microservices.buisiness.ElasticQueryService;
import com.microservices.model.ElasticQueryServiceRequestModel;
import com.microservices.model.ElasticQueryServiceResponseModel;
import com.microservices.model.ElasticQueryServiceResponseModelV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
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

    public ElasticDocumentController(ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
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
    @PreAuthorize("hasPermission(#id, 'ElasticQueryServiceResponseModel','READ')")
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

    @Operation(summary = "Get elastic document by id V2")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Successful response", content = {
                            @Content(mediaType = "application/vnd.api.v2+json",
                                    schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Not Found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            } )
    @GetMapping(value = "/{id}", produces = "application/vnd.api.v2+json")
    public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModelV2> getDocumentByIdV2(@PathVariable @NotEmpty String id) {
        ElasticQueryServiceResponseModel elasticQueryServiceResponseModel = elasticQueryService.getDocumentById(id);
        ElasticQueryServiceResponseModelV2 responseModelV2 = getV2Model(elasticQueryServiceResponseModel);
        LOG.info("Elasticsearch returned document with id {}", id);
        return ResponseEntity.ok(responseModelV2);
    }
    @PreAuthorize("hasRole('APP_USER_ROLE') || hasRole('APP_SUPER_USER_ROLE') || hasAuthority('SCOPE_APP_USER_ROLE')")
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
    ResponseEntity<List<ElasticQueryServiceResponseModel>>
    getDocumentByText(@RequestBody @Valid ElasticQueryServiceRequestModel elasticQueryServiceRequestModel) {


        List<ElasticQueryServiceResponseModel> response = elasticQueryService.getDocumentByText(elasticQueryServiceRequestModel.getText());
        LOG.info("Elasticsearch returned {} of documents", response.size());


        return ResponseEntity.ok(response);
    }

    private ElasticQueryServiceResponseModelV2 getV2Model(ElasticQueryServiceResponseModel responseModel) {
        ElasticQueryServiceResponseModelV2 responseModelV2 = ElasticQueryServiceResponseModelV2.builder()
                .id(Long.parseLong(responseModel.getId()))
                .userId(responseModel.getUserId())
                .text(responseModel.getText())
                .textV2("Version 2 text")
                .build();

        responseModelV2.add(responseModel.getLinks());
        return responseModelV2;
    }
}