package ru.timerchat.bnet_test_tkh1511.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import ru.timerchat.bnet_test_tkh1511.R;
/**
 * Created by Timur on 22.03.16.
 */
public class AddNoteActivity  extends AppCompatActivity {

    public final static String
            URL = "https://bnet.i-partner.ru/testAPI/",
            TOKEN_KEY = "token",
            TOKEN_VALUE = "uGdtON4-CJ-UcoKjBV",
            DATA_KEY = "a",
            DATA_ADD_ENTRY_VALUE = "add_entry",
            SESSION_KEY = "session",
            BODY_KEY = "body";

    private ProgressDialog progressDialog;


    EditText etBody;
    String textBody;

    public static final String APP_PREFERENCES = "mysettings";

    public static final String APP_PREFERENCES_SESSION = "session";


    private SharedPreferences mSettings;

    public static String  session;

    private String answer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        session = mSettings.getString(APP_PREFERENCES_SESSION, "0");

        etBody = (EditText) findViewById(R.id.etBody);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_note:
                addNote();
                break;
            case R.id.cancel:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }




    private void addNote() {

        textBody = etBody.getText().toString();

        sendNote();

    }




    private void sendNote(){
        class SendNoteAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> postDataParams = new HashMap<String, String>();

                postDataParams.put(DATA_KEY, DATA_ADD_ENTRY_VALUE);
                postDataParams.put(SESSION_KEY, session);
                postDataParams.put(BODY_KEY, textBody);

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
                } catch (Exception e) {
                    e.printStackTrace();
                    response = "nonet";
                }

                answer = response;
                return answer;
            }

            @Override
            protected void onPreExecute() {

                progressDialog = new ProgressDialog(AddNoteActivity.this);
                progressDialog.setMessage("Отправление...");
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(true);
                progressDialog.show();


                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(answer);


                if ("nonet".equalsIgnoreCase(answer))
                {
                    progressDialog.cancel();
                    Toast.makeText(getApplicationContext(), "Отсутствует соединение, попробуйте повторить попытку позже", Toast.LENGTH_SHORT).show();


                }
                else {
                    progressDialog.cancel();
                    Intent intent = new Intent(AddNoteActivity.this, NoteListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);


                }

            }
        }
        SendNoteAsyncTask sendNoteAsyncTask = new SendNoteAsyncTask();
        sendNoteAsyncTask.execute();

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



}
