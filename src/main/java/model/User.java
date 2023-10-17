package model;

public class User {
    private int id;
    public String name;
    public String surname;
    public String email;
    public String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    //? String token;
}
