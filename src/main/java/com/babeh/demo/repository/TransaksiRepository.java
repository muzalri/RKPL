package com.babeh.demo.repository;

import com.babeh.demo.model.Transaksi;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaksiRepository extends JpaRepository<Transaksi, Long> {
     List<Transaksi> findByCreatedAtBetween(LocalDate startDate, LocalDate endDate);
}
