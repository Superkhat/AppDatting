package vn.com.supertao.datting.model.object;

public class Chat {
    private String id;
    private String name;
    private String contentMessage;
    private String imageMessage;

    public Chat(String id, String name, String contentMessage, String imageMessage) {
        this.id = id;
        this.name = name;
        this.contentMessage = contentMessage;
        this.imageMessage = imageMessage;
    }

    public Chat(Object user) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentMessage() {
        return contentMessage;
    }

    public void setContentMessage(String contentMessage) {
        this.contentMessage = contentMessage;
    }

    public String getImageMessage() {
        return imageMessage;
    }

    public void setImageMessage(String imageMessage) {
        this.imageMessage = imageMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
