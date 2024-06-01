package com.babeh.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaksi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String namaPelanggan;

    @ManyToMany
    @JoinTable(
        name = "transaksi_menu",
        joinColumns = @JoinColumn(name = "transaksi_id"),
        inverseJoinColumns = @JoinColumn(name = "menu_id")
    )
    private List<Menu> items = new ArrayList<>();


    private int kuantitas;

    private double total;

    private String namaPegawai;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
}
