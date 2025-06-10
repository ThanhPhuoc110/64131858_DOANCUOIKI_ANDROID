package ntp.example.e123.taikhoan;

public class User {
    private String idUser;
    private String hoTen;
    private int Point;
    private String Email;
    private String SDT;
    private int role;

    public User() {
    }

    public User(String idUser, String hoTen, int point, String email, String SDT, int role) {
        this.idUser = idUser;
        this.hoTen = hoTen;
        Point = point;
        Email = email;
        this.SDT = SDT;
        this.role = role;
    }

    public String getIduser() {
        return idUser;
    }

    public void setIduser(String idUser) {
        this.idUser = idUser;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public int getPoint() {
        return Point;
    }

    public void setPoint(int point) {
        Point = point;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
