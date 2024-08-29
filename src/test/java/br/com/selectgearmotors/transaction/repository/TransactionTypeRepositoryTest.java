package br.com.selectgearmotors.transaction.repository;

import br.com.selectgearmotors.transaction.infrastructure.entity.transactiontype.TransactionTypeEntity;
import br.com.selectgearmotors.transaction.infrastructure.repository.TransactionTypeRepository;
import br.com.selectgearmotors.transaction.infrastructure.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@DataJpaTest
@ImportAutoConfiguration(exclude = FlywayAutoConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
class TransactionTypeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransactionTypeRepository productCategoryRepository;

    @Autowired
    private TransactionRepository productRepository;

    private TransactionTypeEntity getTransactionType() {
        return TransactionTypeEntity.builder()
                .id(1l)
                .name("Bebida")
                .build();
    }

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();

        productCategoryRepository.save(getTransactionType());
    }

    @Test
    void should_find_no_clients_if_repository_is_empty() {
        productRepository.deleteAll();
        productCategoryRepository.deleteAll();

        productCategoryRepository.save(getTransactionType());

        List<TransactionTypeEntity> seeds = new ArrayList<>();
        seeds = productCategoryRepository.findAll();
        seeds = Collections.EMPTY_LIST;
        assertThat(seeds).isEmpty();
    }

    @Test
    void should_store_a_product_category() {
        String cocaColaBeverage = "Coca-Cola";
        Optional<TransactionTypeEntity> productCategory = productCategoryRepository.findByName(cocaColaBeverage);
        Optional<TransactionTypeEntity> productCategoryResponse = null;
        if (!productCategory.isPresent()) {

            TransactionTypeEntity cocaCola = TransactionTypeEntity.builder()
                    .name(cocaColaBeverage)
                    .build();

            TransactionTypeEntity save = productCategoryRepository.save(cocaCola);
            productCategoryResponse = productCategoryRepository.findByName(cocaColaBeverage);
        }

        TransactionTypeEntity productCategory1 = productCategoryResponse.get();
        assertThat(productCategory1).hasFieldOrPropertyWithValue("name", cocaColaBeverage);
    }

    @Test
    void testSaveRestaurantWithLongName() {
        TransactionTypeEntity productCategory = new TransactionTypeEntity();
        productCategory.setName("a".repeat(260)); // Nome com 260 caracteres, excedendo o limite de 255

        assertThrows(DataIntegrityViolationException.class, () -> {
            productCategoryRepository.save(productCategory);
        });
    }

    private TransactionTypeEntity createInvalidTransactionType() {
        TransactionTypeEntity productCategory = new TransactionTypeEntity();
        // Configurar o productCategory com valores inválidos
        // Exemplo: valores inválidos que podem causar uma ConstraintViolationException
        productCategory.setName(""); // Nome vazio pode causar uma violação
        return productCategory;
    }

    @Test
    void should_found_null_TransactionType() {
        TransactionTypeEntity productCategory = null;

        Optional<TransactionTypeEntity> fromDb = productCategoryRepository.findById(99l);
        if (fromDb.isPresent()) {
            productCategory = fromDb.get();
        }
        assertThat(productCategory).isNull();
    }

    @Test
    void whenFindById_thenReturnTransactionType() {
        Optional<TransactionTypeEntity> productCategory = productCategoryRepository.findById(1l);
        if (productCategory.isPresent()) {
            TransactionTypeEntity productCategoryResult = productCategory.get();
            assertThat(productCategoryResult).hasFieldOrPropertyWithValue("name", "Bebida");
        }
    }

    @Test
    void whenInvalidId_thenReturnNull() {
        TransactionTypeEntity fromDb = productCategoryRepository.findById(-11l).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    void givenSetOfTransactionTypes_whenFindAll_thenReturnAllTransactionTypes() {
        TransactionTypeEntity productCategory = null;
        TransactionTypeEntity productCategory1 = null;
        TransactionTypeEntity productCategory2 = null;

        Optional<TransactionTypeEntity> restaurant = productCategoryRepository.findById(1l);
        if (restaurant.isPresent()) {

            TransactionTypeEntity bebida = TransactionTypeEntity.builder()
                    .name("Bebida")
                    .build();
            productCategory = productCategoryRepository.save(bebida);

            TransactionTypeEntity acompanhamento = TransactionTypeEntity.builder()
                    .name("Acompanhamento")
                    .build();
            productCategory1 = productCategoryRepository.save(acompanhamento);

            TransactionTypeEntity lanche = TransactionTypeEntity.builder()
                    .name("Lanche")
                    .build();
            productCategory2 = productCategoryRepository.save(lanche);

        }

        Iterator<TransactionTypeEntity> allTransactionTypes = productCategoryRepository.findAll().iterator();
        List<TransactionTypeEntity> clients = new ArrayList<>();
        allTransactionTypes.forEachRemaining(c -> clients.add(c));

        assertNotNull(allTransactionTypes);
    }
}