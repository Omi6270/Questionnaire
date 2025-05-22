package com.questionpro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.questionpro.model.Question;
import com.questionpro.repository.QuestionRepository;
import com.questionpro.service.PollService;

@RestController
@RequestMapping("/api")
public class QuestionController {
	@Autowired
	private QuestionRepository questionRepository;
	
	 @Autowired
	 private PollService pollService;

	@PostMapping("/questions")
	public ResponseEntity<String> addQuestion(@RequestBody Question question) {
		questionRepository.save(question);
	    return ResponseEntity.ok("Question Added");
	}
	
	 @GetMapping("/questions")
	    public List<Question> getQuestions() {
	        return pollService.getAllQuestions();
	   }
}
