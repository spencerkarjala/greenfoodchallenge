package com.bignerdranch.android.greenfoodchallenge.model;

/*
 *  Models question information for food quiz
 */

public class QuizQuestion {

  private int mQuestionText;
  private int mOption1Text;
  private int mOption2Text;

  public QuizQuestion(int mQuestionText, int mOption1Text, int mOption2Text) {
    this.mQuestionText = mQuestionText;
    this.mOption1Text = mOption1Text;
    this.mOption2Text = mOption2Text;
  }

  public int get_mQuestionText() {
    return mQuestionText;
  }

  public int get_mOption1Text() {
    return mOption1Text;
  }

  public int get_mOption2Text() {
    return mOption2Text;
  }

  public void set_mQuestionText(int mQuestionText) {
    this.mQuestionText = mQuestionText;
  }

  public void set_mOption1Text(int mOption1Text) {
    this.mOption1Text = mOption1Text;
  }

  public void set_mOption2Text(int mOption2Text) {
    this.mOption2Text = mOption2Text;
  }
}
