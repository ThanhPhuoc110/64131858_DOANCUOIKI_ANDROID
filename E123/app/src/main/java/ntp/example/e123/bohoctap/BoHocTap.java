package ntp.example.e123.bohoctap;


import java.io.Serializable;

public class BoHocTap implements Serializable {
    private String idBo;
    private int stt;
    private String TenBo;
    public BoHocTap() {}


    public BoHocTap(String idBo, int stt, String tenBo) {
        this.idBo = idBo;
        this.stt = stt;
        this.TenBo = tenBo;
    }

    public String getIdBo() {
        return idBo;
    }

    public void setIdBo(String idBo) {
        this.idBo = idBo;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getTenBo() {
        return TenBo;
    }

    public void setTenBo(String tenBo) {
        TenBo = tenBo;
    }
}

