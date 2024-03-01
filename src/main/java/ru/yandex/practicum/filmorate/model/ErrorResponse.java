package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class ErrorResponse {
    @NonNull
    private String error;
    @NonNull
    private String description;
    private String methodMessage;
}