package com.github.com.Nayan_Mudewar.Online.Quiz.App.repository;

import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.Question;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query
    (value = "SELECT * FROM questions WHERE quiz_id = :quizId ORDER BY RAND() LIMIT 10",nativeQuery = true)
    List<Question> findRandomQuestionsByQuizId(@Param("quizId") Long quizId);

    List<Question> findByQuizId(Long quizId);
    }

