package com.babeh.demo.service;

import com.babeh.demo.model.Menu;
import com.babeh.demo.model.Transaksi;
import com.babeh.demo.repository.TransaksiRepository;
import com.babeh.demo.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class TransaksiService {
    @Autowired
    private TransaksiRepository transaksiRepository;

    @Autowired
    private MenuRepository menuRepository;

    // @Autowired
    // private UserRepository userRepository;

    public Transaksi saveTransaksi(Transaksi transaksi) {
        // User user = userRepository.findByUsername(username);
        // transaksi.setNamaPegawai(user.getUsername());

        // Calculate total and update menu ketersediaan
        double total = 0;
        for (Menu item : transaksi.getItems()) {
            Menu menu = menuRepository.findById(item.getId()).orElseThrow();
            menu.setKetersediaan(menu.getKetersediaan() - transaksi.getKuantitas());
            total += menu.getHarga() * transaksi.getKuantitas();
            menuRepository.save(menu);
        }
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
