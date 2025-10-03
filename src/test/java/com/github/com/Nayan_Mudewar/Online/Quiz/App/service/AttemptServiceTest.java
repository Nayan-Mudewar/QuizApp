package com.github.com.Nayan_Mudewar.Online.Quiz.App.service;

import com.github.com.Nayan_Mudewar.Online.Quiz.App.dto.QuizSubmitRequest;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.dto.QuizSubmitResponse;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.Attempt;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.Question;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.Quiz;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.User;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.exception.BadRequestException;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.repository.AttemptRepository;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.repository.QuestionRepository;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.repository.QuizRepository;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttemptServiceTest {

    @Mock
    private AttemptRepository attemptRepository;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AttemptService attemptService;

    private User user;
    private Quiz quiz;
    private List<Question> questions;
    private Map<Long, String> answers;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .password("password")
                .build();

        quiz = Quiz.builder()
                .id(1L)
                .title("Test Quiz")
                .category("General")
                .build();

        questions = new ArrayList<>();
        answers = new HashMap<>();

        for (int i = 1; i <= 10; i++) {
            Question question = Question.builder()
                    .id((long) i)
                    .quiz(quiz)
                    .question("Question " + i)
                    .options("[\"Option1\",\"Option2\",\"Option3\",\"Option4\"]")
                    .correctAnswer("Option1")
                    .build();
            questions.add(question);
            answers.put((long) i, "Option1"); // All correct answers
        }

        // Mock Security Context
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("john@example.com");
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void submitQuiz_AllCorrect_Score10() {
        // Arrange
        QuizSubmitRequest request = new QuizSubmitRequest(1L, answers);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(quizRepository.findById(anyLong())).thenReturn(Optional.of(quiz));
        when(questionRepository.findAllById(any())).thenReturn(questions);
        when(attemptRepository.save(any(Attempt.class))).thenAnswer(invocation -> {
            Attempt attempt = invocation.getArgument(0);
            attempt.setId(1L);
            return attempt;
        });

        // Act
        QuizSubmitResponse response = attemptService.submitQuiz(request);

        // Assert
        assertNotNull(response);
        assertEquals(10, response.getScore());
        assertEquals(100.0, response.getPercentage());
        assertEquals(10, response.getResults().size());
        verify(attemptRepository, times(1)).save(any(Attempt.class));
    }

    @Test
    void submitQuiz_InvalidAnswerCount_ThrowsException() {
        // Arrange
        Map<Long, String> fiveAnswers = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            fiveAnswers.put((long) i, "Option1");
        }
        QuizSubmitRequest request = new QuizSubmitRequest(1L, fiveAnswers);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(quizRepository.findById(anyLong())).thenReturn(Optional.of(quiz));

        // Act & Assert
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> attemptService.submitQuiz(request)
        );
        assertTrue(exception.getMessage().contains("exactly 10 answers"));
    }

    @Test
    void submitQuiz_SomeWrongAnswers_CorrectScore() {
        // Arrange
        answers.put(1L, "WrongAnswer"); // 1 wrong
        answers.put(2L, "WrongAnswer"); // 2 wrong
        QuizSubmitRequest request = new QuizSubmitRequest(1L, answers);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(quizRepository.findById(anyLong())).thenReturn(Optional.of(quiz));
        when(questionRepository.findAllById(any())).thenReturn(questions);
        when(attemptRepository.save(any(Attempt.class))).thenAnswer(invocation -> {
            Attempt attempt = invocation.getArgument(0);
            attempt.setId(1L);
            return attempt;
        });

        // Act
        QuizSubmitResponse response = attemptService.submitQuiz(request);

        // Assert
        assertEquals(8, response.getScore()); // 8 correct out of 10
        assertEquals(80.0, response.getPercentage());
    }
}
