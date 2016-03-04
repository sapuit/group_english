package vn.magik.groupdictionary_as.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import vn.magik.groupdictionary_as.entities.Vocabulary;


public class TableVocaHelper {

    private static final String DATABASE_NAME  = "GroupDictionary.db";
    private static final String TABLE_VOCA     = "vocabulary";
    private static final String VOCA_ID        = "id";
    private static final String VOCA_GROUPID   = "groupid";
    private static final String VOCA_NAME      = "name";
    private static final String VOCA_IMAGE     = "image";
    private static final String VOCA_AUDIO     = "audio";
    private static final String VOCA_SENTENCE  = "sentence";
    private static final String VOCA_SPELL     = "spell";

    SQLiteDatabase db;
    int groupId;

    public TableVocaHelper(Context context, int groupId) {
        this.groupId = groupId;
        db = context.openOrCreateDatabase(DATABASE_NAME, context.MODE_PRIVATE, null);
        CreateTable();
    }


    public void CreateTable() {
        String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_VOCA + " (" +
                        VOCA_ID       + " integer primary key," +
                        VOCA_GROUPID  + " integer," +
                        VOCA_NAME     + " text," +
                        VOCA_AUDIO    + " text," +
                        VOCA_SENTENCE + " text," +
                        VOCA_IMAGE    + " text," +
                        VOCA_SPELL    + " text);";
        db.execSQL(CREATE_TABLE);
        Log.i("onCreate", "success");
    }

    public boolean insertVoca(Vocabulary voca) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(VOCA_ID,      voca.getId());
        contentValues.put(VOCA_GROUPID, voca.getGroup());
        contentValues.put(VOCA_NAME,    voca.getName());
        contentValues.put(VOCA_IMAGE,   voca.getImage());
        contentValues.put(VOCA_AUDIO,   voca.getAudio());
        contentValues.put(VOCA_SENTENCE,voca.getSentence());
        contentValues.put(VOCA_SPELL,   voca.getSpell());

        db.insert(TABLE_VOCA, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        Cursor res = db.rawQuery(
                "SELECT * FROM " + TABLE_VOCA +
                        " WHERE " + VOCA_ID + "=" + id + " " +
                        VOCA_GROUPID + "=" + groupId, null);
        return res;
    }

    public long numberOfRows() {
        long numRows = (int) DatabaseUtils.longForQuery(db,
                "SELECT COUNT(*) FROM " + TABLE_VOCA +
                        " WHERE " + VOCA_GROUPID + "=" + groupId, null);
        return numRows;
    }

    public boolean updateGroup(Vocabulary voca, int idOldVoca) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(VOCA_ID, voca.getId());
        contentValues.put(VOCA_GROUPID, voca.getGroup());
        contentValues.put(VOCA_NAME, voca.getName());
        contentValues.put(VOCA_IMAGE, voca.getImage());
        contentValues.put(VOCA_AUDIO, voca.getAudio());
        contentValues.put(VOCA_SENTENCE, voca.getSentence());
        contentValues.put(VOCA_SPELL, voca.getSpell());
        db.update(TABLE_VOCA, contentValues, VOCA_ID + " = ? ",
                new String[]{Integer.toString(idOldVoca)});
        return true;
    }

//    public Integer deleteVoca(Integer id) {
//        return db.delete(TABLE_VOCA,
//                VOCA_ID + " = ? ",
//                new String[]{Integer.toString(id)});
//    }

    public ArrayList<Vocabulary> getAllVoca() {
        ArrayList<Vocabulary> voca_list = new ArrayList<Vocabulary>();

        //hp = new HashMap();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_VOCA +
                " WHERE " + VOCA_GROUPID + "=" + groupId, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Vocabulary voca = new Vocabulary();
            voca.setId(res.getInt(res.getColumnIndex(VOCA_ID)));
            voca.setGroup(res.getInt(res.getColumnIndex(VOCA_GROUPID)));
            voca.setName(res.getString(res.getColumnIndex(VOCA_NAME)));
            voca.setImage(res.getString(res.getColumnIndex(VOCA_IMAGE)));
            voca.setAudio(res.getString(res.getColumnIndex(VOCA_AUDIO)));
            voca.setSentence(res.getString(res.getColumnIndex(VOCA_SENTENCE)));
            voca.setSpell(res.getString(res.getColumnIndex(VOCA_SPELL)));
            voca_list.add(voca);
            res.moveToNext();
        }
        return voca_list;
    }
}