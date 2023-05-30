package model.dao;

import db.DB;
import model.dao.impl.AlternativeDaoJDBC;
import model.dao.impl.QuestionDaoJDBC;

public class DaoFactory {

	public static AlternativeDao createAlternativeDao() {
		return new AlternativeDaoJDBC(DB.getConnection());
	}
	
	public static QuestionDao createQuestionDao() {
		return new QuestionDaoJDBC(DB.getConnection());
	}
}
