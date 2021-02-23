package vn.com.supertao.datting.model.object;

public class ContentChat {
    private String ID;
    private String Image;
    private String Content;
    private String Time;
    private Boolean currentUser;

    public ContentChat(String ID, String image, String content, String time, Boolean currentUser) {
        this.ID = ID;
        Image = image;
        Content = content;
        Time = time;
        this.currentUser = currentUser;
    }


    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public Boolean getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Boolean currentUser) {
        this.currentUser = currentUser;
    }
}
