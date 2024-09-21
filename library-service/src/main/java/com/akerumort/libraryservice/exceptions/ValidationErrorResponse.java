package com.akerumort.libraryservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class ValidationErrorResponse { // содержит детали ошибок для отправки кратких сообщений (форматирования)
    private Map<String, String> errors;
}
