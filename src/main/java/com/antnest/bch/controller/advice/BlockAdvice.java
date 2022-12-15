package com.antnest.bch.controller.advice;

import com.antnest.bch.dto.ErrorResponseDto;
import com.antnest.bch.exeception.UndefinedBlockException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BlockAdvice {
    @ExceptionHandler(UndefinedBlockException.class)
    private ResponseEntity<ErrorResponseDto> undefinedBlock(UndefinedBlockException exception){
        return badRequest(exception);
    }

    private ResponseEntity<ErrorResponseDto> badRequest(Throwable throwable){
        ErrorResponseDto errorResponseDTO = ErrorResponseDto.builder().message(throwable.getMessage()).build();
        return ResponseEntity.badRequest().body(errorResponseDTO);
    }
}
