package com.babeh.demo.repository;


import com.babeh.demo.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface MenuRepository extends JpaRepository<Menu,Long> {
    Menu findByNamaMenu(String namaMenu);
}
