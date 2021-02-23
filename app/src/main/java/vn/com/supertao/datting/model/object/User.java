package vn.com.supertao.datting.model.object;

public class User {
    private String Id;
    private String name;
    private String dateOfBirth;
    private long year;
    private String phoneNumber;
    private String sex;
    private String hbbies;
    private String chamNgonSong;
    private String email;
    private String passWord;
    private String image1;
    private String image2;
    private String image3;
    private String image4;

    public User(String id, String name, String dateOfBirth, long year, String phoneNumber, String sex
            , String hbbies, String chamNgonSong, String email, String passWord, String image1
            , String image2, String image3, String image4) {
        Id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.year = year;
        this.phoneNumber = phoneNumber;
        this.sex = sex;
        this.hbbies = hbbies;
        this.chamNgonSong = chamNgonSong;
        this.email = email;
        this.passWord = passWord;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHbbies() {
        return hbbies;
    }

    public void setHbbies(String hbbies) {
        this.hbbies = hbbies;
    }

    public String getChamNgonSong() {
        return chamNgonSong;
    }

    public void setChamNgonSong(String chamNgonSong) {
        this.chamNgonSong = chamNgonSong;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }
}
