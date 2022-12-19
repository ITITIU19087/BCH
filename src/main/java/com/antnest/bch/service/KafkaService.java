package com.antnest.bch.service;

import com.antnest.bch.dto.raw.Transactions;
import com.antnest.bch.entity.Address;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class KafkaService {
    private final Logger logger
            = LoggerFactory.getLogger(KafkaService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static ObjectMapper objectMapper;

    @KafkaListener(topics = "${general.topic.name}",
            groupId = "${general.topic.group.id}")
    public void consume(String message) {
        logger.info(String.format("Message recieved -> %s", message));
    }

    public void sendMessage(Transactions transactions, String topic) throws JsonProcessingException {
        String txId = transactions.getTxid();
        ListenableFuture<SendResult<String, String>> future
                = this.kafkaTemplate.send(topic, txId);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                logger.info("Sent message: " + txId
                        + " with offset: " + result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                logger.error("Unable to send message : " + transactions, ex);
            }
        });
    }
}
