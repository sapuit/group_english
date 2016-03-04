package vn.magik.groupdictionary_as.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sapui on 2/6/2016.
 */
public class Config {

    @SerializedName("version_code")
    private int version_code;
    @SerializedName("link")
    private String link;
    @SerializedName("acti_ads")
    private int acti_ads;
    @SerializedName("number_group")
    private int number_group;

    public Config() {
    }

    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getActi_ads() {
        return acti_ads;
    }

    public void setActi_ads(int acti_ads) {
        this.acti_ads = acti_ads;
    }

    public int getNumber_group() {
        return number_group;
    }

    public void setNumber_group(int number_group) {
        this.number_group = number_group;
    }
}
