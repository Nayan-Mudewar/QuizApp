package com.github.com.Nayan_Mudewar.Online.Quiz.App.controller;

import com.github.com.Nayan_Mudewar.Online.Quiz.App.dto.LeaderboardResponse;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quizzes")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping("/{id}/leaderboard")
    public ResponseEntity<LeaderboardResponse> getLeaderboard(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        LeaderboardResponse response = leaderboardService.getLeaderboard(id, limit);
        return ResponseEntity.ok(response);
    }
}
