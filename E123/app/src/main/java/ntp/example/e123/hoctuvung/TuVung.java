package ntp.example.e123.hoctuvung;

import java.io.Serializable;

public class TuVung implements Serializable {
    private String idTu;
    private String idBoHocTap;
    private String Tu;
    private String dichnghia;
    private String loaitu;
    private String imageUrl;


    public TuVung() {}


    public TuVung(String idTu, String Tu, String dichnghia, String loaitu, String imageUrl, String idBoHocTap) {
        this.idTu = idTu;
        this.Tu = Tu;
        this.dichnghia = dichnghia;
        this.loaitu = loaitu;
        this.imageUrl = imageUrl;
        this.idBoHocTap = idBoHocTap;
    }

    public String getIdTu() { return idTu; }
    public void setIdTu(String idTu) { this.idTu = idTu; }

    public String getIdBoHocTap() { return idBoHocTap; }
    public void setIdBoHocTap(String idBoHocTap) { this.idBoHocTap = idBoHocTap; }

    public String getTu() { return Tu; }
    public void setTu(String Tu) { this.Tu = Tu; }

    public String getDichnghia() { return dichnghia; }
    public void setDichnghia(String dichnghia) { this.dichnghia = dichnghia; }

    public String getLoaitu() { return loaitu; }
    public void setLoaitu(String loaitu) { this.loaitu = loaitu; }


    public String getAnh() { return imageUrl; }
    public void setAnh(String imageUrl) { this.imageUrl = imageUrl; }
}
