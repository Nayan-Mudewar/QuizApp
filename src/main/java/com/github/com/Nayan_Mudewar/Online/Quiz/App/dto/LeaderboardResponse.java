package com.github.com.Nayan_Mudewar.Online.Quiz.App.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaderboardResponse {
    private Long quizId;
    private String quizTitle;
    private Integer totalAttempts;
    private List<LeaderboardEntry> leaderboard;
}