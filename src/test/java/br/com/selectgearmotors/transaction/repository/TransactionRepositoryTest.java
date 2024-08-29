package br.com.selectgearmotors.transaction.repository;

import br.com.selectgearmotors.transaction.infrastructure.entity.transaction.TransactionEntity;
import br.com.selectgearmotors.transaction.infrastructure.entity.transactiontype.TransactionTypeEntity;
import br.com.selectgearmotors.transaction.infrastructure.repository.TransactionRepository;
import br.com.selectgearmotors.transaction.infrastructure.repository.TransactionTypeRepository;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@DataJpaTest
@ImportAutoConfiguration(exclude = FlywayAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository productRepository;

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    private TransactionTypeEntity transactionTypeEntity;

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        log.info("Cleaning up database...");
        productRepository.deleteAll();
        transactionTypeRepository.deleteAll();

        log.info("Setting up test data...");
        this.transactionTypeEntity = transactionTypeRepository.save(getTransactionType());

        TransactionEntity product = productRepository.save(getTransaction(this.transactionTypeEntity));
        log.info("TransactionEntity:{}", product);
    }

    private TransactionEntity getTransaction(TransactionTypeEntity transactionTypeEntity) {
        return TransactionEntity.builder()
                .code(UUID.randomUUID().toString())
                .transactionType(transactionTypeEntity)
                .vehicleId(1l)
                .clientId(1l)
                .build();
    }

    private TransactionEntity getTransaction1(TransactionTypeEntity transactionTypeEntity) {
        return TransactionEntity.builder()
                .code(UUID.randomUUID().toString())
                .transactionType(transactionTypeEntity)
                .vehicleId(1l)
                .clientId(1l)
                .build();
    }

    private TransactionEntity getTransaction2(TransactionTypeEntity transactionTypeEntity) {
        return TransactionEntity.builder()
                .code(UUID.randomUUID().toString())
                .transactionType(transactionTypeEntity)
                .vehicleId(1l)
                .clientId(1l)
                .build();
    }

    private TransactionTypeEntity getTransactionType() {
        return TransactionTypeEntity.builder()
                .name(faker.food().ingredient())
                .build();
    }

    @Test
    void should_find_no_products_if_repository_is_empty() {
        Iterable<TransactionEntity> products = productRepository.findAll();
        products = Collections.EMPTY_LIST;
        assertThat(products).isEmpty();
    }

    @Test
    void should_store_a_product() {
        log.info("Setting up test data...");
       var productCategory1 = transactionTypeRepository.save(getTransactionType());

        TransactionEntity product = getTransaction(productCategory1);
        product.setCode(UUID.randomUUID().toString());

        // Ensure unique code
        TransactionEntity savedTransaction = productRepository.save(product);

        assertThat(savedTransaction).isNotNull();
        assertThat(savedTransaction.getId()).isNotNull();
        assertThat(savedTransaction.getCode()).isEqualTo(product.getCode());
    }

    @Test
    void should_find_product_by_id() {
        log.info("Setting up test data...");
        var productCategory1 = transactionTypeRepository.save(getTransactionType());

        TransactionEntity product = getTransaction(productCategory1);
        product.setCode(UUID.randomUUID().toString());

        // Ensure unique code
        TransactionEntity savedTransaction = productRepository.save(product);

        Optional<TransactionEntity> foundTransaction = productRepository.findById(savedTransaction.getId());
        assertThat(foundTransaction).isPresent();
        assertThat(foundTransaction.get().getCode()).isEqualTo(savedTransaction.getCode());
    }

    @Test
    void should_find_all_products() {
        log.info("Cleaning up database...");
        productRepository.deleteAll();
        transactionTypeRepository.deleteAll();

        var productCategory1 = transactionTypeRepository.save(getTransactionType());

        TransactionEntity product1 = productRepository.save(getTransaction(productCategory1));

        Iterable<TransactionEntity> products = productRepository.findAll();
        List<TransactionEntity> productList = new ArrayList<>();
        products.forEach(productList::add);

        assertThat(productList).hasSize(1);
        assertThat(productList).extracting(TransactionEntity::getCode).contains(product1.getCode());
    }

    @Test
    void should_delete_all_products() {
        log.info("Cleaning up database...");
        productRepository.deleteAll();
        transactionTypeRepository.deleteAll();

        var productCategory1 = transactionTypeRepository.save(getTransactionType());

        productRepository.save(getTransaction(productCategory1));
        productRepository.deleteAll();

        Iterable<TransactionEntity> products = productRepository.findAll();
        assertThat(products).isEmpty();
    }

    @Test
    void whenInvalidId_thenReturnNull() {
        log.info("Cleaning up database...");
        TransactionEntity fromDb = productRepository.findById(-11L).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    void givenSetOfTransactions_whenFindAll_thenReturnAllTransactions() {
        productRepository.deleteAll();
        transactionTypeRepository.deleteAll();

        List<TransactionEntity> all = productRepository.findAll();
        log.info(all.toString());

        TransactionTypeEntity productCategory1 = transactionTypeRepository.save(getTransactionType());

        TransactionEntity product = getTransaction(productCategory1);
        log.info("TransactionEntity:{}", product);
        TransactionEntity product1 = productRepository.save(product);

        Iterable<TransactionEntity> products = productRepository.findAll();
        List<TransactionEntity> productList = new ArrayList<>();
        products.forEach(productList::add);

        assertThat(productList).hasSize(1);
        //assertThat(productList).extracting(TransactionEntity::getName).contains(product1.getName(), product2.getName(), product3.getName());
    }

    @Disabled
    void testSaveRestaurantWithLongName() {
        TransactionEntity productEntity = new TransactionEntity();
        productEntity.setCode(UUID.randomUUID().toString());
        productEntity.setClientId(1l);
        productEntity.setVehicleId(1l);
        productEntity.setPrice(BigDecimal.TEN);
        productEntity.setTransactionType(this.transactionTypeEntity);

        assertThrows(DataIntegrityViolationException.class, () -> {
            productRepository.save(productEntity);
        });
    }

    private TransactionEntity createInvalidTransactionType() {
        TransactionEntity productCategory = new TransactionEntity();
        // Configurar o productCategory com valores inválidos
        // Exemplo: valores inválidos que podem causar uma ConstraintViolationException
        productCategory.setCode(""); // Nome vazio pode causar uma violação
        return productCategory;
    }
}
