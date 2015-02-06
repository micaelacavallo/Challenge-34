package com.example.micaelacavallo.contacts;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContactsFragment extends ListFragment {
    ArrayAdapter<Contact> mAdapter;
    DatabaseHelper mDBHelper = null;

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

        mAdapter = new ContactAdapter(getActivity(), getDBHelper(), contacts);
        setListAdapter(mAdapter);
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
