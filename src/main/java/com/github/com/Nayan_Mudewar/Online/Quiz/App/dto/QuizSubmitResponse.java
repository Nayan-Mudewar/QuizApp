package com.github.com.Nayan_Mudewar.Online.Quiz.App.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizSubmitResponse {
    private Long attemptId;
    private Long quizId;
    private String quizTitle;
    private Integer score;
    private Integer totalQuestions;
    private Double percentage;
    private LocalDateTime completedAt;
    private List<AnswerResult> results;
}