package com.antnest.bch.service;

import com.antnest.bch.dto.BlockDetailDto;
import com.antnest.bch.dto.BlockNumberDto;
import com.antnest.bch.dto.RawResponse;
import com.antnest.bch.dto.RequestDto;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;



@Service
public class BlockService {
    @Autowired
    private RestTemplateService<BlockDetailDto> blockDetailService;

    @Autowired
    private RestTemplateService<BlockNumberDto> blockNumberService;


    public BlockDetailDto getBlockTransactions(String hashOrNumber) {
        RequestDto request = this.createRequest(hashOrNumber);
        RawResponse<BlockDetailDto> response = blockDetailService.callExchange(new ParameterizedTypeReference<>(){},request);
        return response.getResult();
    }

    public BlockNumberDto getLatestBlockHeight(){
        RawResponse<BlockNumberDto> response = blockNumberService.callExchangeNumber(new ParameterizedTypeReference<>(){});
        return response.getResult();
    }

    private RequestDto createRequest(String hashOrNumber) {
        return RequestDto.builder()
                .hashOrNumber(hashOrNumber)
                .build();
    }
}
