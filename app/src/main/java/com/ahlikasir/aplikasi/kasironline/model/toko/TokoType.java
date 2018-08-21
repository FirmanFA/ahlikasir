package com.ahlikasir.aplikasi.kasironline.model.toko;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Firmansyah on 16/05/2018.
 */

public class TokoType {
    @SerializedName("status")
    @Expose
    public String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
