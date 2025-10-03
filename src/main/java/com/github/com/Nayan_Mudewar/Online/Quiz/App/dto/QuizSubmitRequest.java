package com.github.com.Nayan_Mudewar.Online.Quiz.App.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmitRequest {

    @NotNull(message = "Quiz ID is required")
    private Long quizId;

    @NotEmpty(message = "Answers cannot be empty")
    private Map<Long, String> answers; // Map of questionId -> selectedAnswer
}
