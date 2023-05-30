package model.servicies;

import java.util.ArrayList;
import java.util.List;

import model.entities.Question;

public class QuestionService {
	
	
	public List<Question>findAll(){
		List<Question> list  = new ArrayList();
		list.add(new Question(1, "Qual a cor?"));
		list.add(new Question(2, "Qual o tamanho?"));
		list.add(new Question(3, "Qual a largura?"));
		list.add(new Question(4, "Qual a altura?"));
		list.add(new Question(5, "Qual o comprimento?"));
		return list;
	}
}
