package laituan245.projects.koreangrammarhaja;

/**
 * Created by Tuan Lai on 6/19/2015.
 */
public class ConfusingGrammarArticle {
    private int id;
    private String title;
    private String content;
    private String difficulty_level;

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDifficulty_level(String difficulty_level) {
        this.difficulty_level = difficulty_level;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDifficulty_level() {
        return difficulty_level;
    }
}
