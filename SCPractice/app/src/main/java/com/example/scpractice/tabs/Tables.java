package com.example.scpractice.tabs;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
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
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
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
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecognizerIntent;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        setupSpeechRecognizer();
                        startSilentListening();
                    } else {
                        Toast.makeText(getContext(), "You can type your answer in the box instead.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void setupSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(requireContext())) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext());
            speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "mr-IN");
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    mainHandler.post(() -> {
                        tvFeedback.setText("Listening for your answer...");
                        tvFeedback.setTextColor(Color.GRAY);
                        tvFeedback.setVisibility(View.VISIBLE);
                    });
                }

                @Override
                public void onBeginningOfSpeech() {
                }

                @Override
                public void onRmsChanged(float rmsdB) {
                }

                @Override
                public void onBufferReceived(byte[] buffer) {
                }

                @Override
                public void onEndOfSpeech() {
                }

                @Override
                public void onError(int error) {
                    mainHandler.post(() -> {
                        if (tvFeedback != null && tvFeedback.getText().toString().startsWith("Listening")) {
                            tvFeedback.setVisibility(View.GONE);
                        }
                    });
                }

                @Override
                public void onResults(Bundle results) {
                    processSpeechResults(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
                }

                @Override
                public void onPartialResults(Bundle partialResults) {
                    ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (matches != null && !matches.isEmpty()) {
                        String partialText = matches.get(0);
                        mainHandler.post(() -> {
                            tvFeedback.setText("Listening... \"" + partialText + "\"");
                            tvFeedback.setTextColor(Color.GRAY);
                            tvFeedback.setVisibility(View.VISIBLE);
                        });
                        processSpeechResults(matches);
                    }
                }

                @Override
                public void onEvent(int eventType, Bundle params) {
                }
            });
        }
    }

    private void processSpeechResults(ArrayList<String> matches) {
        if (matches != null && !matches.isEmpty()) {
            String spokenText = matches.get(0);
            String numberOnly = spokenText.replaceAll("[^0-9]", "");
            if (!numberOnly.isEmpty()) {
                etAnswer.setText(numberOnly);
                // We stop listening once a number is found so it checks it immediately
                if (speechRecognizer != null) {
                    speechRecognizer.stopListening();
                }
            } else {
                mainHandler.post(() -> {
                    tvFeedback.setText("Listening... \"" + spokenText + "\"");
                    tvFeedback.setTextColor(Color.GRAY);
                    tvFeedback.setVisibility(View.VISIBLE);
                });
            }
        }
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

        tilAnswer.setEndIconOnClickListener(v -> startSilentListening());
    }

    private void startSilentListening() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Check if we should show a rationale dialog
            if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Microphone Permission")
                        .setMessage("If you would like to answer using your voice, please grant microphone permission. Otherwise, you can just type your answer in the text box.")
                        .setPositiveButton("Grant", (dialog, which) -> {
                            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
                        })
                        .setNegativeButton("No, I'll Type", (dialog, which) -> {
                            // User opted out, do nothing, they will type
                            dialog.dismiss();
                        })
                        .show();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
            }
            return;
        }

        if (speechRecognizer == null) {
            setupSpeechRecognizer();
        }

        if (speechRecognizer != null && speechRecognizerIntent != null) {
            mainHandler.post(() -> {
                speechRecognizer.startListening(speechRecognizerIntent);
            });
        }
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
                    startSilentListening();
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
        mainHandler.removeCallbacksAndMessages(null);

        if (speechRecognizer != null) {
            speechRecognizer.destroy();
            speechRecognizer = null;
        }

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