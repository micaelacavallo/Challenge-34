package com.example.micaelacavallo.contacts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * A placeholder fragment containing a simple view.
 */
public class EditContactFragment extends Fragment {
    DatabaseHelper mDBHelper = null;
    public EditContactFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_contact, container, false);
        int id = 0;
        id = getActivity().getIntent().getIntExtra(ContactsFragment.ID, id);
        Dao<Contact,Integer> dao;
        try {
            dao = getDBHelper().getContactDao();
            Contact contact = dao.queryForId(id+1);
            if (contact != null) {
                Toast.makeText(getActivity(), contact.getFirstName().toString() , Toast.LENGTH_LONG).show();
            }
        } catch (SQLException e) {
           e.printStackTrace();
        }
        return rootView;
    }

    public DatabaseHelper getDBHelper() {
        if (mDBHelper == null){
            mDBHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
        return mDBHelper;
    }

    @Override
    public void onDestroy() {
        if (mDBHelper!=null){
            OpenHelperManager.releaseHelper();
            mDBHelper = null;
        }
        super.onDestroy();
    }
}
