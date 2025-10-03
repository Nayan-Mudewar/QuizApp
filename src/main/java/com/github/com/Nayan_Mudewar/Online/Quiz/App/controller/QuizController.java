package com.github.com.Nayan_Mudewar.Online.Quiz.App.controller;

import com.github.com.Nayan_Mudewar.Online.Quiz.App.dto.QuizStartResponse;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.Quiz;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

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
}
