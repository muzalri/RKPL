package com.babeh.demo.model;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="menus")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String namaMenu;
    private double harga;
    private String deskripsi;
    private int ketersediaan;

     @ManyToMany(mappedBy = "items")
    private List<Transaksi> transaksi = new ArrayList<>();
   
}