package com.antnest.bch.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class RequestDto {
    private Long hashOrNumber;
}
