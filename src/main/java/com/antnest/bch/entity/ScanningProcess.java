package com.antnest.bch.entity;

import com.antnest.bch.constant.Chain;
import com.antnest.bch.constant.Status;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "scanning_process")
public class ScanningProcess {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_at")
    @CreationTimestamp
    private LocalDateTime startAt;

    @Column(name = "end_at")
    @UpdateTimestamp
    private LocalDateTime endAt;

    @Column(name = "chain")
    private String chain;

    @Column(name = "from_block")
    private Long fromBlock;

    @Column(name = "to_block")
    private Long toBlock;

    @Column(name = "status")
    private String status;
}
