package ru.timerchat.bnet_test_tkh1511.activity;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import ru.timerchat.bnet_test_tkh1511.R;
import ru.timerchat.bnet_test_tkh1511.adapter.NoteListAdapter;
import ru.timerchat.bnet_test_tkh1511.dialog.NoteFragment;
import ru.timerchat.bnet_test_tkh1511.model.ModelNote;

/**
 * Created by Timur on 22.03.16.
 */
public class NoteListActivity  extends AppCompatActivity {

    public final static String
            URL = "https://bnet.i-partner.ru/testAPI/",
            TOKEN_KEY = "token",
            TOKEN_VALUE = "uGdtON4-CJ-UcoKjBV",
            DATA_KEY = "a",
            DATA_NEW_SESSION_VALUE = "new_session",
            DATA_GET_ENTRIES_VALUE = "get_entries",
            SESSION_KEY = "session";

    private Context context;

    public static final String APP_PREFERENCES = "mysettings";

    public static final String APP_PREFERENCES_SESSION = "session";


    private SharedPreferences mSettings;

    public static String  session;

    private String answer;

    NoteListAdapter adapter;

    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notelist);


        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        session = mSettings.getString(APP_PREFERENCES_SESSION, "0");



        //recycler
        recyclerView = (RecyclerView) findViewById(R.id.rvNoteList);
        layoutManager = new LinearLayoutManager(this);

        adapter = new NoteListAdapter(this);


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteListActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });


        SelectNotes();
    }



    private void SelectNotes(){

        if ("0".equalsIgnoreCase(session)){
            selectNewSession();
        } else {
            select_get_entries();
        }


    }



    private void selectNewSession() {
        class SelectNewSessionAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {



                HashMap<String, String> postDataParams = new HashMap<String, String>();
                postDataParams.put(DATA_KEY, DATA_NEW_SESSION_VALUE);

                java.net.URL url;
                String response = "";

                try {
                    url = new URL(URL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty(TOKEN_KEY, TOKEN_VALUE);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);



                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));

                    writer.flush();
                    writer.close();
                    os.close();
                    int responseCode=conn.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((line=br.readLine()) != null) {
                            response+=line;
                        }
                    }
                    else {
                        response="";

                        throw new HttpException(responseCode+"");
                    }

                }  catch (Exception e) {
                    e.printStackTrace();
                    response = "nonet";
                }

                answer = response;
                return answer;
            }



            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(answer);
                if ("nonet".equalsIgnoreCase(answer))
                {
                    UpdateDataDialog();
                }
                else {
                    try {
                        JSONObject json = new JSONObject(answer);
                        String session = json.getJSONObject("data").getString("session");
                        answer = session;
                    } catch (JSONException e) {

                    }

                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putString(APP_PREFERENCES_SESSION, answer);
                    editor.apply();
                    select_get_entries();
                }
            }
        }

        SelectNewSessionAsyncTask selectNewSessionAsyncTask = new SelectNewSessionAsyncTask();
        selectNewSessionAsyncTask.execute();
    }



    private void select_get_entries(){
        class SelectGetEntriesAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {


                HashMap<String, String> postDataParams = new HashMap<String, String>();
                postDataParams.put(DATA_KEY, DATA_GET_ENTRIES_VALUE);
                postDataParams.put(SESSION_KEY, session);

                URL url;
                String response = "";

                try {
                    url = new URL(URL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty(TOKEN_KEY, TOKEN_VALUE);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);


                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));

                    writer.flush();
                    writer.close();
                    os.close();
                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((line = br.readLine()) != null) {
                            response += line;
                        }
                    } else {
                        response = "";

                        throw new HttpException(responseCode + "");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    response = "nonet";
                }

                answer = response;
                return answer;
            }


            @Override
            protected void onPostExecute(String result) {
                //dialog.dismiss();
                super.onPostExecute(answer);
                if ("nonet".equalsIgnoreCase(answer)) {
                    UpdateDataDialog();
                } else {
                    List<ModelNote> notes = new ArrayList<>();

                    try {
                        JSONObject json = new JSONObject(answer);
                        JSONArray urls = json.getJSONArray("data").getJSONArray(0);


                        for (int i = 0; i < urls.length(); i++) {


                            String id = urls.getJSONObject(i).getString("id").toString();
                            String body = urls.getJSONObject(i).getString("body").toString();
                            long da = Long.valueOf(urls.getJSONObject(i).getString("da").toString())*1000;
                            long dm = Long.valueOf(urls.getJSONObject(i).getString("dm").toString())*1000;

                            ModelNote modelNote = new ModelNote(id, body, da, dm);
                            notes.add(modelNote);
                        }

                    } catch (JSONException e) {

                    }
                    for (int i = 0; i < notes.size(); i++) {
                        addNote(notes.get(i));
                    }

                }
            }
        }

        SelectGetEntriesAsyncTask selectGetEntriesAsyncTask = new SelectGetEntriesAsyncTask();
        selectGetEntriesAsyncTask.execute();
    }








    public void addNote(ModelNote newNote) {

        adapter.addNote(newNote);

    }


    private void UpdateDataDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NoteListActivity.this);
        builder.setTitle("Ошибка!")
                .setMessage("Проверьте соединение с сетью")
                .setPositiveButton("Обновить данные",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SelectNotes();
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }




    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }



    public void showNoteDialog(ModelNote note) {
        FragmentManager fm = getFragmentManager();
        DialogFragment noteFragment = NoteFragment.newInstance(note);
        noteFragment.show(fm, "NoteFragment");
    }



}
