package vn.magik.groupdictionary_as.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sapui on 12/30/2015.
 */
public class ResponseGroup {

    @SerializedName("error_code")
    private int errorCode;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<Group> groupList;

    public ResponseGroup(int err_code, String message, List<Group> groupList) {
        this.errorCode = err_code;
        this.message = message;
        this.groupList = groupList;
    }

    public ResponseGroup() {
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int err_code) {
        this.errorCode = err_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }
}
