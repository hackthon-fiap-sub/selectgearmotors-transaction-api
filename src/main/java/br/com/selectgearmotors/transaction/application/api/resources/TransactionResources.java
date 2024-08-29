package br.com.selectgearmotors.transaction.application.api.resources;

import br.com.selectgearmotors.transaction.application.api.dto.request.TransactionRequest;
import br.com.selectgearmotors.transaction.application.api.dto.response.TransactionResponse;
import br.com.selectgearmotors.transaction.application.api.exception.ResourceFoundException;
import br.com.selectgearmotors.transaction.application.api.mapper.TransactionApiMapper;
import br.com.selectgearmotors.transaction.commons.Constants;
import br.com.selectgearmotors.transaction.commons.util.RestUtils;
import br.com.selectgearmotors.transaction.core.domain.Transaction;
import br.com.selectgearmotors.transaction.core.ports.in.transaction.*;
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
@RequestMapping("/v1/products")
@CrossOrigin(origins = "*", allowedHeaders = "Content-Type, Authorization", maxAge = 3600)
public class TransactionResources {

    private final CreateTransactionPort createTransactionPort;
    private final DeleteTransactionPort deleteTransactionPort;
    private final FindByIdTransactionPort findByIdTransactionPort;
    private final FindTransactionsPort findTransactionsPort;
    private final UpdateTransactionPort updateTransactionPort;
    private final TransactionApiMapper transactionApiMapper;

    @Autowired
    public TransactionResources(CreateTransactionPort createTransactionPort, DeleteTransactionPort deleteTransactionPort, FindByIdTransactionPort findByIdTransactionPort, FindTransactionsPort findTransactionsPort, UpdateTransactionPort updateTransactionPort, TransactionApiMapper transactionApiMapper) {
        this.createTransactionPort = createTransactionPort;
        this.deleteTransactionPort = deleteTransactionPort;
        this.findByIdTransactionPort = findByIdTransactionPort;
        this.findTransactionsPort = findTransactionsPort;
        this.updateTransactionPort = updateTransactionPort;
        this.transactionApiMapper = transactionApiMapper;
    }

    @Operation(summary = "Create a new Transaction", tags = {"products", "post"})
    @ApiResponse(responseCode = "201", content = {
            @Content(schema = @Schema(implementation = TransactionResources.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TransactionResponse> save(@Valid @RequestBody TransactionRequest request) {
        try {
            log.info("Chegada do objeto para ser salvo {}", request);
            Transaction transaction = transactionApiMapper.fromRequest(request);
            Transaction saved = createTransactionPort.save(transaction);
            if (saved == null) {
                throw new ResourceFoundException("Produto não encontroado ao cadastrar");
            }

            TransactionResponse productResponse = transactionApiMapper.fromEntity(saved);
            URI location = RestUtils.getUri(productResponse.getId());

            return ResponseEntity.created(location).body(productResponse);
        } catch (Exception ex) {
            log.error(Constants.ERROR_EXCEPTION_RESOURCE + "-save: {}", ex.getMessage());
            return ResponseEntity.ok().build();
        }
    }

    @Operation(summary = "Update a Transaction by Id", tags = {"products", "put"})
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = TransactionResources.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TransactionResponse> update(@PathVariable("id") Long id, @Valid @RequestBody TransactionRequest request) {
        try {
            log.info("Chegada do objeto para ser alterado {}", request);
            var product = transactionApiMapper.fromRequest(request);
            Transaction updated = updateTransactionPort.update(id, product);
            if (updated == null) {
                throw new ResourceFoundException("Produto não encontroado ao atualizar");
            }

            TransactionResponse productResponse = transactionApiMapper.fromEntity(updated);
            return ResponseEntity.ok(productResponse);
        } catch (Exception ex) {
            log.error(Constants.ERROR_EXCEPTION_RESOURCE + "-update: {}", ex.getMessage());
            return ResponseEntity.ok().build();
        }
    }

    @Operation(summary = "Retrieve all Transaction", tags = {"products", "get", "filter"})
    @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = TransactionResources.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "204", description = "There are no Associations", content = {
            @Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TransactionResponse>> findAll() {
        List<Transaction> productList = findTransactionsPort.findAll();
        List<TransactionResponse> productResponse = transactionApiMapper.map(productList);
        return productResponse.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(productResponse);
    }

    @Operation(
            summary = "Retrieve a Transaction by Id",
            description = "Get a Transaction object by specifying its id. The response is Association object with id, title, description and published status.",
            tags = {"products", "get"})
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = TransactionResources.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TransactionResponse> findOne(@PathVariable("id") Long id) {
        try {
            Transaction productSaved = findByIdTransactionPort.findById(id);
            if (productSaved == null) {
                throw new ResourceFoundException("Produto não encontrado ao buscar por id");
            }

            TransactionResponse productResponse = transactionApiMapper.fromEntity(productSaved);
            return ResponseEntity.ok(productResponse);
        } catch (Exception ex) {
            log.error(Constants.ERROR_EXCEPTION_RESOURCE + "-findOne: {}", ex.getMessage());
            return ResponseEntity.ok().build();
        }
    }

    @Operation(
            summary = "Retrieve a Transaction by Id",
            description = "Get a Transaction object by specifying its id. The response is Association object with id, title, description and published status.",
            tags = {"products", "get"})
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = TransactionResources.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @GetMapping("/code/{code}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TransactionResponse> findByCode(@PathVariable("code") String code) {
        try {

            Transaction productSaved = findByIdTransactionPort.findByCode(code);
            if (productSaved == null) {
                throw new ResourceFoundException("Produto não encontrado ao buscar por código");
            }

            TransactionResponse productResponse = transactionApiMapper.fromEntity(productSaved);
            return ResponseEntity.ok(productResponse);
        } catch (Exception ex) {
            log.error(Constants.ERROR_EXCEPTION_RESOURCE + "-findByCode: {}", ex.getMessage());
            return ResponseEntity.ok().build();
        }
    }

    @Operation(summary = "Delete a Transaction by Id", tags = {"producttrus", "delete"})
    @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())})
    @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> remove(@PathVariable("id") Long id) {
        deleteTransactionPort.remove(id);
        return ResponseEntity.noContent().build();
    }
}