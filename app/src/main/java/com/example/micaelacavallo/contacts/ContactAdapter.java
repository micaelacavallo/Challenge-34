package com.example.micaelacavallo.contacts;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by micaela.cavallo on 05/02/2015.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {

    List<Contact> mContact;
    DatabaseHelper mDBHelper;
    TextView mTextViewName;
    TextView mTextViewNickname;
    ImageView mImageViewPicture;

    public ContactAdapter(Context context, DatabaseHelper dbHelper, List<Contact> contact) {
        super(context, R.layout.list_item_entry, contact);
        mContact = contact;
        mDBHelper = dbHelper;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        rowView = reuseOrGenerateRowView(convertView, parent);
        displayContentInView(position, rowView);
        return rowView;
    }

    private View reuseOrGenerateRowView(View convertView, ViewGroup parent) {
        View rowView;
        if (convertView != null) {
            rowView = convertView;
        } else {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_item_entry, parent, false);
        }
        return rowView;
    }

    private void displayContentInView(final int position, View rowView) {
        if (rowView != null) {
            prepareViews(rowView);
            mTextViewName.setText(mContact.get(position).getFirstName() + " " + mContact.get(position).getLastName());
            mTextViewNickname.setText(mContact.get(position).getNickName());
            Bitmap bmp = getBitmap(position);
            mImageViewPicture.setImageBitmap(bmp);
        }
    }


    private Bitmap getBitmap(int position) {
        Bitmap bmp;
        byte[] image;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        image = mContact.get(position).getImage();
        bmp = BitmapFactory.decodeByteArray(image, 0, image.length, options);
        return bmp;
    }


    private void prepareViews(View rowView) {
        mTextViewName = (TextView) rowView.findViewById(R.id.text_view_contact_name);
        mTextViewNickname = (TextView) rowView.findViewById(R.id.text_view_contact_nickname);
        mImageViewPicture = (ImageView) rowView.findViewById(R.id.image_view_contact_picture);
    }
}
