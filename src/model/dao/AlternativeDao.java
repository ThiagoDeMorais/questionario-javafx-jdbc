package model.dao;

import java.util.List;

import model.entities.Alternative;
import model.entities.Question;

public interface AlternativeDao {
	void insert(Alternative obj);
	void update(Alternative obj);
	void deleteById(Integer id);
	Alternative findById(Integer id);
	List<Alternative> findAll();
	List<Alternative> findByQuestion(Question question);
}
