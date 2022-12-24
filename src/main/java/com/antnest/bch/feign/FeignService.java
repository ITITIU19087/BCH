package com.antnest.bch.feign;

import com.antnest.bch.dto.BlockDetailDto;
import com.antnest.bch.dto.BlockNumberDto;
import com.antnest.bch.dto.RawResponse;
import com.antnest.bch.dto.RequestDto;

import java.util.List;

public interface FeignService {
    BlockNumberDto getLatestBlock();
    BlockDetailDto getBlockTransaction(String hashOrNumber);
    List<BlockDetailDto> getMultiBlockTransactions(Long fromBlock, Long toBlock);
}
