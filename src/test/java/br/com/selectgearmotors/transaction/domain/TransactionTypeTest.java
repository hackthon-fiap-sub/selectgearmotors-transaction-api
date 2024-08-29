package br.com.selectgearmotors.transaction.domain;

import br.com.selectgearmotors.transaction.core.domain.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionTypeTest {

    private TransactionType productCategory;
    private TransactionType anotherTransactionType;

    @BeforeEach
    void setUp() {
        productCategory = TransactionType.builder()
                .id(1L)
                .name("Beverages")
                .build();

        anotherTransactionType = TransactionType.builder()
                .id(2L)
                .name("Snacks")
                .build();
    }

    @Test
    void testGetters() {
        assertEquals(1L, productCategory.getId());
        assertEquals("Beverages", productCategory.getName());
    }

    @Test
    void testSetters() {
        productCategory.setId(2L);
        productCategory.setName("Snacks");

        assertEquals(2L, productCategory.getId());
        assertEquals("Snacks", productCategory.getName());
    }

    @Test
    void testEquals() {
        TransactionType copy = TransactionType.builder()
                .id(1L)
                .name("Beverages")
                .build();

        assertEquals(productCategory, copy);
    }

    @Test
    void testHashCode() {
        TransactionType copy = TransactionType.builder()
                .id(1L)
                .name("Beverages")
                .build();

        assertEquals(productCategory.hashCode(), copy.hashCode());
    }

    @Test
    void testToString() {
        String expected = "TransactionType(id=1, name=Beverages)";
        assertEquals(expected, productCategory.toString());
    }

    @Test
    void testUpdate() {
        productCategory.update(3L, anotherTransactionType);

        assertEquals(3L, productCategory.getId());
        assertEquals("Snacks", productCategory.getName());
    }

    @Test
    void testNoArgsConstructor() {
        TransactionType newTransactionType = new TransactionType();
        assertNotNull(newTransactionType);
    }

    @Test
    void testAllArgsConstructor() {
        TransactionType newTransactionType = new TransactionType(4L, "Frozen Foods");
        assertEquals(4L, newTransactionType.getId());
        assertEquals("Frozen Foods", newTransactionType.getName());
    }
}
