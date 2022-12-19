package com.antnest.bch.controller;

import com.antnest.bch.dto.BlockDetailDto;
import com.antnest.bch.dto.BlockNumberDto;
import com.antnest.bch.service.BlockService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BlockController {

    @Autowired
    private BlockService blockService;

    @PostMapping ("/transactions")
    public ResponseEntity<BlockDetailDto> getBlockTransactions(@RequestParam Long hashOrNumber){
        return new ResponseEntity<>(blockService.getBlockTransactions(hashOrNumber),HttpStatus.OK);
    }

    @GetMapping("/latest")
    public ResponseEntity<BlockNumberDto> getLatestBlockHeight(){
        return new ResponseEntity<>(blockService.getLatestBlockHeight(),HttpStatus.OK);
    }

    @PostMapping("/transaction/list")
    public List<BlockDetailDto> getMultiBlockTransactions(@RequestParam Long start,Long end) throws JsonProcessingException {
        return blockService.getMultiBlockTransactions(start, end);
    }
}
