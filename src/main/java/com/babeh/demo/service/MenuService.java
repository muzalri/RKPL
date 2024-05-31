package com.babeh.demo.service;

import com.babeh.demo.model.Menu;
import com.babeh.demo.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    public List<Menu> semuaMenu() {
        return menuRepository.findAll();
    }

    public Menu tambahMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    public void hapusMenu(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("The given id must not be null.");
        }
        menuRepository.deleteById(id);
    }

    public Menu dapatkanMenu(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("The given id must not be null.");
        }
        return menuRepository.findById(id).orElse(null);
    }

    public Menu simpanMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    public void updateMenu(Menu newMenu) {
        if (newMenu.getId() == null) {
            throw new IllegalArgumentException("The given id must not be null.");
        }
        Menu existingMenu = menuRepository.findById(newMenu.getId()).orElse(null);
        if (existingMenu != null) {
            existingMenu.setNamaMenu(newMenu.getNamaMenu());
            existingMenu.setHarga(newMenu.getHarga());
            existingMenu.setDeskripsi(newMenu.getDeskripsi());
            existingMenu.setKetersediaan(newMenu.getKetersediaan());
            menuRepository.save(existingMenu);
        }
    }

    public void updateStock(Long id, int quantity) {
        if (id == null) {
            throw new IllegalArgumentException("The given id must not be null.");
        }
        Menu menu = dapatkanMenu(id);
        if (menu != null) {
            menu.setKetersediaan(menu.getKetersediaan() + quantity);
            menuRepository.save(menu);
        }
    }

    public int getStock(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("The given id must not be null.");
        }
        Menu menu = dapatkanMenu(id);
        return (menu != null) ? menu.getKetersediaan() : 0;
    }
}
