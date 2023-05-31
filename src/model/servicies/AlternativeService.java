package model.servicies;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.AlternativeDao;
import model.entities.Alternative;

public class AlternativeService {

	private AlternativeDao dao = DaoFactory.createAlternativeDao();

	public List<Alternative> findAll() {
		return dao.findAll();
	}

	public void saveOrUpdate(Alternative obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}
	
	public void remove(Alternative obj) {
		dao.deleteById(obj.getId());
	}
}
