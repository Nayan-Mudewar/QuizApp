package com.github.com.Nayan_Mudewar.Online.Quiz.App.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.dto.QuizStartResponse;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.Question;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.Quiz;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.exception.BadRequestException;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.exception.ResourceNotFoundException;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.repository.QuestionRepository;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private QuizService quizService;

    private Quiz quiz;
    private List<Question> questions;

    @BeforeEach
    void setUp() {
        quiz = Quiz.builder()
                .id(1L)
                .title("Test Quiz")
                .category("General")
                .build();

        questions = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Question question = Question.builder()
                    .id((long) i)
                    .quiz(quiz)
                    .question("Question " + i)
                    .options("[\"Option1\",\"Option2\",\"Option3\",\"Option4\"]")
                    .correctAnswer("Option1")
                    .build();
            questions.add(question);
        }
    }

    @Test
    void startQuiz_Success() throws Exception {
        // Arrange
        when(quizRepository.findById(anyLong())).thenReturn(Optional.of(quiz));
        when(questionRepository.findRandomQuestionsByQuizId(anyLong())).thenReturn(questions);
        when(objectMapper.readValue(anyString(), any(com.fasterxml.jackson.core.type.TypeReference.class)))
                .thenReturn(List.of("Option1", "Option2", "Option3", "Option4"));

        // Act
        QuizStartResponse response = quizService.startQuiz(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getQuizId());
        assertEquals("Test Quiz", response.getTitle());
        assertEquals(10, response.getQuestions().size());
        verify(quizRepository, times(1)).findById(1L);
        verify(questionRepository, times(1)).findRandomQuestionsByQuizId(1L);
    }

    @Test
    void startQuiz_QuizNotFound_ThrowsException() {
        // Arrange
        when(quizRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> quizService.startQuiz(1L)
        );
        assertEquals("Quiz not found with id: 1", exception.getMessage());
    }

    @Test
    void startQuiz_LessThan10Questions_ThrowsException() {
        // Arrange
        when(quizRepository.findById(anyLong())).thenReturn(Optional.of(quiz));
        when(questionRepository.findRandomQuestionsByQuizId(anyLong()))
                .thenReturn(questions.subList(0, 5)); // Only 5 questions

        // Act & Assert
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> quizService.startQuiz(1L)
        );
        assertTrue(exception.getMessage().contains("at least 10 questions"));
    }

    @Test
    void getAllQuizzes_Success() {
        // Arrange
        List<Quiz> quizzes = List.of(quiz);
        when(quizRepository.findAll()).thenReturn(quizzes);

        // Act
        List<Quiz> result = quizService.getAllQuizzes();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(quizRepository, times(1)).findAll();
    }
}
