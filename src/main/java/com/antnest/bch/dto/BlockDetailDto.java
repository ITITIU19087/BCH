package com.antnest.bch.dto;

import com.antnest.bch.dto.raw.Transactions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockDetailDto {
    private List<Transactions> transactions;

}
