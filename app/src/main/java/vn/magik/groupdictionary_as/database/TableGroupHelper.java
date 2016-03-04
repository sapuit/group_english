package vn.magik.groupdictionary_as.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import vn.magik.groupdictionary_as.entities.Group;

public class TableGroupHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME    = "GroupDictionary.db";
    private static final int    DATABASE_VERSION = 1;
    private static final String TABLE_GROUP      = "groups";
    private static final String GROUP_ID         = "id";
    private static final String GROUP_NAME       = "name";
    private static final String GROUP_IMAGE      = "image";
    private static final String GROUP_FILE       = "file";
    private static final String GROUP_FINISH     = "finish";


    public TableGroupHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE =
                "create table IF NOT EXISTS " + TABLE_GROUP + " (" +
                        GROUP_ID + " integer primary key," +
                        GROUP_NAME + " text," +
                        GROUP_IMAGE + " text," +
                        GROUP_FILE + " text," +
                        GROUP_FINISH + " text);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);
        onCreate(db);
    }

    public boolean insertGroup(Group group) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GROUP_ID, group.getId());
        contentValues.put(GROUP_NAME, group.getName());
        contentValues.put(GROUP_IMAGE, group.getImage());
        contentValues.put(GROUP_FILE, group.getFile());
        contentValues.put(GROUP_FINISH, group.getFinish());
        db.insert(TABLE_GROUP, null, contentValues);
        return true;
    }

    public Cursor getGroup(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(
                "select * from" + TABLE_GROUP +
                        " where " + GROUP_ID + "=" + id + "", null);
        res.moveToFirst();
        Group group = new Group();
        group.setId(res.getInt(res.getColumnIndex(GROUP_ID)));
        group.setName(res.getString(res.getColumnIndex(GROUP_NAME)));
        group.setImage(res.getString(res.getColumnIndex(GROUP_IMAGE)));
        group.setFile(res.getString(res.getColumnIndex(GROUP_FILE)));
        group.setFinish(res.getString(res.getColumnIndex(GROUP_FINISH)));
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_GROUP);
        return numRows;
    }

    public boolean updateGroup(Group group, int idOldGroup) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GROUP_ID, group.getId());
        contentValues.put(GROUP_NAME, group.getName());
        contentValues.put(GROUP_IMAGE, group.getImage());
        contentValues.put(GROUP_FILE, group.getFile());
        contentValues.put(GROUP_FINISH, group.getFinish());
        db.update(TABLE_GROUP, contentValues, GROUP_ID + " = ? ",
                new String[]{Integer.toString(idOldGroup)});
        return true;
    }

    public Integer deleteGroup(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_GROUP,
                GROUP_ID + " = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<Group> getAllGroup() {
        ArrayList<Group> group_list = new ArrayList<Group>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_GROUP, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Group group = new Group();
            group.setId(res.getInt(res.getColumnIndex(GROUP_ID)));
            group.setName(res.getString(res.getColumnIndex(GROUP_NAME)));
            group.setImage(res.getString(res.getColumnIndex(GROUP_IMAGE)));
            group.setFile(res.getString(res.getColumnIndex(GROUP_FILE)));
            group.setFinish(res.getString(res.getColumnIndex(GROUP_FINISH)));
            group_list.add(group);
            res.moveToNext();
        }
        return group_list;
    }

    public boolean groupFinish(int idOldGroup) {

        Log.i("idOldGroup", String.valueOf(idOldGroup));
        try {
            SQLiteDatabase dbread = this.getReadableDatabase();
            Cursor res = dbread.rawQuery("select * from " + TABLE_GROUP +
                    " where " + GROUP_ID + "=" + idOldGroup + "", null);

            Group group = new Group();
            res.moveToFirst();
            group.setId(res.getInt(res.getColumnIndex(GROUP_ID)));
            group.setName(res.getString(res.getColumnIndex(GROUP_NAME)));
            group.setImage(res.getString(res.getColumnIndex(GROUP_IMAGE)));
            group.setFile(res.getString(res.getColumnIndex(GROUP_FILE)));
            group.setFinish("true");

            return updateGroup(group, idOldGroup);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
