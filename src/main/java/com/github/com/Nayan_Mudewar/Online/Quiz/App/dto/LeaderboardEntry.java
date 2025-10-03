package com.github.com.Nayan_Mudewar.Online.Quiz.App.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaderboardEntry {
    private Long userId;
    private String userName;
    private String userEmail;
    private Integer score;
    private Double percentage;
    private LocalDateTime completedAt;
    private Integer rank;
}
