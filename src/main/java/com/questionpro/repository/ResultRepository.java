package com.questionpro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.questionpro.model.ResultModel;

public interface ResultRepository extends JpaRepository<ResultModel, Long> {
  List<ResultModel> findAllByOrderByScoreDesc();
  
  List<ResultModel> findAllByEmail(String email);

}