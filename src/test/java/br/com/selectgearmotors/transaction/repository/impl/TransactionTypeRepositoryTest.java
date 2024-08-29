package br.com.selectgearmotors.transaction.repository.impl;

import br.com.selectgearmotors.transaction.infrastructure.entity.transactiontype.TransactionTypeEntity;
import br.com.selectgearmotors.transaction.infrastructure.repository.TransactionTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class TransactionTypeRepositoryTest {

    @Autowired
    private TransactionTypeRepository productCategoryRepository;

    private TransactionTypeEntity electronics;
    private TransactionTypeEntity books;

    @BeforeEach
    void setUp() {
        electronics = new TransactionTypeEntity();
        electronics.setName("Electronics");

        books = new TransactionTypeEntity();
        books.setName("Books");

        productCategoryRepository.save(electronics);
        productCategoryRepository.save(books);
    }

    @Test
    void testFindByName_found() {
        Optional<TransactionTypeEntity> found = productCategoryRepository.findByName("Electronics");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Electronics");
    }

    @Test
    void testFindByName_notFound() {
        Optional<TransactionTypeEntity> found = productCategoryRepository.findByName("NonExistingCategory");
        assertThat(found).isNotPresent();
    }

    @Test
    void testFindByName_caseInsensitive() {
        Optional<TransactionTypeEntity> found = productCategoryRepository.findByName("electronics");
        assertThat(found).isNotPresent();
    }
}
