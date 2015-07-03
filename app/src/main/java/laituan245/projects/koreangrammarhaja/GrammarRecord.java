package laituan245.projects.koreangrammarhaja;

public class GrammarRecord {
    private int id;
    private String label;
    private String information;
    private String category;
    private String englishLabel;

    public GrammarRecord() {
    }

    public String getEnglishLabel() {
        return englishLabel;
    }

    public void setEnglishLabel(String englishLabel) {
        this.englishLabel = englishLabel;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getInformation() {
        return information;
    }
}
