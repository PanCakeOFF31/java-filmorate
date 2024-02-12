package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    @NonNull
    private String error;
    @NonNull
    private String descriptin;
    private String methodMessage;

}
