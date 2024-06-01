package com.babeh.demo.service;

import com.babeh.demo.model.Menu;
import com.babeh.demo.model.Transaksi;
import com.babeh.demo.model.User;
import com.babeh.demo.repository.TransaksiRepository;
import com.babeh.demo.repository.MenuRepository;
import com.babeh.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class TransaksiService {
    @Autowired
    private TransaksiRepository transaksiRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private UserRepository userRepository;

     public Transaksi saveTransaksi(Transaksi transaksi, String username, List<Long> menuIds, List<Integer> quantities) {
        User user = userRepository.findByUsername(username);
        transaksi.setNamaPegawai(user.getUsername());

        // Calculate total and update menu ketersediaan
        double total = 0;
        List<Menu> items = new ArrayList<>();
        for (int i = 0; i < menuIds.size(); i++) {
            Long menuId = menuIds.get(i);
            int quantity = quantities.get(i);

            Menu menu = menuRepository.findById(menuId).orElseThrow();
            menu.setKetersediaan(menu.getKetersediaan() - quantity);
            total += menu.getHarga() * quantity;
            menuRepository.save(menu);

            items.add(menu);
        }
        transaksi.setItems(items);
        transaksi.setKuantitas(quantities.stream().mapToInt(Integer::intValue).sum());
        transaksi.setTotal(total);

        return transaksiRepository.save(transaksi);
    }

    
    public List<Transaksi> getAllTransaksi() {
        return transaksiRepository.findAll();
    }

    // Other methods...

    public void deleteTransaction(Long id) {
        transaksiRepository.deleteById(id);
    }
}
