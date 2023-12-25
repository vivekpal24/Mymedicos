package com.medical.my_medicos.activities.pg.model;

import com.hishd.tinycart.model.Item;

import java.io.Serializable;
import java.math.BigDecimal;

public class QuizPGinsider implements Item, Serializable {

    private String question, optionA, optionB, optionC, optionD, correctAnswer, idQuestion, titleOfSet, specialityQuiz;

    private String selectedOption; // Add this field

    private String image;

    public QuizPGinsider(String question, String optionA, String optionB, String optionC, String optionD, String correctAnswer, String idQuestion, String titleOfSet, String specialityQuiz, String image) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.idQuestion = idQuestion; // Initialize idQuestion
        this.titleOfSet = titleOfSet; // Initialize titleOfSet
        this.specialityQuiz = specialityQuiz;
        this.image = image;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }


    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getTitleOfSet() {
        return titleOfSet;
    }

    public void setTitleOfSet(String titleOfSet) {
        this.titleOfSet = titleOfSet;
    }

    public String getSpecialityQuiz() {
        return specialityQuiz;
    }

    public void setSpecialityQuiz(String specialityQuiz) {
        this.specialityQuiz = specialityQuiz;
    }



    @Override
    public BigDecimal getItemPrice() {
        return null;
    }

    @Override
    public String getItemName() {
        return question;
    }
}