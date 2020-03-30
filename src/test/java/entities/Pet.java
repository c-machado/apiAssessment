package entities;

import java.util.ArrayList;
import java.util.HashMap;

public class Pet {

    Integer id;
    HashMap<String, ?> category = new HashMap<>();
    String name = "";
    ArrayList<String> photoUrls = new ArrayList<String>();
    ArrayList<HashMap<String, Object>> tags= new ArrayList<>();
    String status = "";

    public Pet() {
    }

    public Pet(Integer id, int categoryId, String categoryName, String name, ArrayList<String> photoUrls, ArrayList<String> tags, String status) {
        this.id = id;
        HashMap<String, Object> petCategory = new HashMap<>();
        petCategory.put("id", categoryId);
        petCategory.put("name", categoryName);
        this.category = petCategory;
        this.name = name;
        this.photoUrls = photoUrls;
        this.tags = createTags(tags);
        this.status = status;
    }

    public ArrayList<HashMap<String, Object>> createTags(ArrayList<String> tags){
        ArrayList<HashMap<String, Object>> petTags = new ArrayList<>();
        for (int i = 0; i < tags.size(); i++) {
            HashMap<String, Object> petTag = new HashMap<>();
            petTag.put("id", i);
            petTag.put("name", tags.get(i));
            petTags.add(petTag);
        }
        return petTags;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public HashMap<String, ?> getCategory() {
        return category;
    }

    public void setCategory(HashMap<String, Object> category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(ArrayList<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public ArrayList<HashMap<String, Object>> getTags() {
        return tags;
    }

    public void setTags(ArrayList<HashMap<String, Object>> tags) {
        this.tags = tags;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
