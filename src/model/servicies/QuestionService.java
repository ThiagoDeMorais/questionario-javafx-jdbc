package model.servicies;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.QuestionDao;
import model.entities.Question;

public class QuestionService {

	private QuestionDao dao = DaoFactory.createQuestionDao();

	public List<Question> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Question obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
}
