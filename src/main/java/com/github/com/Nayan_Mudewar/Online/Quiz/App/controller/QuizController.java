package com.github.com.Nayan_Mudewar.Online.Quiz.App.controller;

import com.github.com.Nayan_Mudewar.Online.Quiz.App.dto.QuizStartResponse;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.dto.QuizSubmitRequest;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.dto.QuizSubmitResponse;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.Quiz;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.service.AttemptService;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.service.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;
    private final AttemptService attemptService;

    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        List<Quiz> quizzes = quizService.getAllQuizzes();
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        Quiz quiz = quizService.getQuizById(id);
        return ResponseEntity.ok(quiz);
    }

    @GetMapping("/{id}/start")
    public ResponseEntity<QuizStartResponse> startQuiz(@PathVariable Long id) {
        QuizStartResponse response = quizService.startQuiz(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/submit")
    public ResponseEntity<QuizSubmitResponse> submitQuiz(@Valid @RequestBody QuizSubmitRequest request) {
        QuizSubmitResponse response = attemptService.submitQuiz(request);
        return ResponseEntity.ok(response);
    }
}
