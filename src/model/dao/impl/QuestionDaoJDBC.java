package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.QuestionDao;
import model.entities.Question;

public class QuestionDaoJDBC implements QuestionDao {
	
	private Connection conn;

	public QuestionDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Question obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO \r\n"
					+ "question (description) \r\n"
					+ "VALUES (?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getDescription());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected>0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}else {
				throw new DbException("Erro inesperado, nenhumk linha alterada!");
			}
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}		
	}

	@Override
	public void update(Question obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE question\r\n"
					+ "SET description = ?\r\n"
					+ "WHERE id = ?",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getDescription());
			st.setInt(2, obj.getId());

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
			st = conn.prepareStatement("DELETE FROM question\r\n"
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
	public Question findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM question\r\n"
					+ "WHERE id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Question per = instantiateQuestion(rs);
				return per;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Question instantiateQuestion(ResultSet rs) throws SQLException {
		Question per = new Question();
		per.setId(rs.getInt("id"));
		per.setEnunciado(rs.getString("description"));
		return per;
	}
	
	@Override
	public List<Question> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM question\r\n"
					+ "ORDER BY id");

			rs = st.executeQuery();

			List<Question> list = new ArrayList<>();

			while (rs.next()) {
				Question per = instantiateQuestion(rs);
				list.add(per);
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
