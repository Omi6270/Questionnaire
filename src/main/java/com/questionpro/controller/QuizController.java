package com.questionpro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

import com.questionpro.dto.AnswerRequest;
import com.questionpro.model.ResultModel;
import com.questionpro.service.PollService;

@Controller
public class QuizController {

    @Autowired
    private PollService pollService;

    @GetMapping("/home")
    public String showForm(Model model) {
        model.addAttribute("questions", pollService.getAllQuestions());
        return "quiz"; // quiz.html
    }

    @PostMapping("/submit")
    public String submitQuiz(@ModelAttribute AnswerRequest answerRequest, Model model) {
        ResultModel result = pollService.submitAnswers(
                answerRequest.getName(),
                answerRequest.getEmail(),
                answerRequest.getAnswers()
        );

        model.addAttribute("result", result);
        model.addAttribute("answers", answerRequest.getAnswers());
      //  model.addAttribute("leaderboard", pollService.getLeaderboard());

        return "result";
    }
    
    @GetMapping("/leaderboard")
    public String showLeaderboard(Model model) {
        model.addAttribute("leaderboard", pollService.getLeaderboard());
        return "leaderboard";
    }

    
}
