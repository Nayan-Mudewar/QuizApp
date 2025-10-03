package com.github.com.Nayan_Mudewar.Online.Quiz.App.repository;

import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
}


