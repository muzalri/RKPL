package com.babeh.demo.service;

import com.babeh.demo.model.Menu;
import com.babeh.demo.model.Transaction;
import com.babeh.demo.repository.TransactionRepository;
import com.babeh.demo.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;


    @Autowired
    private MenuRepository menuRepository;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public Transaction saveTransaction(Transaction transaction) {
        double totalHarga = transaction.getItems().stream()
                .mapToDouble(menu -> menu.getHarga() * transaction.getKuantitas())
                .sum();
        transaction.setTotal(totalHarga);
        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
