package com.antnest.bch.service;

import com.antnest.bch.constant.Chain;
import com.antnest.bch.constant.Status;
import com.antnest.bch.constant.Type;
import com.antnest.bch.dto.BlockDetailDto;
import com.antnest.bch.dto.BlockNumberDto;
import com.antnest.bch.dto.RawResponse;
import com.antnest.bch.dto.RequestDto;
import com.antnest.bch.dto.raw.Transactions;
import com.antnest.bch.entity.ScanningProcess;
import com.antnest.bch.exeception.UndefinedBlockException;
import com.antnest.bch.repository.ScanningProcessRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;


@Service
@Slf4j
public class BlockService {
    @Autowired
    private RestTemplateService<BlockDetailDto> blockDetailService;

    @Autowired
    private RestTemplateService<BlockNumberDto> blockNumberService;

    @Autowired
    private ScanningProcessRepo scanningProcessRepo;

    @Autowired
    private RedisService redisService;

    @Autowired
    private KafkaService kafkaService;


    public BlockDetailDto getBlockTransactions(Long hashOrNumber) {
        RequestDto request = this.createRequest(hashOrNumber);
        RawResponse<BlockDetailDto> response = blockDetailService.callExchange(new ParameterizedTypeReference<>(){},request);
        return response.getResult();
    }

    public BlockNumberDto getLatestBlockHeight(){
        RawResponse<BlockNumberDto> response = blockNumberService.callExchangeNumber(new ParameterizedTypeReference<>(){});
        return response.getResult();
    }

    private RequestDto createRequest(Long hashOrNumber) {
        return RequestDto.builder()
                .hashOrNumber(hashOrNumber)
                .build();
    }
    public List<BlockDetailDto> getMultiBlockTransactions(Long fromBlock, Long toBlock) throws JsonProcessingException {
        List<BlockDetailDto> list = new LinkedList<>();
        if(!isScanningProcess()){
            Long latestBlock = this.getLatestBlockHeight().getBlockNumber();
            Long processInsertId = insertScanningProcess(fromBlock, toBlock);
            if (fromBlock > toBlock){
                throw new UndefinedBlockException("Undefined Block");
            }
            else if(fromBlock > latestBlock && toBlock > latestBlock){
                log.info("Latest Block transactions: " + latestBlock);
                RequestDto request = this.createRequest(latestBlock);
                RawResponse<BlockDetailDto> response = blockDetailService.callExchange(new ParameterizedTypeReference<>(){},request);
                if(!checkAddressValue(response.getResult())){
                    list.add(response.getResult());
                    this.sendMessage(response.getResult());
                }
            }
            else if(toBlock > latestBlock){
                log.info("From block " + fromBlock + " to latest block " + latestBlock);
                for (Long i = fromBlock; i <= latestBlock; i++) {
                    RequestDto request = this.createRequest(i);
                    RawResponse<BlockDetailDto> response = blockDetailService.callExchange(new ParameterizedTypeReference<>(){},request);
                    if(!checkAddressValue(response.getResult())){
                        list.add(response.getResult());
                        this.sendMessage(response.getResult());
                    }
                }
            }
            else{
                for (Long i = fromBlock; i <= toBlock; i++) {
                    RequestDto request = this.createRequest(i);
                    RawResponse<BlockDetailDto> response = blockDetailService.callExchange(new ParameterizedTypeReference<>(){},request);
                    if(!checkAddressValue(response.getResult())){
                        list.add(response.getResult());
                        this.sendMessage(response.getResult());
                    }

                }
            }
            updateProcessEvent(processInsertId);
        }
        return list;
    }

    private long insertScanningProcess(Long fromBlock, Long toBlock) {
        ScanningProcess scanningProcess = new ScanningProcess();
        scanningProcess.setFromBlock(fromBlock);
        scanningProcess.setChain(String.valueOf(Chain.BCH));
        scanningProcess.setStatus(String.valueOf(Status.IN_PROGRESS));
        scanningProcess.setToBlock(toBlock);
        scanningProcessRepo.save(scanningProcess);
        return scanningProcess.getId();
    }

    private boolean updateProcessEvent(Long processId) {
        ScanningProcess scanningProcess = scanningProcessRepo.getReferenceById(processId);
        scanningProcess.setStatus(String.valueOf(Status.COMPLETE));
        scanningProcessRepo.save(scanningProcess);
        return true;
    }

    private boolean isScanningProcess() {
        boolean isScanning = scanningProcessRepo.existsByStatus(String.valueOf(Status.IN_PROGRESS));
        return isScanning;
    }

    private Boolean checkAddressValue(BlockDetailDto blockDetailDto){
        boolean isValid = true;
        List<Transactions> list = blockDetailDto.getTransactions();
        for (int i = 0; i < list.size(); i++) {
            if(redisService.checkKey(list.get(i).getTxid()) == null){
                isValid = false;
            }
        }
        return isValid;
    }

    private void sendMessage(BlockDetailDto blockDetailDto){
        List<Transactions> list = blockDetailDto.getTransactions();
        list.forEach((element)->{
            try {
                kafkaService.sendMessage(element,String.valueOf(Type.DEPOSIT));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

    }
}
