package vn.magik.groupdictionary_as.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sapui on 12/30/2015.
 */
public class ResponseConfig {

    @SerializedName("error_code")
    private int errorCode;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private Config config;

    public ResponseConfig() {
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
