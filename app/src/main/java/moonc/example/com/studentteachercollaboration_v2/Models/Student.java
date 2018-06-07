package moonc.example.com.studentteachercollaboration_v2.Models;

public class Student {
   private String Key;
   private String EmailIDNumber;
   private String Name;
   private String PhoneNumber;
   private String ID,Password,Role;
   private String Session;

    public Student() {
    }

    public Student(String key_, String email_, String name_, String phone_number_, String id, String password, String role, String session) {
        this.Key = key_;
        this.EmailIDNumber = email_;
        this.Name = name_;
        this.PhoneNumber = phone_number_;
        this.ID = id;
        this.Password = password;
        this.Role = role;
        this.Session = session;
    }

    public String getSession() {
        return Session;
    }

    public void setSession(String session) {
        Session = session;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key_) {
        Key = key_;
    }

    public String getEmailIDNumber() {
        return EmailIDNumber;
    }

    public void setEmailIDNumber(String emailIDNumber) {
        EmailIDNumber = emailIDNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name_) {
        Name = name_;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phone_number_) {
        PhoneNumber = phone_number_;
    }
}
