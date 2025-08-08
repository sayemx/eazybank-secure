package com.sayem.eazybank.dto;

import org.springframework.http.HttpStatus;

public record LoginResponseDto(HttpStatus status, String jwtToke) {

}
