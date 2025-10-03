package com.github.com.Nayan_Mudewar.Online.Quiz.App.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.Question;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.entity.Quiz;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.repository.QuestionRepository;
import com.github.com.Nayan_Mudewar.Online.Quiz.App.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        if (quizRepository.count() == 0) {
            loadSampleData();
        }
    }

    private void loadSampleData() throws Exception {
        // Create a sample quiz
        Quiz quiz = Quiz.builder()
                .title("General Knowledge Quiz")
                .category("General")
                .build();
        quiz = quizRepository.save(quiz);

        String[][] questionsData = {
                {"What is the capital of France?", "Berlin,Madrid,Paris,Rome", "Paris"},
                {"Who painted the Mona Lisa?", "Van Gogh,Picasso,Leonardo da Vinci,Michelangelo", "Leonardo da Vinci"},
                {"What is 2 + 2?", "3,4,5,6", "4"},
                {"Which planet is known as the Red Planet?", "Venus,Mars,Jupiter,Saturn", "Mars"},
                {"Who wrote Romeo and Juliet?", "Charles Dickens,William Shakespeare,Jane Austen,Mark Twain", "William Shakespeare"},
                {"What is the largest ocean?", "Atlantic,Indian,Arctic,Pacific", "Pacific"},
                {"How many continents are there?", "5,6,7,8", "7"},
                {"What is the smallest prime number?", "0,1,2,3", "2"},
                {"Which animal is known as the King of the Jungle?", "Tiger,Elephant,Lion,Bear", "Lion"},
                {"What is the chemical symbol for water?", "H2O,CO2,O2,NaCl", "H2O"},
                {"How many days are in a leap year?", "364,365,366,367", "366"},
                {"What is the capital of Japan?", "Seoul,Beijing,Tokyo,Bangkok", "Tokyo"},
                {"Who invented the telephone?", "Thomas Edison,Nikola Tesla,Alexander Graham Bell,Albert Einstein", "Alexander Graham Bell"},
                {"What is the largest mammal?", "Elephant,Blue Whale,Giraffe,Polar Bear", "Blue Whale"},
                {"How many sides does a hexagon have?", "5,6,7,8", "6"}
        };

        for (String[] data : questionsData) {
            List<String> options = Arrays.asList(data[1].split(","));
            String optionsJson = objectMapper.writeValueAsString(options);

            Question question = Question.builder()
                    .quiz(quiz)
                    .question(data[0])
                    .options(optionsJson)
                    .correctAnswer(data[2])
                    .build();
            questionRepository.save(question);
        }

        System.out.println("✅ Sample quiz data loaded successfully!");
    }
}
