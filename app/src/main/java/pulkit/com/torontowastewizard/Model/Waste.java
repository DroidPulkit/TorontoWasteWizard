package pulkit.com.torontowastewizard.Model;

import java.io.Serializable;

public class Waste implements Serializable {
    private String body;
    private String category;
    private String title;
    private String keywords;

    public Waste(String body, String category, String title, String keywords) {
        this.body = body;
        this.category = category;
        this.title = title;
        this.keywords = keywords;
    }

    public Waste(){

    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
