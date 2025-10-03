package com.github.com.Nayan_Mudewar.Online.Quiz.App.service;

import com.github.com.Nayan_Mudewar.Online.Quiz.App.dto.AnswerResult;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.dto.AttemptHistoryResponse;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.dto.QuizSubmitRequest;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.dto.QuizSubmitResponse;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.Attempt;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.Question;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.Quiz;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.User;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.exception.BadRequestException;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.exception.ResourceNotFoundException;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.repository.AttemptRepository;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.repository.QuestionRepository;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.repository.QuizRepository;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttemptService {

    private final AttemptRepository attemptRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    @Transactional
    public QuizSubmitResponse submitQuiz(QuizSubmitRequest request) {
        // Get authenticated user
        User user = getAuthenticatedUser();

        // Get quiz
        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + request.getQuizId()));

        // Validate that we have exactly 10 answers
        if (request.getAnswers().size() != 10) {
            throw new BadRequestException("Quiz must have exactly 10 answers. Provided: " + request.getAnswers().size());
        }

        // Get all questions for validation
        List<Question> questions = questionRepository.findAllById(request.getAnswers().keySet());

        if (questions.size() != 10) {
            throw new BadRequestException("Invalid question IDs provided");
        }

        // Validate all questions belong to this quiz
        for (Question question : questions) {
            if (!question.getQuiz().getId().equals(quiz.getId())) {
                throw new BadRequestException("Question " + question.getId() + " does not belong to quiz " + quiz.getId());
            }
        }

        // Calculate score and build results
        int score = 0;
        List<AnswerResult> results = new ArrayList<>();

        for (Question question : questions) {
            String userAnswer = request.getAnswers().get(question.getId());
            boolean isCorrect = question.getCorrectAnswer().equalsIgnoreCase(userAnswer);

            if (isCorrect) {
                score++;
            }

            results.add(AnswerResult.builder()
                    .questionId(question.getId())
                    .question(question.getQuestion())
                    .userAnswer(userAnswer)
                    .correctAnswer(question.getCorrectAnswer())
                    .isCorrect(isCorrect)
                    .build());
        }

        // Save attempt
        Attempt attempt = Attempt.builder()
                .user(user)
                .quiz(quiz)
                .score(score)
                .completedAt(LocalDateTime.now())
                .build();

        attempt = attemptRepository.save(attempt);

        // Calculate percentage
        double percentage = (score * 100.0) / 10;

        return QuizSubmitResponse.builder()
                .attemptId(attempt.getId())
                .quizId(quiz.getId())
                .quizTitle(quiz.getTitle())
                .score(score)
                .totalQuestions(10)
                .percentage(percentage)
                .completedAt(attempt.getCompletedAt())
                .results(results)
                .build();
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public List<AttemptHistoryResponse> getUserAttempts() {
        User user = getAuthenticatedUser();

        List<Attempt> attempts = attemptRepository.findByUserIdOrderByCompletedAtDesc(user.getId());

        return attempts.stream()
                .map(this::convertToHistoryDto)
                .collect(Collectors.toList());
    }

    private AttemptHistoryResponse convertToHistoryDto(Attempt attempt) {
        double percentage = (attempt.getScore() * 100.0) / 10;

        return AttemptHistoryResponse.builder()
                .attemptId(attempt.getId())
                .quizId(attempt.getQuiz().getId())
                .quizTitle(attempt.getQuiz().getTitle())
                .quizCategory(attempt.getQuiz().getCategory())
                .score(attempt.getScore())
                .totalQuestions(10)
                .percentage(percentage)
                .completedAt(attempt.getCompletedAt())
                .build();
    }
}
