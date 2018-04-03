package net.kaparray.velp.classes_for_data;



public class UserLoader {

    private String age;
    private String nameUser;
    private String city;
    private String email;
    private String phone;


    public UserLoader(String age, String name, String city, String email, String phone) {

        this.age = age;
        this.nameUser = name;
        this.city = city;
        this.email = email;
        this.phone = phone;
    }

    public UserLoader() {
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
