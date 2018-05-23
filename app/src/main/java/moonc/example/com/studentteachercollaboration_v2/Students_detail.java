package moonc.example.com.studentteachercollaboration_v2;

public class Students_detail {
   private String Key;
   private String Email_id_number;


   private String Name;
   private String Phone_number;
   private String ID,Password,Role;

    public Students_detail() {
    }

    public Students_detail(String key_, String email_, String name_, String phone_number_,String id,String password,String role) {
        this.Key = key_;
        this.Email_id_number = email_;
        this.Name = name_;
        this.Phone_number = phone_number_;
        this.ID = id;
        this.Password = password;
        this.Role = role;
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


    public String getEmail_id_number() {
        return Email_id_number;
    }

    public void setEmail_id_number(String email_id_number) {
        Email_id_number = email_id_number;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name_) {
        Name = name_;
    }

    public String getPhone_number() {
        return Phone_number;
    }

    public void setPhone_number(String phone_number_) {
        Phone_number = phone_number_;
    }
}
