package model.entities;

import java.io.Serializable;
import java.util.Objects;

public class Alternative implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String description;
	private Boolean isCorrect;
	private Question question;
	
	public Alternative() {
		
	}

	public Alternative(String description, boolean isCorrect, Question question) {
		this.description = description;
		this.isCorrect = isCorrect;
		this.question = question;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsCorrect() {
		return isCorrect;
	}

	public void setIsCorrect(Boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Alternative other = (Alternative) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Alternativa [id=" + id + ", conteudo=" + description + ", ehVerdadeira=" + isCorrect + ", pergunta="
				+ question + "]";
	}
}
