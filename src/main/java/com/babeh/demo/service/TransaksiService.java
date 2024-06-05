package com.babeh.demo.service;

import com.babeh.demo.model.Menu;
import com.babeh.demo.model.Transaksi;
import com.babeh.demo.model.User;
import com.babeh.demo.repository.TransaksiRepository;
import com.babeh.demo.repository.MenuRepository;
import com.babeh.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public Transaksi findById(Long id) {
        return transaksiRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid transaksi Id:" + id));
    }

    public Transaksi updateTransaksi(Long id, Transaksi transaksi, String username, List<Long> menuIds, List<Integer> quantities) {
        Transaksi existingTransaksi = findById(id);

        // Revert the stock of the previous items
        for (Menu menu : existingTransaksi.getItems()) {
            menu.setKetersediaan(menu.getKetersediaan() + existingTransaksi.getKuantitas());
            menuRepository.save(menu);
        }

        existingTransaksi.setNamaPelanggan(transaksi.getNamaPelanggan());
        existingTransaksi.setNamaPegawai(username);
        existingTransaksi.setItems(new ArrayList<>());
        existingTransaksi.setKuantitas(0);
        existingTransaksi.setTotal(0);

        return saveTransaksi(existingTransaksi, username, menuIds, quantities);
    }

    public void deleteTransaksi(Long id) {
        Transaksi transaksi = findById(id);

        // Revert the stock of the items
        for (Menu menu : transaksi.getItems()) {
            menu.setKetersediaan(menu.getKetersediaan() + transaksi.getKuantitas());
            menuRepository.save(menu);
        }

        transaksiRepository.delete(transaksi);
    }

    public List<Transaksi> getFilteredTransaksi(String startDate, String endDate) {
    if (startDate == null || endDate == null) {
        return transaksiRepository.findAll();
    }

    LocalDate start = LocalDate.parse(startDate);
    LocalDate end = LocalDate.parse(endDate);
    return transaksiRepository.findByCreatedAtBetween(start, end);
}

}
