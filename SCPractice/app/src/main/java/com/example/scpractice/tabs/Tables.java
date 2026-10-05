package com.example.scpractice.tabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.scpractice.MainActivity;
import com.example.scpractice.R;
import com.example.scpractice.Utils.MarathiUtils;
import com.example.scpractice.enums.Language;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
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

    private ActivityResultLauncher<Intent> speechRecognizerLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        speechRecognizerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            ArrayList<String> matches = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                            if (matches != null && !matches.isEmpty()) {
                                String spokenText = matches.get(0);
                                // Try to extract digits from spoken text
                                String numberOnly = spokenText.replaceAll("[^0-9]", "");
                                if (!numberOnly.isEmpty()) {
                                    etAnswer.setText(numberOnly);
                                    // TextWatcher will automatically check it
                                } else {
                                    Toast.makeText(getContext(), "Could not recognize a number: " + spokenText, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
        );
    }

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

        tilAnswer.setEndIconOnClickListener(v -> launchSpeechRecognizer());
    }

    private void launchSpeechRecognizer() {
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "mr-IN");
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak the answer...");
//        try {
//            speechRecognizerLauncher.launch(intent);
//        } catch (Exception e) {
//            Toast.makeText(getContext(), "Speech Recognition not available", Toast.LENGTH_SHORT).show();
//        }
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
        if (secondNumber < 5) secondNumber += 5;


        correctAnswer = firstNumber * secondNumber;
        String localMarathi = MarathiUtils.getPronunciation(Language.MARATHI, secondNumber);


        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).speakMessage(firstNumber + localMarathi, () -> {
                if (isAdded() && getContext() != null) {
                    launchSpeechRecognizer();
                }
            });
        }

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

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).speakMessage("Barobar");
        }

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

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).speakMessage("Chook");
        }

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

    private final Runnable checkAnswerRunnable = new Runnable() {
        @Override
        public void run() {
            checkAnswerAutomatically();
        }
    };


}