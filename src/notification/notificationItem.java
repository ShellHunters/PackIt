package notification;

import javafx.scene.image.Image;

public class notificationItem {
    private Image image_item;
    private String titel_text;
    private String description_text;
    private String duration_text;

    public notificationItem(String titel, String description, String duration, String image_url) {
        this.titel_text = titel;
        this.description_text = description;
        this.duration_text = duration;
        this.image_item = new Image(image_url, 30, 30, true, true);
    }

    public Image getImage_item() {
        return image_item;
    }

    public void setImage_item(Image image_item) {
        this.image_item = image_item;
    }

    public String getTitel_text() {
        return titel_text;
    }

    public void setTitel_text(String titel_text) {
        this.titel_text = titel_text;
    }

    public String getDescription_text() {
        return description_text;
    }

    public void setDescription_text(String description_text) {
        this.description_text = description_text;
    }

    public String getDuration_text() {
        return duration_text;
    }

    public void setDuration_text(String duration_text) {
        this.duration_text = duration_text;
    }
}
