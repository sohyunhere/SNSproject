package dduwcom.mobile.simple_sns;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostInfo implements Serializable {
    private String title;
    private ArrayList<String> contents;
    private ArrayList<String> formats;
    private String publisher;
    private Date createdAt;
    private String id;

    public PostInfo(String title, ArrayList<String> contents, ArrayList<String> formats, String publisher, Date createdAt){
        this.title = title;
        this.contents = contents;
        this.formats = formats;
        this.publisher = publisher;
        this.createdAt = createdAt;
    }
    public PostInfo(String title, ArrayList<String> contents, ArrayList<String> formats, String publisher, Date createdAt, String id){
        this.title = title;
        this.contents = contents;
        this.formats = formats;
        this.publisher = publisher;
        this.createdAt = createdAt;
        this.id = id;
    }

    public Map<String, Object> getPostInfo(){
        Map<String, Object> docDate = new HashMap<>();
        docDate.put("title", title);
        docDate.put("contents", contents);
        docDate.put("formats", formats);
        docDate.put("publisher", publisher);
        docDate.put("createdAt", createdAt);
        return docDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getContents() {
        return contents;
    }

    public void setContents(ArrayList<String> contents) {
        this.contents = contents;
    }
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public ArrayList<String> getFormats() {
        return formats;
    }

    public void setFormats(ArrayList<String> formats) {
        this.formats = formats;
    }
}
