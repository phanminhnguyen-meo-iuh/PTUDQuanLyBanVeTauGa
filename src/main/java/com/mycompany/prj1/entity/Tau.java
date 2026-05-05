package com.mycompany.prj1.entity;

public class Tau {
    private String maTau;
    private Integer soLuongToa;
    private String trangThaiTau;

    public Tau() {
    }

    public Tau(String maTau, Integer soLuongToa, String trangThaiTau) {
        this.maTau = maTau;
        this.soLuongToa = soLuongToa;
        this.trangThaiTau = trangThaiTau;
    }

    public String getMaTau() {
        return maTau;
    }

    public void setMaTau(String maTau) {
        this.maTau = maTau;
    }

    public Integer getSoLuongToa() {
        return soLuongToa;
    }

    public void setSoLuongToa(Integer soLuongToa) {
        this.soLuongToa = soLuongToa;
    }

    public String getTrangThaiTau() {
        return trangThaiTau;
    }

    public void setTrangThaiTau(String trangThaiTau) {
        this.trangThaiTau = trangThaiTau;
    }
}