package com.babeh.demo.service;
import com.babeh.demo.model.Transaction;
import com.babeh.demo.model.TransactionItem;
import com.babeh.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MenuService menuService;

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }
    public Transaction save(Transaction transaction) throws Exception {
        // Pastikan transaksi memiliki item terkait
        if (transaction.getItems() == null || transaction.getItems().isEmpty()) {
            throw new Exception("Transaction must have items.");
        }
        
        for (TransactionItem item : transaction.getItems()) {
            int requestedQuantity = item.getQuantity();
            int availableQuantity = menuService.getStock(item.getMenu().getId());
            if (requestedQuantity <= 0 || requestedQuantity > availableQuantity) {
                throw new Exception("Requested quantity for " + item.getMenu().getNamaMenu() + " is not valid or exceeds availability.");
            }
            menuService.updateStock(item.getMenu().getId(), -requestedQuantity);
        }
        return transactionRepository.save(transaction);
    }
    

    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }
}
