package marsmadoka98.gmail;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter {
    private String title;
    private String description;
    private String image; //make sure they match those in your database or else it wont work
public RecyclerAdapter(){

}

    public RecyclerAdapter(String title, String description, String image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
