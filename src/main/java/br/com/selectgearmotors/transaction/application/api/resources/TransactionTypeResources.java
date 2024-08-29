package br.com.selectgearmotors.transaction.application.api.resources;

import br.com.selectgearmotors.transaction.application.api.dto.request.TransactionTypeRequest;
import br.com.selectgearmotors.transaction.application.api.dto.response.TransactionTypeResponse;
import br.com.selectgearmotors.transaction.application.api.exception.ResourceFoundException;
import br.com.selectgearmotors.transaction.application.api.mapper.TransactionTypeApiMapper;
import br.com.selectgearmotors.transaction.commons.Constants;
import br.com.selectgearmotors.transaction.commons.util.RestUtils;
import br.com.selectgearmotors.transaction.core.domain.TransactionType;
import br.com.selectgearmotors.transaction.core.ports.in.transactiontype.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/product-categories")
@CrossOrigin(origins = "*", allowedHeaders = "Content-Type, Authorization", maxAge = 3600)
public class TransactionTypeResources {

    private final CreateTransactionTypePort createTransactionTypePort;
    private final DeleteTransactionTypePort deleteTransactionTypePort;
    private final FindByIdTransactionTypePort findByIdTransactionTypePort;
    private final FindTransactionTypesPort findTransactionTypesPort;
    private final UpdateTransactionTypePort updateTransactionTypePort;
    private final TransactionTypeApiMapper productCategoryApiMapper;

    @Autowired
    public TransactionTypeResources(CreateTransactionTypePort createTransactionTypePort, DeleteTransactionTypePort deleteTransactionTypePort, FindByIdTransactionTypePort findByIdTransactionTypePort, FindTransactionTypesPort findTransactionTypesPort, UpdateTransactionTypePort updateTransactionTypePort, TransactionTypeApiMapper productCategoryApiMapper) {
        this.createTransactionTypePort = createTransactionTypePort;
        this.deleteTransactionTypePort = deleteTransactionTypePort;
        this.findByIdTransactionTypePort = findByIdTransactionTypePort;
        this.findTransactionTypesPort = findTransactionTypesPort;
        this.updateTransactionTypePort = updateTransactionTypePort;
        this.productCategoryApiMapper = productCategoryApiMapper;
    }

    @Operation(summary = "Create a new TransactionType", tags = {"productCategorys", "post"})
    @ApiResponse(responseCode = "201", content = {
            @Content(schema = @Schema(implementation = TransactionTypeResources.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TransactionTypeResponse> save(@Valid @RequestBody TransactionTypeRequest request) {
        try {
            log.info("Chegada do objeto para ser salvo {}", request);
            TransactionType productCategory = productCategoryApiMapper.fromRequest(request);
            TransactionType saved = createTransactionTypePort.save(productCategory);
            if (saved == null) {
                throw new ResourceFoundException("Produto não encontroado ao cadastrar");
            }

            TransactionTypeResponse productCategoryResponse = productCategoryApiMapper.fromEntity(saved);
            URI location = RestUtils.getUri(productCategoryResponse.getId());
            return ResponseEntity.created(location).body(productCategoryResponse);
        } catch (Exception ex) {
            log.error(Constants.ERROR_EXCEPTION_RESOURCE + "-save: {}", ex.getMessage());
            return ResponseEntity.ok().build();
        }
    }

    @Operation(summary = "Update a TransactionType by Id", tags = {"productCategorys", "put"})
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = TransactionTypeResources.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TransactionTypeResponse> update(@PathVariable("id") Long id, @Valid @RequestBody TransactionTypeRequest request) {
        try {
            log.info("Chegada do objeto para ser alterado {}", request);
            var productCategory = productCategoryApiMapper.fromRequest(request);
            TransactionType updated = updateTransactionTypePort.update(id, productCategory);
            if (updated == null) {
                throw new ResourceFoundException("\"Produto não encontroado ao atualizar");
            }

            TransactionTypeResponse productCategoryResponse = productCategoryApiMapper.fromEntity(updated);
            return ResponseEntity.ok(productCategoryResponse);
        } catch (Exception ex) {
            log.error(Constants.ERROR_EXCEPTION_RESOURCE + "-update: {}", ex.getMessage());
            return ResponseEntity.ok().build();
        }
    }

    @Operation(summary = "Retrieve all TransactionType", tags = {"productCategorys", "get", "filter"})
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = TransactionTypeResources.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "204", description = "There are no Associations", content = {
            @Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TransactionTypeResponse>> findAll() {
        List<TransactionType> productCategoryList = findTransactionTypesPort.findAll();
        List<TransactionTypeResponse> productCategoryResponse = productCategoryApiMapper.map(productCategoryList);
        return productCategoryResponse.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(productCategoryResponse);
    }

    @Operation(
            summary = "Retrieve a TransactionType by Id",
            description = "Get a TransactionType object by specifying its id. The response is Association object with id, title, description and published status.",
            tags = {"productCategorys", "get"})
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = TransactionTypeResources.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TransactionTypeResponse> findOne(@PathVariable("id") Long id) {
        try {
            TransactionType productCategorySaved = findByIdTransactionTypePort.findById(id);
            if (productCategorySaved == null) {
                throw new ResourceFoundException("Produto não encontrado ao buscar por código");
            }

            TransactionTypeResponse productCategoryResponse = productCategoryApiMapper.fromEntity(productCategorySaved);
            return ResponseEntity.ok(productCategoryResponse);

        } catch (Exception ex) {
            log.error(Constants.ERROR_EXCEPTION_RESOURCE + "-findOne: {}", ex.getMessage());
            return ResponseEntity.ok().build();
        }
    }

    @Operation(summary = "Delete a TransactionType by Id", tags = {"productCategorytrus", "delete"})
    @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> remove(@PathVariable("id") Long id) {
        deleteTransactionTypePort.remove(id);
        return ResponseEntity.noContent().build();
    }
}