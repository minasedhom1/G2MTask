package com.example.lenovo.g2mtask;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchCountriesFrag extends Fragment {
  public static final String COUNTRIES_URL="http://services.groupkt.com/country/get/all";
    public SearchCountriesFrag() {
        // Required empty public constructor
    }

ArrayList<String > countries_name;
    ListView countries_lv;
    ArrayAdapter<String> adapter;
    ProgressBar progressBar;
    EditText search_Bar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countries_name=new ArrayList<>();
        StringRequest request= new StringRequest(Request.Method.GET, COUNTRIES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            jsonObject=jsonObject.getJSONObject("RestResponse");
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                countries_name.add(object.getString("name"));
                            }
                            countries_name.size();
                            {
                                if (getContext() != null)
                                    adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, countries_name);
                                countries_lv.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String s =error.getMessage();
            }
        });
        RequestQueue queue= Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_search_countries, container, false);
        countries_lv= (ListView) v.findViewById(R.id.countries_lv);
        progressBar= (ProgressBar) v.findViewById(R.id.progress_bar);
        search_Bar= (EditText) v.findViewById(R.id.search_et);

        search_Bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


}
