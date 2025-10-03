package com.github.com.Nayan_Mudewar.Online.Quiz.App.service;

import com.github.com.Nayan_Mudewar.Online.Quiz.App.dto.LeaderboardEntry;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.dto.LeaderboardResponse;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.Attempt;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.Quiz;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.exception.ResourceNotFoundException;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.repository.AttemptRepository;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final AttemptRepository attemptRepository;
    private final QuizRepository quizRepository;

    @Transactional(readOnly = true)
    public LeaderboardResponse getLeaderboard(Long quizId, Integer limit) {
        // Validate quiz exists
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with id: " + quizId));

        // Get all attempts for this quiz, sorted by score DESC, then by completedAt ASC
        List<Attempt> attempts = attemptRepository.findBestAttemptPerUserByQuizId(quizId);

        // Build leaderboard entries
        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        int rank = 1;
        Integer previousScore = null;
        int actualRank = 1;

        for (int i = 0; i < attempts.size() && (limit == null || i < limit); i++) {
            Attempt attempt = attempts.get(i);

            // Handle ties - same score gets same rank
            if (previousScore != null && !previousScore.equals(attempt.getScore())) {
                rank = actualRank;
            }

            double percentage = (attempt.getScore() * 100.0) / 10;

            LeaderboardEntry entry = LeaderboardEntry.builder()
                    .userId(attempt.getUser().getId())
                    .userName(attempt.getUser().getName())
                    .userEmail(attempt.getUser().getEmail())
                    .score(attempt.getScore())
                    .percentage(percentage)
                    .completedAt(attempt.getCompletedAt())
                    .rank(rank)
                    .build();

            leaderboard.add(entry);
            previousScore = attempt.getScore();
            actualRank++;
        }

        return LeaderboardResponse.builder()
                .quizId(quiz.getId())
                .quizTitle(quiz.getTitle())
                .totalAttempts(attempts.size())
                .leaderboard(leaderboard)
                .build();
    }

    @Transactional(readOnly = true)
    public LeaderboardResponse getTopLeaderboard(Long quizId) {
        return getLeaderboard(quizId, 10); // Default top 10
    }
}