package laituan245.projects.koreangrammarhaja;

public class GrammarRecord {
    private int id;
    private String label;
    private String information;

    public GrammarRecord() {
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
