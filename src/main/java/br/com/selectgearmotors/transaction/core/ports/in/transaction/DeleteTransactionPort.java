package br.com.selectgearmotors.transaction.core.ports.in.transaction;

public interface DeleteTransactionPort {
    boolean remove(Long id);
}
