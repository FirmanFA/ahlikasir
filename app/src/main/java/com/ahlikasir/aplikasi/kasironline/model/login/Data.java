package com.ahlikasir.aplikasi.kasironline.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Firmansyah on 29/09/2018.
 */

public class Data implements Serializable {
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("user")
    @Expose
    public String user;
    @SerializedName("domain")
    @Expose
    public String domain;
    @SerializedName("tld")
    @Expose
    public String tld;
    @SerializedName("sub")
    @Expose
    public Boolean sub;
    @SerializedName("role")
    @Expose
    public Boolean role;
    @SerializedName("catchall")
    @Expose
    public Boolean catchall;
    @SerializedName("disposable")
    @Expose
    public Boolean disposable;
    @SerializedName("status")
    @Expose
    public Boolean status;

    public Data(String email, String user, String domain, String tld, Boolean sub, Boolean role, Boolean catchall, Boolean disposable, Boolean status) {
        this.email = email;
        this.user = user;
        this.domain = domain;
        this.tld = tld;
        this.sub = sub;
        this.role = role;
        this.catchall = catchall;
        this.disposable = disposable;
        this.status = status;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTld() {
        return tld;
    }

    public void setTld(String tld) {
        this.tld = tld;
    }

    public Boolean getSub() {
        return sub;
    }

    public void setSub(Boolean sub) {
        this.sub = sub;
    }

    public Boolean getRole() {
        return role;
    }

    public void setRole(Boolean role) {
        this.role = role;
    }

    public Boolean getCatchall() {
        return catchall;
    }

    public void setCatchall(Boolean catchall) {
        this.catchall = catchall;
    }

    public Boolean getDisposable() {
        return disposable;
    }

    public void setDisposable(Boolean disposable) {
        this.disposable = disposable;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
