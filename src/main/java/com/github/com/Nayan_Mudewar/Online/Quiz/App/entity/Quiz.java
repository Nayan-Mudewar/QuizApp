package com.github.com.Nayan_Mudewar.Online.Quiz.App.entity;

import jakarta.persistence.*;
import lombok.*;
import org.aspectj.weaver.patterns.TypePatternQuestions;

import java.util.List;

@Entity
@Table(name = "quizzes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    private String title;

    // Bidirectional mapping to questions
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;
}
