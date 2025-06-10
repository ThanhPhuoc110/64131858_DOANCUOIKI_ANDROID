package ntp.example.e123.dienkhuyet;

import java.io.Serializable;

public class CauDienKhuyet implements Serializable {
    private String idCau;
    private String idBoHocTap;
    private String noiDung;
    private String dapAn;
    private String goiY;


    public CauDienKhuyet() {}

    public CauDienKhuyet(String idCau, String idBo, String noiDung, String dapAn, String goiY) {
        this.idCau = idCau;
        this.idBoHocTap = idBo;
        this.noiDung = noiDung;
        this.dapAn = dapAn;
        this.goiY = goiY;
    }

    public String getIdCau() { return idCau; }
    public void setIdCau(String idCau) { this.idCau = idCau; }

    public String getIdBo() { return idBoHocTap; }
    public void setIdBo(String idBo) { this.idBoHocTap = idBo; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public String getDapAn() { return dapAn; }
    public void setDapAn(String dapAn) { this.dapAn = dapAn; }

    public String getGoiY() { return goiY; }
    public void setGoiY(String goiY) { this.goiY = goiY; }
}
