package com.example.dbex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dbex.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class MyDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ContactsDB";
    private static final int DATABASE_VERSION = 1;

    // 1. You MUST assign text values to these variables!
    private static final String TABLE = "contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE_NO = "phone_number";

    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 2. Now when this runs, the variables will inject "id", "name", etc.
        db.execSQL("CREATE TABLE " + TABLE + "( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAME + " TEXT, "
                + KEY_PHONE_NO + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public void addContact(Contact contact){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_NAME,contact.getName());
        values.put(KEY_PHONE_NO,contact.getPhoneNo());

        db.insert(TABLE,null,values);

        db.close();
    }


    public List<Contact> fetchContacts(){
        SQLiteDatabase db= this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE,null);

        List<Contact>contacts=new ArrayList<>();

        while (cursor.moveToNext()){
            Contact contact=new Contact();
            contact.setId(cursor.getInt(0));
            contact.setName(cursor.getString(1));
            contact.setPhoneNo(cursor.getString(2));
            contacts.add(contact);
        }

        db.close();



        return contacts;
    }


    public void update(Contact contact){
        SQLiteDatabase db=getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        if(contact.getName()!=null){
            contentValues.put(KEY_NAME,contact.getName());
        }

        if(contact.getPhoneNo()!=null){
            contentValues.put(KEY_PHONE_NO,contact.getPhoneNo());
        }

        db.update(TABLE,contentValues,KEY_ID+" = "+contact.getId(),null);
        db.close();
    }


    public void deleteById(int id){
        SQLiteDatabase db=getWritableDatabase();
        String ids[]=new String[1];
        ids[0]=String.valueOf(id);
        db.delete(TABLE,KEY_ID+" = ? ",ids);
    }





}
