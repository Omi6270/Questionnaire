package com.questionpro.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.questionpro.model.Question;
import com.questionpro.model.ResultModel;
import com.questionpro.model.User;
import com.questionpro.repository.QuestionRepository;
import com.questionpro.repository.ResultRepository;
import com.questionpro.repository.UserRepository;

import jakarta.persistence.EntityManager;

@Service
public class PollService {
	
	@Autowired
	private EntityManager entityManager;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private ResultRepository resultRepo;

    public List<Question> getAllQuestions() {
        return questionRepo.findAll();
    }

    public ResultModel submitAnswers(String name, String email, Map<Long, String> answers) {
    
    	
        Optional<User> optionalUser = userRepo.findByEmail(email);

        
        User user = optionalUser.orElseGet(() -> {
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            return userRepo.save(newUser);
        });
        
        List<Question> questions = questionRepo.findAll();
        int score = 0;

        for (Question q : questions) {
            if (answers.containsKey(q.getId()) && q.getCorrectAnswer().equalsIgnoreCase(answers.get(q.getId()))) {
                score++;
            }
        }

        ResultModel result = new ResultModel();
        result.setName(name);
        result.setEmail(email);
        result.setScore(score);

        result.setPass(score >= (questions.size() / 2)); // pass if >= 50%
        return resultRepo.save(result);
    }

    public List<ResultModel>getResultsByEmail(String email) {
        return resultRepo.findAllByEmail(email);
    }


    public List<ResultModel> getAllResults() {
        return resultRepo.findAllByOrderByScoreDesc();
    }
    
    public List<User> findALl(){
    	return userRepo.findAll();
    }
    

    // For Debugging
    List<Integer> testBestResult=new ArrayList<>();
    public List<Integer> getBestScore(){
    	return testBestResult;
    }
    
    public List<ResultModel> getLeaderboard() {
        List<User> users = userRepo.findAll();
        List<ResultModel> bestResults = new ArrayList<>();

        for (User user : users) {
            List<ResultModel> results = resultRepo.findAllByEmail(user.getEmail());
            ResultModel best = results.stream()
                    .max(Comparator.comparingInt(ResultModel::getScore))
                    .orElse(null);
            if (best != null) {
                bestResults.add(best);
            }
        }

        // Sort by score descending
        bestResults.sort(Comparator.comparingInt(ResultModel::getScore).reversed());
      
        // For Debugging 
        for (ResultModel result : bestResults) {
            testBestResult.add( result.getScore());
        }
        

        
        List<Object[]> emailRanks = entityManager.createNativeQuery("""
        	    WITH max_scores AS (
        	        SELECT email, MAX(score) AS max_score
        	        FROM result_table
        	        GROUP BY email
        	    )
        	    SELECT email, max_score, DENSE_RANK() OVER (ORDER BY max_score DESC) AS ranklist
        	    FROM max_scores
        	""").getResultList();

        	// Map email → rank
        	Map<String, Integer> emailToRank = new HashMap<>();
        	for (Object[] row : emailRanks) {
        	    String email = (String) row[0];
        	    Integer rank = ((Number) row[2]).intValue(); // row[2] = rank
        	    emailToRank.put(email, rank);
        	}

        	// Assign ranks to bestResults
        	for (ResultModel result : bestResults) {
        	    Integer rank = emailToRank.get(result.getEmail());
        	    result.setUserRank(rank != null ? rank : 0);  
        	}

        	resultRepo.saveAll(bestResults);
        	return bestResults;

    }


}

