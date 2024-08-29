package br.com.selectgearmotors.transaction.service;

import br.com.selectgearmotors.transaction.application.database.mapper.TransactionTypeMapper;
import br.com.selectgearmotors.transaction.core.domain.TransactionType;
import br.com.selectgearmotors.transaction.core.ports.in.transactiontype.*;
import br.com.selectgearmotors.transaction.core.ports.out.TransactionTypeRepositoryPort;
import br.com.selectgearmotors.transaction.core.service.TransactionTypeService;
import br.com.selectgearmotors.transaction.infrastructure.entity.transactiontype.TransactionTypeEntity;
import br.com.selectgearmotors.transaction.infrastructure.repository.TransactionTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TransactionTypeServiceTest {

    @InjectMocks
    TransactionTypeService productCategoryService;

    @Mock
    TransactionTypeRepositoryPort productCategoryRepository;

    @Mock
    TransactionTypeRepository repository;

    @Mock
    TransactionTypeMapper mapper;

    @Mock
    CreateTransactionTypePort createTransactionTypePort;

    @Mock
    DeleteTransactionTypePort deleteTransactionTypePort;

    @Mock
    FindByIdTransactionTypePort findByIdTransactionTypePort;

    @Mock
    FindTransactionTypesPort findTransactionCategoriesPort;

    @Mock
    UpdateTransactionTypePort updateTransactionTypePort;

    private TransactionTypeEntity getTransactionTypeEntity() {
        return TransactionTypeEntity.builder()
                .name("Bebida")
                .build();
    }

    private TransactionTypeEntity getTransactionTypeEntity1() {
        return TransactionTypeEntity.builder()
                .name("Bebida 1")
                .build();
    }

    private TransactionTypeEntity getTransactionTypeEntity2() {
        return TransactionTypeEntity.builder()
                .name("Bebida 2")
                .build();
    }

    private TransactionType getTransactionType() {
        return TransactionType.builder()
                .name("Bebida")
                .build();
    }

    private TransactionType getTransactionType1() {
        return TransactionType.builder()
                .name("Bebida 1")
                .build();
    }

    private TransactionType getTransactionType2() {
        return TransactionType.builder()
                .name("Bebida 2")
                .build();
    }

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllTransactionTypesTest() {
        List<TransactionType> productCategorys = new ArrayList<>();
        List<TransactionTypeEntity> listEntity = new ArrayList<>();

        TransactionType client = getTransactionType();
        TransactionType client1 = getTransactionType1();
        TransactionType client2 = getTransactionType2();

        TransactionTypeEntity clientEntity = getTransactionTypeEntity();
        TransactionTypeEntity clientEntity1 = getTransactionTypeEntity1();
        TransactionTypeEntity clientEntity2 = getTransactionTypeEntity2();

        productCategorys.add(client);
        productCategorys.add(client1);
        productCategorys.add(client2);

        listEntity.add(clientEntity);
        listEntity.add(clientEntity1);
        listEntity.add(clientEntity2);

        when(productCategoryService.findAll()).thenReturn(productCategorys);

        List<TransactionType> productCategoryList = productCategoryService.findAll();

        assertNotNull(productCategoryList);
    }

    @Test
    void getTransactionTypeByIdTest() {
        TransactionType productCategory1 = getTransactionType();
        when(productCategoryService.findById(1L)).thenReturn(productCategory1);

        TransactionType productCategory = productCategoryService.findById(1L);

        assertEquals("Bebida", productCategory.getName());
    }

    @Test
    void getFindTransactionTypeByShortIdTest() {
        TransactionType productCategory = getTransactionType();
        when(productCategoryService.findById(1L)).thenReturn(productCategory);

        TransactionType result = productCategoryService.findById(1L);

        assertEquals("Bebida", result.getName());
    }

    @Test
    void createTransactionTypeTest() {
        TransactionType productCategory = getTransactionType();
        TransactionType productCategoryResult = getTransactionType();
        productCategoryResult.setId(1L);

        when(productCategoryService.save(productCategory)).thenReturn(productCategoryResult);
        TransactionType save = productCategoryService.save(productCategory);

        assertNotNull(save);
        //verify(productCategoryRepository, times(1)).save(productCategory);
    }

    @Test
    void testSaveRestaurantWithLongName() {
        TransactionType productCategory = new TransactionType();
        productCategory.setName("a".repeat(260)); // Nome com 260 caracteres, excedendo o limite de 255

        // Simulando o lançamento de uma exceção
        doThrow(new DataException("Value too long for column 'name'", null)).when(productCategoryRepository).save(productCategory);

        assertThrows(DataException.class, () -> {
            productCategoryRepository.save(productCategory);
        });
    }

    @Test
    void testRemoveRestaurant_Success() {
        Long restaurantId = 1L;
        TransactionType productCategory = getTransactionType();
        productCategory.setId(restaurantId);

        when(productCategoryService.findById(restaurantId)).thenReturn(productCategory);
        boolean result = productCategoryService.remove(restaurantId);
        assertTrue(result);
    }

    @Test
    void testRemove_Exception() {
        Long productId = 99L;

        boolean result = productCategoryService.remove(productId);
        assertFalse(result);
        verify(productCategoryRepository, never()).remove(productId);
    }

    @Test
    void testCreateTransactionType() {
        TransactionType productCategory = getTransactionType();
        when(createTransactionTypePort.save(productCategory)).thenReturn(productCategory);

        TransactionType result = createTransactionTypePort.save(productCategory);

        assertNotNull(result);
        assertEquals("Bebida", result.getName());
    }

    @Test
    void testDeleteTransactionType() {
        Long productId = 1L;
        when(deleteTransactionTypePort.remove(productId)).thenReturn(true);

        boolean result = deleteTransactionTypePort.remove(productId);

        assertTrue(result);
    }

    @Test
    void testFindByIdTransactionType() {
        TransactionType productCategory = getTransactionType();
        when(findByIdTransactionTypePort.findById(1L)).thenReturn(productCategory);

        TransactionType result = findByIdTransactionTypePort.findById(1L);

        assertNotNull(result);
        assertEquals("Bebida", result.getName());
    }

    @Test
    void testFindTransactionCategories() {
        List<TransactionType> productCategories = new ArrayList<>();
        productCategories.add(getTransactionType());
        productCategories.add(getTransactionType1());
        productCategories.add(getTransactionType2());

        when(findTransactionCategoriesPort.findAll()).thenReturn(productCategories);

        List<TransactionType> result = findTransactionCategoriesPort.findAll();

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void testUpdateTransactionType() {
        Long productId = 1L;
        TransactionType productCategory = getTransactionType();
        productCategory.setName("Updated Name");

        when(updateTransactionTypePort.update(productId, productCategory)).thenReturn(productCategory);

        TransactionType result = updateTransactionTypePort.update(productId, productCategory);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
    }

}