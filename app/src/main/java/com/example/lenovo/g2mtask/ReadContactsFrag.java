package com.example.lenovo.g2mtask;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


public class ReadContactsFrag extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public ReadContactsFrag() {
    }
private ArrayList<Contact> contacts;
private  String [] mProjection=new String[]{
 ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
        ContactsContract.Contacts._ID,
         ContactsContract.Contacts.HAS_PHONE_NUMBER};


    private Button fetch_btn;
    private ListView contacts_lv;
    private  ArrayAdapter<Contact> adapter;
    private ProgressBar progressBar;
    private boolean firstTimeloaded=false;

   private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         contacts=new ArrayList<>();
         progressDialog = new ProgressDialog(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_read_contacts, container, false);
        contacts_lv= (ListView) view.findViewById(R.id.contacts_lv);
        progressBar= (ProgressBar) view.findViewById(R.id.progress_bar);
        fetch_btn= (Button) view.findViewById(R.id.fetch_contatcts_btn);
        fetch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  progressBar.setVisibility(View.VISIBLE);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                if(!firstTimeloaded)

                {

                    getLoaderManager().initLoader(1,null,ReadContactsFrag.this);
                    firstTimeloaded=true;
                }
                else {

                    getLoaderManager().restartLoader(1, null, ReadContactsFrag.this);
                }
            }
        });

        contacts_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contact contact= (Contact) adapterView.getAdapter().getItem(i);
                startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" +contact.getPhone())));
            }
        });

        return  view;
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id==1){ //loader unique id
            return  new CursorLoader(getContext(),ContactsContract.Contacts.CONTENT_URI,mProjection, null, null,null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        try {
            if (cursor != null && cursor.getCount() > 0) {

                while (cursor.moveToNext()) {

                    String contact_id=cursor.getString(1); // get phone by id
                    String hasPhone = cursor.getString(2); //

                         if (Integer.valueOf(hasPhone)>0) {

                                 Cursor phones = getContext().getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contact_id, null, null);

                                 while (phones.moveToNext()) {

                                     String phoneNumber = phones.getString(phones.getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER));

                                     Contact contact=new Contact(cursor.getString(0),phoneNumber);

                                     contacts.add(contact);
                                  }
                                phones.close();
                         }


                }
                cursor.close();
                adapter = new ArrayAdapter<Contact>(getContext(), android.R.layout.simple_list_item_1, contacts);
                contacts_lv.setAdapter(adapter);
               // progressBar.setVisibility(View.GONE);
                progressDialog.dismiss();


            } else {}

        }catch (Exception e)
        {e.printStackTrace();}
    }


    @Override
    public void onLoaderReset(Loader loader) {

    }
}
