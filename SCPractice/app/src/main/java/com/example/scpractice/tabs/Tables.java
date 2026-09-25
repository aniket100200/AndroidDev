package com.example.scpractice.tabs;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.scpractice.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

public class Tables extends Fragment {

    private final Random random = new Random();
    private TextInputLayout tilStartTable;
    private TextInputLayout tilEndTable;
    private TextInputLayout tilAnswer;
    private TextInputEditText etStartTable;
    private TextInputEditText etEndTable;
    private TextInputEditText etAnswer;
    private TextView tvQuestion;
    private TextView tvFeedback;
    private MaterialButton btnSubmit;
    private MaterialButton btnNext;
    private int firstNumber;
    private int secondNumber;
    private int correctAnswer;

    private boolean answerSubmitted = false;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_tables,
                container,
                false
        );

        initializeViews(view);
        setupListeners();

        // Generate the first question.
        generateQuestion();

        return view;
    }

    private void initializeViews(View view) {

        tilStartTable = view.findViewById(R.id.tilStartTable);
        tilEndTable = view.findViewById(R.id.tilEndTable);
        tilAnswer = view.findViewById(R.id.tilAnswer);

        etStartTable = view.findViewById(R.id.etStartTable);
        etEndTable = view.findViewById(R.id.etEndTable);
        etAnswer = view.findViewById(R.id.etAnswer);

        tvQuestion = view.findViewById(R.id.tvQuestion);
        tvFeedback = view.findViewById(R.id.tvFeedback);

        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnNext = view.findViewById(R.id.btnNext);
    }

    private void setupListeners() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQuestion();
            }
        });
    }

    /**
     * Generates a random multiplication question.
     * <p>
     * Example:
     * Start = 12
     * End = 20
     * <p>
     * First number -> random number between 12 and 20
     * Second number -> random number between 1 and 10
     * <p>
     * Example result:
     * 19 × 7 = ?
     */
    private void generateQuestion() {

        if (!validateRange()) {
            return;
        }

        int startTable = Integer.parseInt(
                etStartTable.getText().toString().trim()
        );

        int endTable = Integer.parseInt(
                etEndTable.getText().toString().trim()
        );

        // Inclusive random number:
        // random.nextInt(max - min + 1) + min
        firstNumber = random.nextInt(
                endTable - startTable + 1
        ) + startTable;

        // Second number is always between 1 and 10 inclusive.
        secondNumber = random.nextInt(10) + 1;

        correctAnswer = firstNumber * secondNumber;

        tvQuestion.setText(
                firstNumber + " × " + secondNumber + " = ?"
        );

        // Reset answer field.
        etAnswer.setText("");

        // Reset feedback.
        tvFeedback.setText("");
        tvFeedback.setVisibility(View.GONE);

        tilAnswer.setError(null);

        // Submit is available for the new question.
        btnSubmit.setEnabled(true);

        // Next remains disabled until answer is submitted.
        btnNext.setEnabled(false);

        answerSubmitted = false;

        // Focus answer field.
        etAnswer.requestFocus();

        showKeyboard();
    }

    /**
     * Validates the start and end table values.
     */
    private boolean validateRange() {

        tilStartTable.setError(null);
        tilEndTable.setError(null);

        String startText = etStartTable.getText() == null
                ? ""
                : etStartTable.getText().toString().trim();

        String endText = etEndTable.getText() == null
                ? ""
                : etEndTable.getText().toString().trim();

        if (startText.isEmpty()) {
            tilStartTable.setError("Enter start table");
            return false;
        }

        if (endText.isEmpty()) {
            tilEndTable.setError("Enter end table");
            return false;
        }

        int startTable;
        int endTable;

        try {
            startTable = Integer.parseInt(startText);
            endTable = Integer.parseInt(endText);
        } catch (NumberFormatException e) {
            tilStartTable.setError("Enter a valid number");
            tilEndTable.setError("Enter a valid number");
            return false;
        }

        if (startTable <= 0) {
            tilStartTable.setError("Must be greater than 0");
            return false;
        }

        if (endTable <= 0) {
            tilEndTable.setError("Must be greater than 0");
            return false;
        }

        if (startTable > endTable) {
            tilStartTable.setError("Start must be ≤ End");
            return false;
        }

        return true;
    }

    /**
     * Checks the user's multiplication answer.
     */
    private void checkAnswer() {

        tilAnswer.setError(null);

        String answerText = etAnswer.getText() == null
                ? ""
                : etAnswer.getText().toString().trim();

        if (answerText.isEmpty()) {
            tilAnswer.setError("Please enter your answer");
            etAnswer.requestFocus();
            return;
        }

        int userAnswer;

        try {
            userAnswer = Integer.parseInt(answerText);
        } catch (NumberFormatException e) {
            tilAnswer.setError("Enter a valid number");
            return;
        }

        hideKeyboard();

        if (userAnswer == correctAnswer) {

            tvFeedback.setText("Correct! 🎉");
            tvFeedback.setTextColor(
                    Color.rgb(46, 125, 50)
            );

        } else {

            tvFeedback.setText(
                    "Incorrect, the answer is " + correctAnswer
            );

            tvFeedback.setTextColor(
                    Color.rgb(198, 40, 40)
            );
        }

        tvFeedback.setVisibility(View.VISIBLE);

        // Prevent multiple submissions for the same question.
        btnSubmit.setEnabled(false);

        // Allow the user to move to the next question.
        btnNext.setEnabled(true);

        answerSubmitted = true;
    }

    /**
     * Displays the keyboard for the answer field.
     */
    private void showKeyboard() {

        if (etAnswer == null) {
            return;
        }

        etAnswer.postDelayed(new Runnable() {
            @Override
            public void run() {

                Context context = getContext();

                if (context != null) {

                    InputMethodManager imm =
                            (InputMethodManager) context.getSystemService(
                                    Context.INPUT_METHOD_SERVICE
                            );

                    if (imm != null) {
                        imm.showSoftInput(
                                etAnswer,
                                InputMethodManager.SHOW_IMPLICIT
                        );
                    }
                }

            }
        }, 200);
    }

    /**
     * Hides the keyboard after submitting the answer.
     */
    private void hideKeyboard() {

        if (getContext() == null || etAnswer == null) {
            return;
        }

        InputMethodManager imm =
                (InputMethodManager) getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE
                );

        if (imm != null) {
            imm.hideSoftInputFromWindow(
                    etAnswer.getWindowToken(),
                    0
            );
        }

        etAnswer.clearFocus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        tilStartTable = null;
        tilEndTable = null;
        tilAnswer = null;

        etStartTable = null;
        etEndTable = null;
        etAnswer = null;

        tvQuestion = null;
        tvFeedback = null;

        btnSubmit = null;
        btnNext = null;
    }
}