package vn.com.supertao.datting.model.object;

public class Notification {
    private String NameFriend;
    private String ImageFriend;
    private String Time;
    private String Content;
    private int tempCheck;
    private String id;

    public Notification(String nameFriend, String imageFriend, String time, String content, int tempCheck, String id) {
        NameFriend = nameFriend;
        ImageFriend = imageFriend;
        Time = time;
        Content = content;
        this.tempCheck = tempCheck;
        this.id = id;
    }

    public String getNameFriend() {
        return NameFriend;
    }

    public void setNameFriend(String nameFriend) {
        NameFriend = nameFriend;
    }

    public String getImageFriend() {
        return ImageFriend;
    }

    public void setImageFriend(String imageFriend) {
        ImageFriend = imageFriend;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getTempCheck() {
        return tempCheck;
    }

    public void setTempCheck(int tempCheck) {
        this.tempCheck = tempCheck;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
