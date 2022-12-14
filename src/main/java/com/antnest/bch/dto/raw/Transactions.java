package com.antnest.bch.dto.raw;

import lombok.Data;

import java.util.List;

@Data
public class Transactions {
    private String txid;
    private List<String> to;
}
