package com.antnest.bch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RawResponse<T>{
    private String code;
    private T result;
    private String message;
    private T error;
}
