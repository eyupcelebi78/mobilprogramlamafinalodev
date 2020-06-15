package com.example.finalprojeodevi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerLoginResponse {

    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("token")
    @Expose
    public String token;
}
