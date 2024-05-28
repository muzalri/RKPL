package com.babeh.demo.controller;

import com.babeh.demo.model.Menu;
import com.babeh.demo.model.Transaction;
import com.babeh.demo.model.TransactionItem;
import com.babeh.demo.model.User;
import com.babeh.demo.service.MenuService;
import com.babeh.demo.service.TransactionService;
import com.babeh.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String getAllTransactions(Model model) {
        List<Transaction> transactions = transactionService.findAll();
        model.addAttribute("transactions", transactions);
        return "transaction-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("menus", menuService.semuaMenu());
        model.addAttribute("users", userService.findAllUsers());
        return "transaction-form";
    }

    @PostMapping("/new/save")
    public String saveTransaction(@ModelAttribute Transaction transaction, Model model) {
        try {
            // Pastikan transaksi memiliki item terkait
            if (transaction.getItems() == null || transaction.getItems().isEmpty()) {
                throw new Exception("Transaction must have items.");
            }
            
            transactionService.save(transaction);
            return "redirect:/transactions";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("transaction", transaction);
            model.addAttribute("menus", menuService.semuaMenu());
            model.addAttribute("users", userService.findAllUsers());
            return "transaction-form";
        }
    }
    

    @GetMapping("/{id}")
    public String getTransactionById(@PathVariable Long id, Model model) {
        Optional<Transaction> transaction = transactionService.findById(id);
        if (transaction.isPresent()) {
            model.addAttribute("transaction", transaction.get());
            return "transaction-detail";
        } else {
            return "redirect:/transactions";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Transaction> transaction = transactionService.findById(id);
        if (transaction.isPresent()) {
            model.addAttribute("transaction", transaction.get());
            model.addAttribute("menus", menuService.semuaMenu());
            model.addAttribute("users", userService.findAllUsers());
            return "transaction-form";
        } else {
            return "redirect:/transactions";
        }
    }

    // @PostMapping("/{id}/update")
    // public String updateTransaction(@PathVariable Long id, @ModelAttribute("transaction") Transaction transaction, BindingResult result, Model model) {
    //     boolean hasError = false;
    //     for (TransactionItem item : transaction.getItems()) {
    //         Menu menu = menuService.dapatkanMenu(item.getMenu().getId());
    //         if (menu.getKetersediaan() < item.getQuantity()) {
    //             result.rejectValue("items", "error.transaction", "Insufficient stock for " + menu.getNamaMenu());
    //             hasError = true;
    //         }
    //     }

    //     if (hasError) {
    //         model.addAttribute("menus", menuService.semuaMenu());
    //         model.addAttribute("users", userService.findAllUsers());
    //         return "transaction-form";
    //     }

    //     transaction.setId(id);
    //     transactionService.save(transaction);
    //     return "redirect:/transactions";
    // }

    @GetMapping("/{id}/delete")
    public String deleteTransaction(@PathVariable Long id) {
        transactionService.deleteById(id);
        return "redirect:/transactions";
    }
}
