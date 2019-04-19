package com.android.greenfoodchallenge.controller.activities;

import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.greenfoodchallenge.R;
import com.android.greenfoodchallenge.model.QuizQuestion;

import java.util.Arrays;
import java.util.List;

public class QuizActivity extends BaseActivity {

  private TextView mQuestionText;
  private TextView mQuizIntro;
  private TextView mResultText;
  private Button mButtonOption1;
  private Button mButtonOption2;
  private List<String> mCorrectAnswers;
  private int mCurrentIndex = 0;
  private int mScore = 0;
  private String mAnswer;
  List<QuizQuestion> mQuestions;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quiz);

    fetchDataFromIntent();
    startToolbar();
    startNavDrawer();
    setTitle(getString(R.string.toolbar_title_quiz));

    this.startQuiz();
    this.startViews();

    this.setResponseAListener();
    this.setResponseBListener();
  }

  private void startViews() {
    this.mQuestionText  = findViewById(R.id.questionTextView);
    this.mQuizIntro     = findViewById(R.id.introTextView);
    this.mResultText    = findViewById(R.id.resultTextView);
    this.mButtonOption1 = findViewById(R.id.option1a);
    this.mButtonOption2 = findViewById(R.id.option1b);

    mResultText.setVisibility(View.INVISIBLE);
  }

  private void startQuiz() {
    mCorrectAnswers = Arrays.asList(getString(R.string.quiz_correct_answer_0),
        getString(R.string.quiz_correct_answer_1),
        getString(R.string.quiz_correct_answer_2),
        getString(R.string.quiz_correct_answer_3),
        getString(R.string.quiz_correct_answer_4),
        getString(R.string.quiz_correct_answer_5),
        getString(R.string.quiz_correct_answer_6),
        getString(R.string.quiz_correct_answer_7));
    mQuestions = Arrays.asList(
        new QuizQuestion(R.string.question1, R.string.option1a,
            R.string.option1b),
        new QuizQuestion(R.string.question2, R.string.option2a,
            R.string.option2b),
        new QuizQuestion(R.string.question3, R.string.option3a,
            R.string.option3b),
        new QuizQuestion(R.string.question4, R.string.option4a,
            R.string.option4b),
        new QuizQuestion(R.string.question5, R.string.option5a,
            R.string.option5b),
        new QuizQuestion(R.string.question6, R.string.option6a,
            R.string.option6b),
        new QuizQuestion(R.string.question7, R.string.option7a,
            R.string.option7b),
        new QuizQuestion(R.string.question8, R.string.option8a,
            R.string.option8b));
    mAnswer = mCorrectAnswers.get(mCurrentIndex);
  }

  private void setResponseAListener() {
    mButtonOption1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        handleResponseA();
      }
    });
  }

  private void setResponseBListener() {
    mButtonOption2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        handleResponseB();
      }
    });
  }

  private void displayCorrectToast() {
    mScore += 1;
    final Toast toast = Toast.makeText(QuizActivity.this,
        R.string.toast_correct,
        Toast.LENGTH_SHORT);
    toast.show();
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        toast.cancel();
      }
    }, 500);
  }

  private void displayIncorrectToast() {
    final Toast toast = Toast.makeText(QuizActivity.this,
        R.string.toast_incorrect,
        Toast.LENGTH_SHORT);
    toast.show();
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        toast.cancel();
      }
    }, 500);
  }

  private void updateQuestion() {
    mCurrentIndex++;

    if(this.checkIfQuizIsDone()){
      this.displayResults();
    } else {
      int question = mQuestions.get(mCurrentIndex).get_mQuestionText();
      int option1 = mQuestions.get(mCurrentIndex).get_mOption1Text();
      int option2 = mQuestions.get(mCurrentIndex).get_mOption2Text();
      mQuestionText.setText(question);
      mButtonOption1.setText(option1);
      mButtonOption2.setText(option2);
      mAnswer = mCorrectAnswers.get(mCurrentIndex);
    }
  }

  private boolean checkIfQuizIsDone() {
    int NUM_QUIZ_QUESTIONS = 7;
    return mCurrentIndex > NUM_QUIZ_QUESTIONS;
  }

  private boolean checkIfCorrectAnswer(String currentResponse) {
    return currentResponse.equals(mAnswer);
  }

  private void displayResults() {
    String resultText = getString(R.string.resultText1)
        + " "
        + mScore
        + " "
        + getString(R.string.resultText2);
    mQuestionText.setVisibility(View.INVISIBLE);
    mButtonOption1.setVisibility(View.INVISIBLE);
    mButtonOption2.setVisibility(View.INVISIBLE);
    mQuizIntro.setVisibility(View.INVISIBLE);
    mResultText.setText(resultText);
    mResultText.setVisibility(View.VISIBLE);
  }

  private void handleResponseA() {
    String currentResponse = mButtonOption1.getText().toString();
    if(checkIfCorrectAnswer(currentResponse)){
      this.displayCorrectToast();
    }
    else{
      this.displayIncorrectToast();
    }
    this.updateQuestion();
  }

  private void handleResponseB() {
    String currentResponse = mButtonOption1.getText().toString();
    if (checkIfCorrectAnswer(currentResponse)) {
      this.displayCorrectToast();
    } else {
      this.displayIncorrectToast();
    }
    this.updateQuestion();
  }
}

