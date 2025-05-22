package com.questionpro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.questionpro.dto.AnswerRequest;
import com.questionpro.model.Question;
import com.questionpro.model.ResultModel;
import com.questionpro.model.User;
import com.questionpro.repository.UserRepository;
import com.questionpro.service.PollService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private PollService pollService;
    
    @Autowired
    private UserRepository  userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid User user) {
    	 userRepository.save(user);
        return ResponseEntity.ok("Registered");
    }
    
    @GetMapping("/users")
    public List<User> getAllUser(){
    	return pollService.findALl();
    }

    @PostMapping("/submit")
    public ResponseEntity<ResultModel> submitAnswers(@RequestBody AnswerRequest request) {
        ResultModel result = pollService.submitAnswers(request.getName(), request.getEmail(), request.getAnswers());
        return ResponseEntity.ok(result);
    }

   
    
    @GetMapping("/result/{email}")
    public ResponseEntity<List<ResultModel>>getResultsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(pollService.getResultsByEmail(email));
    }


    @GetMapping("/all-results")
    public List<ResultModel> getAllResults() {
        return pollService.getAllResults();
    }
    
    @GetMapping("/test-bestResult")
    public List<Integer> getBestScore(){
    	return pollService.getBestScore();
    }

}
