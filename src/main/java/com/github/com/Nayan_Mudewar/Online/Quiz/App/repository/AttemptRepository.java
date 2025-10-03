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
    @Query(value = """
        SELECT a.* FROM attempts a
        INNER JOIN (
            SELECT user_id, MAX(score) as max_score, MIN(completed_at) as earliest_time
            FROM attempts
            WHERE quiz_id = :quizId
            GROUP BY user_id
        ) best ON a.user_id = best.user_id 
                AND a.score = best.max_score 
                AND a.completed_at = best.earliest_time
        WHERE a.quiz_id = :quizId
        ORDER BY a.score DESC, a.completed_at ASC
        """, nativeQuery = true)
    List<Attempt> findBestAttemptPerUserByQuizId(@Param("quizId") Long quizId);
}

