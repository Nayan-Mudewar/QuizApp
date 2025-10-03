package com.github.com.Nayan_Mudewar.Online.Quiz.App.controller;

import com.github.com.Nayan_Mudewar.Online.Quiz.App.dto.AttemptHistoryResponse;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.service.AttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final AttemptService attemptService;

    @GetMapping("/me/attempts")
    public ResponseEntity<List<AttemptHistoryResponse>> getUserAttempts() {
        List<AttemptHistoryResponse> attempts = attemptService.getUserAttempts();
        return ResponseEntity.ok(attempts);
    }
}
