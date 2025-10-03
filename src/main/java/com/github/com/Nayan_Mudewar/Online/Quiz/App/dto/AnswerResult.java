package com.github.com.Nayan_Mudewar.Online.Quiz.App.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerResult {
    private Long questionId;
    private String question;
    private String userAnswer;
    private String correctAnswer;
    private Boolean isCorrect;
}
