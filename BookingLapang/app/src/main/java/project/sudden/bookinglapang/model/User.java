package project.sudden.bookinglapang.model;

/**
 * Created by mac on 4/8/17.
 */
public class User {

    public String fullName;
    public String emailAddress;
    //public String userName;
    public String nomor;
    public String avatarLink;
    public long createdAt;

    public User() {}

    public User(String fullName, String emailAddress, String userName, String avatarLink, long createdAt) {
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.nomor = userName;
        this.avatarLink = avatarLink;
        this.createdAt = createdAt;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getNomor() {
        return nomor;
    }

    public String getAvatarLink() {
        return avatarLink;
    }

    public long getCreatedAt() {
        return createdAt;
    }

}
