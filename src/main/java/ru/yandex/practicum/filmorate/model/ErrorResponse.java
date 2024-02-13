package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class ErrorResponse {
    @NonNull
    private String error;
    @NonNull
    private String description;
    private String methodMessage = "";
}