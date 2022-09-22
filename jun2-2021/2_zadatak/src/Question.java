public class Question {

    private String question_text;
    private String question_anwser;
    private int question_points;

    public Question(String question_text, String question_anwser, int question_points) {
        this.question_text = question_text;
        this.question_anwser = question_anwser;
        this.question_points = question_points;
    }

    @Override
    public String toString() {
        return this.question_text + " " + this.question_anwser + " " + this.question_points;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public String getQuestion_anwser() {
        return question_anwser;
    }

    public void setQuestion_anwser(String question_anwser) {
        this.question_anwser = question_anwser;
    }

    public int getQuestion_points() {
        return question_points;
    }

    public void setQuestion_points(int question_points) {
        this.question_points = question_points;
    }
}
