package com.example.micaelacavallo.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    Contact mContactToEdit;
    public static final String ID = "id";
    private static final Integer ADD_CONTACT = 0;
    private static final Integer EDIT_CONTACT = 1;

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
        contacts = getContactsFromBD();
        prepareListView(contacts);
    }

    private List<Contact> getContactsFromBD() {
        List<Contact> contacts;
        try {
            contacts = getDBHelper().getContactDao().queryForAll();
        } catch (SQLException e) {
            contacts = new ArrayList<>();
            e.printStackTrace();
        }
        return contacts;
    }

    private void prepareListView(List<Contact> contacts) {
        mAdapter = new ContactAdapter(getActivity(), contacts);
        setListAdapter(mAdapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent (getActivity(), AddContactActivity.class);
                mContactToEdit = mAdapter.getItem(position);
                intent.putExtra(ID,mAdapter.getItem(position).getId());
                startActivityForResult(intent, EDIT_CONTACT);
           }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK)
        {
            String firstname = data.getStringExtra(Contact.FIRST_NAME);
            String lastname = data.getStringExtra(Contact.LAST_NAME);
            String nickname = data.getStringExtra(Contact.NICKNAME);
            byte[] image = data.getByteArrayExtra(Contact.PICTURE);
            //int id = 0;
            //id = data.getIntExtra(Contact.ID, id);

            String message = "";
            if (requestCode == ADD_CONTACT)
            {
                Contact contact = getContact(firstname, lastname, nickname, image);
                saveContact(contact);
                mAdapter.add(contact);
               message = "The contact has been created correctly";
            }
            else
            {
                if (data.getBooleanExtra(AddContactFragment.DELETE_CONTACT, true)) {
                    deleteContact(mContactToEdit);
                    message = "The contact has been deleted correctly";

                }
                else {
                    mContactToEdit.setFirstName(firstname);
                    mContactToEdit.setLastName(lastname);
                    mContactToEdit.setNickName(nickname);
                    mContactToEdit.setImage(image);
                    editContact(mContactToEdit);
                    message = "The contact has been modified correctly";
                }
            }
            mAdapter.clear();
            List<Contact> contacts = getContactsFromBD();
            mAdapter = new ContactAdapter(getActivity(), contacts);
            setListAdapter(mAdapter);
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
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

    private void editContact (Contact contact)
    {
        try {
            Dao<Contact,Integer> dao = getDBHelper().getContactDao();
            dao.update(contact);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveContact(Contact contact) {
        try {
            Dao<Contact,Integer> dao = getDBHelper().getContactDao();
            dao.create(contact);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteContact(Contact contact) {
        try {
            Dao<Contact,Integer> dao = getDBHelper().getContactDao();
            dao.delete(contact);
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
                startActivityForResult(intent, ADD_CONTACT);
                handled = true;
                break;
        }

        if (!handled){
            handled = super.onOptionsItemSelected(item);
        }
        return handled;

    }
}
