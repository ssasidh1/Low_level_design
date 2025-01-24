import java.util.*;
import java.util.stream.Collectors;

// Question Status Enum
public enum QuestionStatus {
    OPEN,
    CLOSED,
    ON_HOLD,
    DELETED
}

// Question Closing Remark Enum
public enum QuestionClosingRemark {
    DUPLICATE,
    OFF_TOPIC,
    TOO_BROAD,
    NOT_CONSTRUCTIVE,
    NOT_A_REAL_QUESTION,
    PRIMARILY_OPINION_BASED
}

// Account Status Enum
public enum AccountStatus {
    ACTIVE,
    CLOSED,
    CANCELED,
    BLACKLISTED,
    BLOCKED
}

// Address Class
public class Address {
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    // Constructor
    public Address(String streetAddress, String city, String state, String zipCode, String country) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }

    // Getters and setters (Assumed private attributes with public getters/setters)
    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
}

// Account Class
public class Account {
    private String id;
    private String password;
    private AccountStatus status;
    private String name;
    private Address address;
    private String email;
    private String phone;
    private int reputation;

    // Constructor
    public Account(String id, String password, AccountStatus status, String name, Address address, String email, String phone, int reputation) {
        this.id = id;
        this.password = password;
        this.status = status;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.reputation = reputation;
    }

    // Method to reset password
    public boolean resetPassword() {
        // Logic to reset password
        return true;
    }

    // Getter and Setter methods omitted for brevity
}

// Badge Class
public class Badge {
    private String name;
    private String description;

    public Badge(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

// Tag Class
public class Tag {
    private String name;
    private String description;
    private long dailyAskedFrequency;
    private long weeklyAskedFrequency;

    public Tag(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

// Notification Class (Observer Pattern)
public interface Notification {
    boolean sendNotification();
}

public class EmailNotification implements Notification {
    private String content;

    public EmailNotification(String content) {
        this.content = content;
    }

    @Override
    public boolean sendNotification() {
        // Send email
        System.out.println("Sending Email: " + content);
        return true;
    }
}

public class SMSNotification implements Notification {
    private String content;

    public SMSNotification(String content) {
        this.content = content;
    }

    @Override
    public boolean sendNotification() {
        // Send SMS
        System.out.println("Sending SMS: " + content);
        return true;
    }
}

// Notification Factory (Factory Pattern)
public class NotificationFactory {
    public static Notification createNotification(String type, String content) {
        if (type.equalsIgnoreCase("email")) {
            return new EmailNotification(content);
        } else if (type.equalsIgnoreCase("sms")) {
            return new SMSNotification(content);
        }
        return null;
    }
}

// Member Class (Base Class)
public class Member {
    private Account account;
    private List<Badge> badges;

    public Member(Account account) {
        this.account = account;
        this.badges = new ArrayList<>();
    }

    public int getReputation() {
        return account.reputation;
    }

    public String getEmail() {
        return account.email;
    }

    public boolean createQuestion(Question question) {
        // Logic for creating a question
        return true;
    }

    public boolean createTag(Tag tag) {
        // Logic for creating a tag
        return true;
    }
}

// Admin Class (Extends Member)
public class Admin extends Member {
    public Admin(Account account) {
        super(account);
    }

    public boolean blockMember(Member member) {
        // Block member logic
        return true;
    }

    public boolean unblockMember(Member member) {
        // Unblock member logic
        return true;
    }
}

// Moderator Class (Extends Member)
public class Moderator extends Member {
    public Moderator(Account account) {
        super(account);
    }

    public boolean closeQuestion(Question question) {
        question.close();
        return true;
    }

    public boolean undeleteQuestion(Question question) {
        question.undelete();
        return true;
    }
}

// Comment Class
public class Comment {
    private String text;
    private Date creationTime;
    private int flagCount;
    private int voteCount;

    private Member askingMember;

    public Comment(String text, Member askingMember) {
        this.text = text;
        this.askingMember = askingMember;
        this.creationTime = new Date();
        this.flagCount = 0;
        this.voteCount = 0;
    }

    public boolean incrementVoteCount() {
        this.voteCount++;
        return true;
    }
}

// Question Class
public class Question {
    private String title;
    private String description;
    private int viewCount;
    private int voteCount;
    private Date creationTime;
    private Date updateTime;
    private QuestionStatus status;
    private QuestionClosingRemark closingRemark;

    private Member askingMember;
    private List<Photo> photos;
    private List<Comment> comments;
    private List<Answer> answers;

    public Question(String title, String description, Member askingMember) {
        this.title = title;
        this.description = description;
        this.askingMember = askingMember;
        this.photos = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.answers = new ArrayList<>();
    }

    public boolean close() {
        this.status = QuestionStatus.CLOSED;
        return true;
    }

    public boolean undelete() {
        this.status = QuestionStatus.OPEN;
        return true;
    }

    public boolean addComment(Comment comment) {
        comments.add(comment);
        return true;
    }

    public boolean addBounty(Bounty bounty) {
        // Add bounty logic
        return true;
    }

    public static List<Question> search(String query) {
        // Search logic to find questions based on the query
        return new ArrayList<>();
    }
}

// Answer Class
public class Answer {
    private String answerText;
    private boolean accepted;
    private int voteCount;
    private int flagCount;
    private Date creationTime;

    private Member creatingMember;
    private List<Photo> photos;

    public Answer(String answerText, Member creatingMember) {
        this.answerText = answerText;
        this.creatingMember = creatingMember;
        this.creationTime = new Date();
        this.voteCount = 0;
        this.flagCount = 0;
    }

    public boolean incrementVoteCount() {
        this.voteCount++;
        return true;
    }
}

// Photo Class
public class Photo {
    private int photoId;
    private String photoPath;
    private Date creationDate;
    private Member creatingMember;

    public Photo(int photoId, String photoPath, Member creatingMember) {
        this.photoId = photoId;
