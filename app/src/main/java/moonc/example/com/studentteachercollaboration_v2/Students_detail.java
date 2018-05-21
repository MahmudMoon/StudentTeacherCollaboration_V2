package moonc.example.com.studentteachercollaboration_v2;

public class Students_detail {
    String Email;
    String Name;
    String Phone;
    String Key;


    public Students_detail() {

    }

    public Students_detail(String Key, String name, String email, String phone) {
        this.Key = Key;
        this.Email = email;
        this.Name = name;
        this.Phone = phone;
    }


    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
