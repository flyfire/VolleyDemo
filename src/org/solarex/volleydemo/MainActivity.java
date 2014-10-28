
package org.solarex.volleydemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private String urlJsonObj = "http://solarex.github.io/downloads/code/json/person_object.json";
    private String urlJsonArray = "http://solarex.github.io/downloads/code/json/person_array.json";

    private Button btnMakeObjectRequest, btnMakeArrayRequest;
    private ProgressDialog pDialog;
    private TextView tvResponse;

    private StringBuilder sbResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMakeObjectRequest = (Button) this.findViewById(R.id.btnObjRequest);
        btnMakeArrayRequest = (Button) this.findViewById(R.id.btnArrayRequest);
        tvResponse = (TextView) this.findViewById(R.id.txtResponse);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        btnMakeObjectRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                makeJsonObjectRequest();
            }
        });

        btnMakeArrayRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                makeJsonArrayRequest();
            }
        });
    }

    private void makeJsonObjectRequest() {
        showDialog();
        JsonObjectRequest request = new JsonObjectRequest(Method.GET, urlJsonObj, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            sbResponse = new StringBuilder();
                            String name = response.getString("name");
                            String email = response.getString("email");
                            JSONObject phone = response.getJSONObject("phone");
                            String home = phone.getString("home");
                            String mobile = phone.getString("mobile");

                            sbResponse.append("Name: " + name + "\n"
                                    + "Email: " + email + "\n"
                                    + "Home: " + home + "\n"
                                    + "Mobile: " + mobile);
                            tvResponse.setText(sbResponse.toString());
                        } catch (JSONException e) {
                            Log.d(TAG, "Exception happened, ex = " + e.getMessage());
                        }
                        hideDialog();
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        VolleyLog.d(TAG, "error: " + arg0.getMessage());
                        Toast.makeText(getApplicationContext(), arg0.getMessage(),
                                Toast.LENGTH_LONG).show();
                        hideDialog();
                    }

                });
        App.getInstance().addToRequestQueue(request);
    }

    private void makeJsonArrayRequest() {
        showDialog();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            sbResponse = new StringBuilder();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject person = (JSONObject) response.get(i);
                                String name = person.getString("name");
                                String email = person.getString("email");
                                JSONObject phone = person.getJSONObject("phone");
                                String home = phone.getString("home");
                                String mobile = phone.getString("mobile");

                                sbResponse.append("Name: " + name + "\n"
                                        + "Email: " + email + "\n"
                                        + "Home: " + home + "\n"
                                        + "Mobile: " + mobile + "\n");
                                if (i == 0) {
                                    sbResponse.append("-------------------\n");
                                }
                            }
                            tvResponse.setText(sbResponse.toString());
                        } catch (JSONException e) {
                            Log.d(TAG, "Exception happened, ex = " + e.getMessage());
                            Toast.makeText(getApplicationContext(), "error:" + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                        hideDialog();
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        VolleyLog.d(TAG, "Error: " + arg0.getMessage());
                        Toast.makeText(getApplicationContext(), "Error" + arg0.getMessage(),
                                Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                });
        App.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    private void showDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

}
