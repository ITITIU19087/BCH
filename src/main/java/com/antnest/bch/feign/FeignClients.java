package com.antnest.bch.feign;

import com.antnest.bch.dto.BlockDetailDto;
import com.antnest.bch.dto.BlockNumberDto;
import com.antnest.bch.dto.RawResponse;

import com.antnest.bch.dto.RequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "test",url = "localhost:5001/bch/v1/block")
public interface FeignClients{

    @RequestMapping(value = "/latest",method = RequestMethod.POST)
    RawResponse<BlockNumberDto> getLatestBlock();

    @RequestMapping(value = "transactions", method = RequestMethod.POST)
    RawResponse<BlockDetailDto> getBlockTransactions(RequestDto hashOrNumber);
}
