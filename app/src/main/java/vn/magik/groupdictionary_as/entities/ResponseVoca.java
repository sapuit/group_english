package vn.magik.groupdictionary_as.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sapui on 1/5/2016.
 */
public class ResponseVoca {

    @SerializedName("error_code")
    private int errorCode;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<Vocabulary> vocaList;

    public ResponseVoca(int errorCode, String message, List<Vocabulary> vocaList) {
        this.errorCode = errorCode;
        this.message = message;
        this.vocaList = vocaList;
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

    public List<Vocabulary> getVocaList() {
        return vocaList;
    }

    public void setVocaList(List<Vocabulary> vocaList) {
        this.vocaList = vocaList;
    }
}
