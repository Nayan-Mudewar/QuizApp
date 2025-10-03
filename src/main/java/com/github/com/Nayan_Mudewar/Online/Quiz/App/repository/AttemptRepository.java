package com.github.com.Nayan_Mudewar.Online.Quiz.App.repository;

import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.Attempt;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.Quiz;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttemptRepository extends JpaRepository<Attempt, Long> {
    List<Attempt> findByUserIdOrderByCompletedAtDesc(Long userId);
    @Query("SELECT a FROM Attempt a WHERE a.quiz.id = :quizId ORDER BY a.score DESC, a.completedAt ASC")
    List<Attempt> findTopScoresByQuizId(@Param("quizId") Long quizId);
}

