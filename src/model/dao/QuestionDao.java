package model.dao;

import java.util.List;

import model.entities.Question;

public interface QuestionDao {
	void insert(Question obj);
	void update(Question obj);
	void deleteById(Integer id);
	Question findById(Integer id);
	List<Question> findAll();
}
