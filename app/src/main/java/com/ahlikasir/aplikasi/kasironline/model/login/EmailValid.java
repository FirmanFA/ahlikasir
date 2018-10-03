package com.ahlikasir.aplikasi.kasironline.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Firmansyah on 29/09/2018.
 */

public class EmailValid {

    @SerializedName("data")
    @Expose
    public Data data;

    public EmailValid(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
