package vn.com.supertao.datting.model.object;

public class Diary {
    private String ImageUserDiay;
    private String nameUserDiary;
    private String ContentDiary;
    private String ImageCentenDiary;
    private String CommentDiary;
    private String Date;

    public Diary(String imageUserDiay, String nameUserDiary, String contentDiary, String imageCentenDiary, String commentDiary,String Date) {
        ImageUserDiay = imageUserDiay;
        this.nameUserDiary = nameUserDiary;
        ContentDiary = contentDiary;
        ImageCentenDiary = imageCentenDiary;
        CommentDiary = commentDiary;
        this.Date=Date;
    }

    public String getImageUserDiay() {
        return ImageUserDiay;
    }

    public void setImageUserDiay(String imageUserDiay) {
        ImageUserDiay = imageUserDiay;
    }

    public String getNameUserDiary() {
        return nameUserDiary;
    }

    public void setNameUserDiary(String nameUserDiary) {
        this.nameUserDiary = nameUserDiary;
    }

    public String getContentDiary() {
        return ContentDiary;
    }

    public void setContentDiary(String contentDiary) {
        ContentDiary = contentDiary;
    }

    public String getImageCentenDiary() {
        return ImageCentenDiary;
    }

    public void setImageCentenDiary(String imageCentenDiary) {
        ImageCentenDiary = imageCentenDiary;
    }

    public String getCommentDiary() {
        return CommentDiary;
    }

    public void setCommentDiary(String commentDiary) {
        CommentDiary = commentDiary;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
