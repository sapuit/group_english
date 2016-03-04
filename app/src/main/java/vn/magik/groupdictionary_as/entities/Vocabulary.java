package vn.magik.groupdictionary_as.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sapui on 12/30/2015.
 */
public class Vocabulary implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("group")
    private int group;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String image;
    @SerializedName("audio")
    private String audio;
    @SerializedName("sentence")
    private String sentence;
    @SerializedName("spell")
    private String spell;

    public Vocabulary() {
    }

    public Vocabulary(int id, int group, String name, String image, String audio, String sentence) {
        this.id = id;
        this.group = group;
        this.name = name.trim();
        this.image = image.trim();
        this.audio = audio;
        this.sentence = sentence.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image =image.trim();
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }
}
