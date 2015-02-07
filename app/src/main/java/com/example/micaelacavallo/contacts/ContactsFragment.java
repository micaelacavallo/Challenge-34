package com.example.micaelacavallo.contacts;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContactsFragment extends ListFragment {
    ContactAdapter mAdapter;
    DatabaseHelper mDBHelper = null;
    public static final String ID = "id";
    private static final Integer REQUEST_CODE = 0;

    public ContactsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        return rootView;
    }

    public DatabaseHelper getDBHelper() {
        if (mDBHelper == null){
            mDBHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
        return mDBHelper;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add_contact, menu);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<Contact> contacts;
        try {
            contacts = getDBHelper().getContactDao().queryForAll();
        } catch (SQLException e) {
            contacts = new ArrayList<>();
            e.printStackTrace();
        }
        prepareListView(contacts);
    }

    private void prepareListView(List<Contact> contacts) {
        mAdapter = new ContactAdapter(getActivity(), contacts);
        setListAdapter(mAdapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent (getActivity(), EditContactActivity.class);
                intent.putExtra(ID, position);
                startActivity(intent);

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE)
        {
            if (resultCode == getActivity().RESULT_OK)
            {
                String firstname = data.getStringExtra(Contact.FIRST_NAME);
                String lastname = data.getStringExtra(Contact.LAST_NAME);
                String nickname = data.getStringExtra(Contact.NICKNAME);
                byte[] image = data.getByteArrayExtra(Contact.PICTURE);
                Contact contact = getContact(firstname, lastname, nickname, image);
                mAdapter.add(contact);
                saveContact(contact);
            }
        }
    }

    private Contact getContact(String firstname, String lastname, String nickname, byte[] image) {
        Contact contact = new Contact();
        contact.setFirstName(firstname);
        contact.setLastName(lastname);
        contact.setNickName(nickname);
        contact.setImage(image);
        return contact;
    }

    private void saveContact(Contact contact) {
        try {
            Dao<Contact,Integer> dao = getDBHelper().getContactDao();
            dao.create(contact);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        if (mDBHelper!=null){
            OpenHelperManager.releaseHelper();
            mDBHelper = null;
        }
        super.onDestroy();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Boolean handled = false;

        switch (id){
            case R.id.action_add_contact:
                Intent intent = new Intent(getActivity(), AddContactActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                handled = true;
                break;
        }

        if (!handled){
            handled = super.onOptionsItemSelected(item);
        }
        return handled;

    }
}
