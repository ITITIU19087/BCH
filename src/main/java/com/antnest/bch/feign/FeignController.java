package com.antnest.bch.feign;


import com.antnest.bch.dto.BlockDetailDto;
import com.antnest.bch.dto.BlockNumberDto;
import com.antnest.bch.dto.RequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class FeignController {

    @Autowired
    FeignService feignService;

    @PostMapping("feign/latest")
    public BlockNumberDto getLatestBlock(){
        return feignService.getLatestBlock();
    }

    @PostMapping ("feign/transactions")
    public BlockDetailDto getBlockTransactions(@RequestParam String blockNumber ){
        return feignService.getBlockTransaction(blockNumber);
    }

    @PostMapping("feign/list")
    public List<BlockDetailDto> getBlockTransactions(@RequestParam Long fromBlock, Long toBlock){
        return feignService.getMultiBlockTransactions(fromBlock,toBlock);
    }
}
