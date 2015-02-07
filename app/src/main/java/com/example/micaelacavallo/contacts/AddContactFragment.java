package com.example.micaelacavallo.contacts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddContactFragment extends Fragment {
    ImageButton mImageButtonPicture;
    byte[] mPictureArray;
    Button mButtonDone;
    EditText mEditTextFirstName;
    EditText mEditTextLastName;
    EditText mEditTextNickname;
    Bitmap mPhoto;

    private static int REQUEST_IMAGE_CAPTURE = 1;

    public AddContactFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_contact, container, false);
        prepareEditTexts(rootView);
        prepareImageButton(rootView);
        prepareButtonDone(rootView);
        return rootView;
    }

    private void prepareButtonDone(View rootView) {
        mButtonDone = (Button)rootView.findViewById(R.id.button_done);
        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                Intent intent = putIntentExtras();
                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();
            }

            private Intent putIntentExtras() {
                Intent intent = new Intent ();
                intent.putExtra(Contact.FIRST_NAME, mEditTextFirstName.getText().toString());
                intent.putExtra(Contact.LAST_NAME, mEditTextLastName.getText().toString());
                intent.putExtra(Contact.NICKNAME, mEditTextNickname.getText().toString());
                convertBitmapImageToByteArray();
                intent.putExtra(Contact.PICTURE, mPictureArray);
                return intent;
            }
        });
    }

    private void prepareEditTexts(View rootView) {
        mEditTextFirstName = (EditText)rootView.findViewById(R.id.edit_text_first_name);
        mEditTextLastName = (EditText)rootView.findViewById(R.id.edit_text_last_name);
        mEditTextNickname = (EditText)rootView.findViewById(R.id.edit_text_nickname);

        TextWatcher listener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(mEditTextFirstName.getText().toString()) &&
                        !TextUtils.isEmpty(mEditTextLastName.getText().toString()))
                {
                    mButtonDone.setEnabled(true);
                }
                else
                {
                    mButtonDone.setEnabled(false);
                }
            }
        };

        mEditTextFirstName.addTextChangedListener(listener);
        mEditTextLastName.addTextChangedListener(listener);
    }

    private void prepareImageButton(View rootView) {
        mImageButtonPicture = (ImageButton) rootView.findViewById(R.id.image_button_picture);
        mPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.contact);
        mImageButtonPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        });
    }

    private void convertBitmapImageToByteArray() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mPhoto.compress(Bitmap.CompressFormat.PNG, 100, stream);
        mPictureArray = stream.toByteArray();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            mPhoto = (Bitmap) data.getExtras().get("data");
            mImageButtonPicture.setImageBitmap(mPhoto);
        }
    }
}
