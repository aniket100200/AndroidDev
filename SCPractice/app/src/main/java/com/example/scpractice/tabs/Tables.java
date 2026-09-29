package com.example.scpractice.tabs;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.scpractice.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

public class Tables extends Fragment {

    private final Random random = new Random();
    /*
     * Handler is used to wait for the user to finish typing
     * before deciding that the answer is incorrect.
     */
    private final Handler handler = new Handler(Looper.getMainLooper());
    private TextInputLayout tilStartTable;
    private TextInputLayout tilEndTable;
    private TextInputLayout tilAnswer;
    private TextInputEditText etStartTable;
    private TextInputEditText etEndTable;
    private TextInputEditText etAnswer;
    private TextView tvQuestion;
    private TextView tvFeedback;
    private int firstNumber;
    private int secondNumber;
    private int correctAnswer;

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

        generateQuestion();

        return view;
    }    private final Runnable checkAnswerRunnable = new Runnable() {
        @Override
        public void run() {
            checkAnswerAutomatically();
        }
    };

    private void initializeViews(View view) {

        tilStartTable = view.findViewById(R.id.tilStartTable);
        tilEndTable = view.findViewById(R.id.tilEndTable);
        tilAnswer = view.findViewById(R.id.tilAnswer);

        etStartTable = view.findViewById(R.id.etStartTable);
        etEndTable = view.findViewById(R.id.etEndTable);
        etAnswer = view.findViewById(R.id.etAnswer);

        tvQuestion = view.findViewById(R.id.tvQuestion);
        tvFeedback = view.findViewById(R.id.tvFeedback);
    }

    private void setupListeners() {

        /*
         * Automatically check the answer whenever
         * the user changes the text.
         */
        etAnswer.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count) {

                // Remove previous pending check.
                handler.removeCallbacks(checkAnswerRunnable);

                // Clear previous error.
                tilAnswer.setError(null);

                String answer = s.toString().trim();

                if (answer.isEmpty()) {
                    tvFeedback.setText("");
                    tvFeedback.setVisibility(View.GONE);
                    return;
                }

                /*
                 * If the answer is exactly correct,
                 * show Correct immediately.
                 */
                try {

                    int userAnswer = Integer.parseInt(answer);

                    if (userAnswer == correctAnswer) {

                        showCorrect();

                        return;
                    }

                } catch (NumberFormatException e) {

                    tilAnswer.setError("Enter a valid number");

                    return;
                }

                /*
                 * Don't immediately mark a partially typed answer
                 * as incorrect.
                 *
                 * Wait 700ms after the user stops typing.
                 */
                handler.postDelayed(
                        checkAnswerRunnable,
                        700
                );
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * Generates a random multiplication question.
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

        firstNumber = random.nextInt(
                endTable - startTable + 1
        ) + startTable;

        secondNumber = random.nextInt(10) + 1;

        correctAnswer = firstNumber * secondNumber;

        tvQuestion.setText(
                firstNumber + " × " + secondNumber + " = ?"
        );

        /*
         * Reset answer.
         */
        etAnswer.setText("");

        tilAnswer.setError(null);

        /*
         * Reset feedback.
         */
        tvFeedback.setText("");
        tvFeedback.setVisibility(View.GONE);

        /*
         * Focus answer field.
         */
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

            tilStartTable.setError(
                    "Enter start table"
            );

            return false;
        }

        if (endText.isEmpty()) {

            tilEndTable.setError(
                    "Enter end table"
            );

            return false;
        }

        int startTable;
        int endTable;

        try {

            startTable = Integer.parseInt(startText);
            endTable = Integer.parseInt(endText);

        } catch (NumberFormatException e) {

            tilStartTable.setError(
                    "Enter a valid number"
            );

            tilEndTable.setError(
                    "Enter a valid number"
            );

            return false;
        }

        if (startTable <= 0) {

            tilStartTable.setError(
                    "Must be greater than 0"
            );

            return false;
        }

        if (endTable <= 0) {

            tilEndTable.setError(
                    "Must be greater than 0"
            );

            return false;
        }

        if (startTable > endTable) {

            tilStartTable.setError(
                    "Start must be ≤ End"
            );

            return false;
        }

        return true;
    }

    /**
     * Checks the answer after the user has stopped typing.
     */
    private void checkAnswerAutomatically() {

        String answerText = etAnswer.getText() == null
                ? ""
                : etAnswer.getText().toString().trim();

        if (answerText.isEmpty()) {
            return;
        }

        int userAnswer;

        try {

            userAnswer = Integer.parseInt(answerText);

        } catch (NumberFormatException e) {

            tilAnswer.setError(
                    "Enter a valid number"
            );

            return;
        }

        if (userAnswer == correctAnswer) {

            showCorrect();

        } else {

            showIncorrect();
        }
    }

    /**
     * Displays correct feedback and automatically
     * generates the next question.
     */
    private void showCorrect() {

        handler.removeCallbacks(checkAnswerRunnable);

        tvFeedback.setText(
                "Correct! 🎉"
        );

        tvFeedback.setTextColor(
                Color.rgb(46, 125, 50)
        );

        tvFeedback.setVisibility(
                View.VISIBLE
        );

        /*
         * Wait briefly so the user can see
         * the "Correct!" message.
         */
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isAdded() && getView() != null) {
                    generateQuestion();
                }

            }
        }, 800);
    }

    /**
     * Displays incorrect feedback.
     */
    private void showIncorrect() {

        tvFeedback.setText(
                "Incorrect, the answer is " + correctAnswer
        );

        tvFeedback.setTextColor(
                Color.rgb(198, 40, 40)
        );

        tvFeedback.setVisibility(
                View.VISIBLE
        );

        /*
         * Automatically generate a new question
         * after showing the correct answer.
         */
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isAdded() && getView() != null) {
                    generateQuestion();
                }

            }
        }, 1200);
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
                            (InputMethodManager)
                                    context.getSystemService(
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
     * Hides the keyboard.
     */
    private void hideKeyboard() {

        if (getContext() == null || etAnswer == null) {
            return;
        }

        InputMethodManager imm =
                (InputMethodManager)
                        getContext().getSystemService(
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

        handler.removeCallbacksAndMessages(null);

        super.onDestroyView();

        tilStartTable = null;
        tilEndTable = null;
        tilAnswer = null;

        etStartTable = null;
        etEndTable = null;
        etAnswer = null;

        tvQuestion = null;
        tvFeedback = null;
    }


}