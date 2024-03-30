package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.MethodNotImplemented;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService service;

    // TODO: Функциональность «Отзывы». 4 SP. Реализовать функциональность.
    @PostMapping
    public Review createReview(@Valid @RequestBody final Review review) {
        log.debug("/reviews - POST: createReview()");
        return service.createReview(review);
    }

    // TODO: Функциональность «Отзывы». 4 SP. Реализовать функциональность.
    @PutMapping
    public Review updateReview(@Valid @RequestBody final Review review) {
        log.debug("/reviews - PUT: updateReview()");
        return service.updateReview(review);
    }

    // TODO: Функциональность «Отзывы». 4 SP. Реализовать функциональность.
    @DeleteMapping("/{id}")
    public Review deleteReviewById(@PathVariable(name = "id") final int reviewId) {
        log.debug("/reviews/{} - DELETE: deleteReviewById()", reviewId);
        return service.deleteReviewById(reviewId);
    }

    // TODO: Функциональность «Отзывы». 4 SP. Реализовать функциональность.
    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable(name = "id") final int reviewId) {
        log.debug("/reviews/{} - GET: getReviewById()", reviewId);
        return service.getReviewById(reviewId);
    }

    // TODO: Функциональность «Отзывы». 4 SP. Реализовать функциональность.
    @GetMapping
    public List<Review> getAllReviewsByFilmId(@RequestParam final int filmId,
                                              @RequestParam(defaultValue = "10") final int count) {
        log.debug("/reviews?filmId={}&count={} - GET: getAllReviewsByFilmId()", filmId, count);
        return service.getAllReviewsByFilmId(filmId, count);
    }

    // TODO: Функциональность «Отзывы». 4 SP. Реализовать функциональность.
    @PutMapping("/{id}/like/{userId}")
    public Review likeReview(@PathVariable(name = "id") final int reviewId,
                             @PathVariable final int userId) {
        log.debug("/reviews/{}/like/{} - PUT: likeReview()", reviewId, userId);
        return service.likeReview(reviewId, userId);
    }

    // TODO: Функциональность «Отзывы». 4 SP. Реализовать функциональность.
    @PutMapping("/{id}/dislike/{userId}")
    public Review dislikeReview(@PathVariable(name = "id") final int reviewId,
                                @PathVariable final int userId) {
        log.debug("/reviews/{}/like/{} - PUT: dislikeReview()", reviewId, userId);
        return service.dislikeReview(reviewId, userId);
    }

    // TODO: Функциональность «Отзывы». 4 SP. Реализовать функциональность.
    @DeleteMapping("/{id}/like/{userId}")
    public Review undoLikeReview(@PathVariable(name = "id") final int reviewId,
                                 @PathVariable final int userId) {
        log.debug("/reviews/{}/like/{} - PUT: undoLikeReview()", reviewId, userId);
        return service.undoLikeReview(reviewId, userId);
    }

    // TODO: Функциональность «Отзывы». 4 SP. Реализовать функциональность.
    @DeleteMapping("/{id}/dislike/{userId}")
    public Review undoDislikeReview(@PathVariable(name = "id") final int reviewId,
                                    @PathVariable final int userId) {
        log.debug("/reviews/{}/like/{} - PUT: undoDislikeReview()", reviewId, userId);
        return service.undoDislikeReview(reviewId, userId);
    }
}