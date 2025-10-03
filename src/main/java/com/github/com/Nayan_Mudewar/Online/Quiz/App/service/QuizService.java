package com.github.com.Nayan_Mudewar.Online.Quiz.App.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.dto.QuestionDto;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.dto.QuizStartResponse;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.Question;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.Quiz;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.exception.BadRequestException;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.exception.ResourceNotFoundException;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.repository.QuestionRepository;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public QuizStartResponse startQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + quizId));

        List<Question> randomQuestions = questionRepository.findRandomQuestionsByQuizId(quizId);

        if (randomQuestions.isEmpty()) {
            throw new BadRequestException("No questions available for this quiz");
        }

        if (randomQuestions.size() < 10) {
            throw new BadRequestException("Quiz must have at least 10 questions. Current: " + randomQuestions.size());
        }

        List<QuestionDto> questionDtos = randomQuestions.stream()
                .map(this::convertToQuestionDto)
                .collect(Collectors.toList());

        return QuizStartResponse.builder()
                .quizId(quiz.getId())
                .title(quiz.getTitle())
                .category(quiz.getCategory())
                .questions(questionDtos)
                .build();
    }

    private QuestionDto convertToQuestionDto(Question question) {
        try {
            List<String> options = objectMapper.readValue(
                    question.getOptions(),
                    new TypeReference<List<String>>() {}
            );

            return QuestionDto.builder()
                    .id(question.getId())
                    .question(question.getQuestion())
                    .options(options)
                    .build();
        } catch (Exception e) {
            throw new BadRequestException("Error parsing question options: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Quiz getQuizById(Long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + quizId));
    }
}