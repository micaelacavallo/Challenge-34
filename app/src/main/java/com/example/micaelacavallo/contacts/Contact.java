package com.example.micaelacavallo.contacts;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by micaela.cavallo on 05/02/2015.
 */
public class Contact {

    public final static String ID = "id";
    public final static String FIRST_NAME = "first_name";
    public final static String LAST_NAME = "last_name";
    public final static String NICKNAME = "nickname";
    public final static String PICTURE = "picture";

    @DatabaseField(generatedId = true, columnName = ID) private int id;
    @DatabaseField (columnName = FIRST_NAME) private String mFirstName;
    @DatabaseField (columnName = LAST_NAME) private String mLastName;
    @DatabaseField (columnName = NICKNAME) private String mNickName;
    @DatabaseField(columnName = PICTURE, dataType = DataType.BYTE_ARRAY) private byte[] mImage;

    public Contact() {
    }

    public Contact(String mFirstName, String mLastName, String mNickName, byte[] mImage) {
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mNickName = mNickName;
        this.mImage = mImage;
    }

    public byte[] getImage() {
        return mImage;
    }

    public void setImage(byte[] mImage) {
        this.mImage = mImage;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getNickName() {
        return mNickName;
    }

    public void setNickName(String mNickName) {
        this.mNickName = mNickName;
    }
}
