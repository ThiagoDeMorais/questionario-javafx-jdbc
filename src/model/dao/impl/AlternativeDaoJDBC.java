package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.AlternativeDao;
import model.entities.Alternative;
import model.entities.Question;

public class AlternativeDaoJDBC implements AlternativeDao {

	private Connection conn;

	public AlternativeDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Alternative obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO \r\n"
					+ "alternative (id_question, description, isCorrect) \r\n"
					+ "VALUES (?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setInt(1, obj.getQuestion().getId());
			st.setString(2, obj.getDecription());
			st.setString(3, obj.getIsCorrect() == true ? "V":"F");
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected>0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}else {
				throw new DbException("Erro inesperado, nenhuma linha alterada!");
			}
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Alternative obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE alternative\r\n"
					+ "SET id_question = ?, description = ?, isCorrect = ?\r\n"
					+ "WHERE id = ?",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setInt(1, obj.getQuestion().getId());
			st.setString(2, obj.getDecription());
			st.setString(3, obj.getIsCorrect() == true ? "V":"F");
			st.setInt(4, obj.getId());
			
			st.executeUpdate();
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM alternative\r\n"
					+ "WHERE id = ?");
			
			st.setInt(1, id );
			st.executeUpdate();
		}catch(SQLException e ) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Alternative findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT alternative.*,question.description as description "
					+ "FROM alternative INNER JOIN question " + "ON alternative.id_question = question.id "
					+ "WHERE alternative.id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Question per = instantiateQuestion(rs);
				Alternative obj = instantiateAlternative(rs, per);
				return obj;

			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Alternative instantiateAlternative(ResultSet rs, Question per) throws SQLException {
		Alternative obj = new Alternative();
		obj.setId(rs.getInt("id"));
		obj.setDecription(rs.getString("description"));
		obj.setIsCorrect(rs.getString("isCorrect") == "V" ? true : false);
		obj.setQuestion(per);
		return obj;
	}

	private Question instantiateQuestion(ResultSet rs) throws SQLException {
		Question per = new Question();
		per.setId(rs.getInt("id_question"));
		per.setEnunciado(rs.getString("description"));
		return per;
	}

	@Override
	public List<Alternative> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT alternative.*,question.description as description\r\n"
					+ "FROM alternative INNER JOIN question\r\n" + "ON alternative.id_question = question.id\r\n"
					+ "ORDER BY id");

			rs = st.executeQuery();

			List<Alternative> list = new ArrayList<>();
			Map<Integer, Question> map = new HashMap<>();

			while (rs.next()) {
				Question per = map.get(rs.getInt("id_question"));

				if (per == null) {
					per = instantiateQuestion(rs);
					map.put(rs.getInt("id_question"), per);
				}

				Alternative obj = instantiateAlternative(rs, per);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Alternative> findByQuestion(Question question) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT alternative.*,question.description as description\r\n"
					+ "FROM alternative INNER JOIN question\r\n" + "ON alternative.id_question = question.id\r\n"
					+ "WHERE id_question = ?\r\n" + "ORDER BY id");

			st.setInt(1, question.getId());

			rs = st.executeQuery();

			List<Alternative> list = new ArrayList<>();
			Map<Integer, Question> map = new HashMap<>();

			while (rs.next()) {
				Question per = map.get(rs.getInt("id_question"));

				if (per == null) {
					per = instantiateQuestion(rs);
					map.put(rs.getInt("id_question"), per);
				}

				Alternative obj = instantiateAlternative(rs, per);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
