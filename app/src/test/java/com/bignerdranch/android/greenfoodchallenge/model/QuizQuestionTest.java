package com.bignerdranch.android.greenfoodchallenge.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class QuizQuestionTest {

    @Test
    public void testQuizQuestionConstructor() {
        int questionText = 0;
        int optionOneText = 0;
        int optionTwoText = 0;

        QuizQuestion newQuestion = new QuizQuestion(questionText, optionOneText, optionTwoText);
        assertEquals(questionText, newQuestion.get_mQuestionText());
        assertEquals(optionOneText, newQuestion.get_mOption2Text());
        assertEquals(optionTwoText, newQuestion.get_mOption1Text());

        int difQuestionText = 51;
        int difOptionOneText = 86;
        int difOptionTwoText = 165;
        newQuestion.set_mQuestionText(difQuestionText);
        newQuestion.set_mOption1Text(difOptionOneText);
        newQuestion.set_mOption2Text(difOptionTwoText);

        assertNotEquals(questionText, newQuestion.get_mQuestionText());
        assertNotEquals(optionOneText, newQuestion.get_mOption1Text());
        assertNotEquals(optionTwoText, newQuestion.get_mOption2Text());

        assertEquals(difQuestionText, newQuestion.get_mQuestionText());
        assertEquals(difOptionOneText, newQuestion.get_mOption1Text());
        assertEquals(difOptionTwoText, newQuestion.get_mOption2Text());
    }
}
