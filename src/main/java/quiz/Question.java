package quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Question {

    private String question;
    private List<String> answers;
    private List<String> correctAnswers;

    public Question(String question, List<String> answers, List<String> correctAnswers) {
        this.question = question;
        this.answers = answers;
        this.correctAnswers = correctAnswers;
    }

    public static boolean isValid(List<String> columns) {
        if (columns.size() < 11) {
            return false;
        }
        return true;
    }

    public static Question fromColumns(List<String> columns) {
        String question = columns.get(0);
        List<String> answers = new ArrayList<>();
        List<String> correctAnswers = new ArrayList<>();
        for (int i = 1; i < columns.size(); i++) {
            String answer = columns.get(i);
            if (!answer.equals("")) {
                answers.add(answer);
                if (i < 9) {
                    correctAnswers.add(answer);
                }
            }
        }
        Collections.shuffle(answers);
        return new Question(question, answers, correctAnswers);
    }

    public boolean isCorrect(int answer) {
        return correctAnswers.contains(answers.get(answer));
    }

    public boolean areAllCorrectAnswersFound(List<String> selectedAnswers) {
        return correctAnswers.stream().allMatch(ca -> selectedAnswers.contains(ca));
    }

    public boolean areAllWrongAnswersFound(List<String> selectedAnswers) {
        return answers.stream().allMatch(a -> correctAnswers.contains(a) || selectedAnswers.contains(a));
    }

    public boolean oneCorrectAnswerRemaining(List<String> selectedAnswers) {
        return remainingCorrectAnswers(selectedAnswers).size() == 1;
    }

    public List<String> remainingCorrectAnswers(List<String> selectedAnswers) {
        return correctAnswers.stream().filter(ca -> !selectedAnswers.contains(ca)).collect(Collectors.toList());
    }

    public List<String> hint(List<String> selectedAnswers) {
        List<String> remainingCorrectAnswers = remainingCorrectAnswers(selectedAnswers);
        Collections.shuffle(remainingCorrectAnswers);
        List<String> remainingWrongAnswers = answers.stream()
                .filter(a -> !selectedAnswers.contains(a) && !correctAnswers.contains(a)).collect(Collectors.toList());
        Collections.shuffle(remainingWrongAnswers);
        List<String> result = remainingCorrectAnswers.subList(0, 2);
        result.add(remainingWrongAnswers.get(0));
        return result;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer(int index) {
        return answers.get(index);
    }

    public List<String> getAnswers() {
        return answers;
    }
}
