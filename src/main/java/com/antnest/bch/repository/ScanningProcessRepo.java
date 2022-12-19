package com.antnest.bch.repository;

import com.antnest.bch.entity.ScanningProcess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanningProcessRepo extends JpaRepository<ScanningProcess, Long> {
    boolean existsByStatus(String process);
}
