package br.com.selectgearmotors.transaction.entity;

import br.com.selectgearmotors.transaction.core.domain.TransactionType;
import br.com.selectgearmotors.transaction.infrastructure.entity.transactiontype.TransactionTypeEntity;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionTypeEntityTest {

    @Disabled
    void testUpdate() {
        // Arrange
        Long id = 1L;
        TransactionType productCategory = new TransactionType();
        productCategory.setName("Updated Name");

        TransactionTypeEntity productCategoryEntity = new TransactionTypeEntity();
        productCategoryEntity.setId(2L);
        productCategoryEntity.setName("Old Name");

        // Act
        productCategoryEntity.update(id, productCategoryEntity);

        // Assert
        assertEquals(id, productCategoryEntity.getId());
        assertEquals("Updated Name", productCategoryEntity.getName());
    }

    @Test
    void testGettersAndSetters() {
        TransactionType productCategory = new TransactionType();
        productCategory.setId(1L);
        productCategory.setName("Updated Name");

        assertThat(productCategory.getId()).isEqualTo(1L);
        assertThat(productCategory.getName()).isEqualTo("Updated Name");
    }

    @Test
    void testToString() {
        TransactionType productCategory = TransactionType.builder()
                .id(1L)
                .name("Updated Name")
                .build();

        String expected = "TransactionType(id=1, name=Updated Name)";
        assertEquals(productCategory.toString(), expected);
    }
}
