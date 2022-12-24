package com.antnest.bch.feign;

import com.antnest.bch.dto.BlockDetailDto;
import com.antnest.bch.dto.BlockNumberDto;
import com.antnest.bch.dto.RawResponse;
import com.antnest.bch.dto.RequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class FeignServiceImpl implements FeignService {

    @Autowired
    FeignClients feignClients;

    @Override
    public BlockNumberDto getLatestBlock() {
        BlockNumberDto response = feignClients.getLatestBlock().getResult();
        return response;
    }

    @Override
    public BlockDetailDto getBlockTransaction(String blockNumber) {
        RequestDto request = this.createRequest(blockNumber);
        return feignClients.getBlockTransactions(request).getResult();
    }

    @Override
    public List<BlockDetailDto> getMultiBlockTransactions(Long fromBlock, Long toBlock) {
        List<BlockDetailDto> list = new LinkedList<>();
        for (Long i = fromBlock; i <= toBlock ; i++) {
            RequestDto request = this.createRequest(String.valueOf(i));
            list.add(feignClients.getBlockTransactions(request).getResult());
        }
        return list;
    }

    public RequestDto createRequest(String blockNumber){
        return RequestDto.builder().hashOrNumber(blockNumber).build();
    }
}
