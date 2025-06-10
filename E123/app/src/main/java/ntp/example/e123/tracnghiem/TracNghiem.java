package ntp.example.e123.tracnghiem;

public class TracNghiem {
    private String idCau;
    private String idBoHocTap;
    private String noiDung;
    private String dapAnA;
    private String dapAnB;
    private String dapAnC;
    private String dapAnD;
    private String dapAnTrue;

    public TracNghiem() {}

    public TracNghiem(String idCau, String idBoHocTap, String noiDung, String dapAnA, String dapAnB, String dapAnC, String dapAnD, String dapAnTrue) {
        this.idCau = idCau;
        this.idBoHocTap = idBoHocTap;
        this.noiDung = noiDung;
        this.dapAnA = dapAnA;
        this.dapAnB = dapAnB;
        this.dapAnC = dapAnC;
        this.dapAnD = dapAnD;
        this.dapAnTrue = dapAnTrue;
    }

    public String getIdCau() { return idCau; }
    public void setIdCau(String idCau) { this.idCau = idCau; }

    public String getIdBoHocTap() { return idBoHocTap; }
    public void setIdBoHocTap(String idBoHocTap) { this.idBoHocTap = idBoHocTap; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public String getDapAnA() { return dapAnA; }
    public void setDapAnA(String dapAnA) { this.dapAnA = dapAnA; }

    public String getDapAnB() { return dapAnB; }
    public void setDapAnB(String dapAnB) { this.dapAnB = dapAnB; }

    public String getDapAnC() { return dapAnC; }
    public void setDapAnC(String dapAnC) { this.dapAnC = dapAnC; }

    public String getDapAnD() { return dapAnD; }
    public void setDapAnD(String dapAnD) { this.dapAnD = dapAnD; }

    public String getDapAnTrue() { return dapAnTrue; }
    public void setDapAnTrue(String dapAnTrue) { this.dapAnTrue = dapAnTrue; }
}
