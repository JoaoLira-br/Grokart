package com.example.grokart;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.grokart.app.AppController;
import com.example.grokart.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_username, et_password;
    private static String jsonResponse;
    private JSONObject user;
    private TextView msgResponse, tv_JSONOutput, tv_appName;
    private Button btn_register, btn_login;
    private final String TAG = RegisterActivity.class.getSimpleName();
    private boolean successfulLogin;
    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    public static String EXTRA_USERNAME= "com.groKart.android.username";
    public static final String EXTRA_PASSWORD= "com.groKart.android.password";
    public static final String EXTRA_EMAIL= "com.groKart.android.email";







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView tv_appName = findViewById(R.id.tv_appTitle);

        tv_appName = findViewById(R.id.tv_appTitle);
        btn_login =  findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        msgResponse = findViewById(R.id.tv_msgResponse);
        //setting the style for the App Title
        Spannable groKart = new SpannableString(getString(R.string.groKart));
        groKart.setSpan(new ForegroundColorSpan(Color.GREEN), 0,3,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        groKart.setSpan(new ForegroundColorSpan(Color.RED),3,groKart.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv_appName.setText(groKart);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);



        //TODO all url endpoints must have no whitespace

        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(checkInputs()){
//                    String path = (Const.URL_USER_PSTMN + "?username="+username.trim()+"&password="+password).replaceAll("\\s", "");
                        String userName = et_username.getText().toString();
                        String password = et_password.getText().toString();
                        String path = (Const.URL_SERVER_USERS + userName + "/" + password).replaceAll("\\s", "");
                        Log.d(TAG, path);
                        jsonObjGetReq(path);
                    Log.d(TAG, "response: "+jsonResponse);
                    Log.d(TAG, (String) msgResponse.getText());
                        jsonObjGetReq(Const.URL_SAMPLE_READ_USER_GET+userName);
                    Log.d(TAG, "response: "+ jsonResponse);

                        sendToLoginPage(v);
                    //TODO get user info after succesful login check and send him to main PAGE

//                        if(successfulLogin) {
//                            path = Const.URL_SAMPLE_READ_USER_GET+"userName";
//                            jsonObjGetReq(Const.URL_SAMPLE_READ_USER_GET+"userName");
//
//                            sendToLoginPage(v, user );
//                        }



                }
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //TODO send input to backend and proceed to Home page
                // with response as Intent Extra
                if( checkInputs()) {
                    String userName = et_username.getText().toString();
                    String password = et_password.getText().toString();
                    user = createUser(userName, password);

                    //TODO send input to appropriate backend path
                    jsonObjPostReq(user);

                }

            }
        });

    }


    private Boolean checkInputs() {
        int invalidCounter = 0;
        if (et_username.length() == 0) {
            et_username.setError("Username is required");
            invalidCounter++;
        }
        if (et_password.length() == 0) {
            et_password.setError("Password is required");
            invalidCounter++;
        } else if (et_password.length() < 7) {
            et_password.setError("Password must be minimum 8 characters");
            invalidCounter++;
        }
        if (invalidCounter == 0)  return true;
        else return false;
        // after all validation return true.
    }

    private JSONObject createUser(String userName, String password){
        user = new JSONObject();
        try {
            user.put("userName", userName);
            user.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    //how to handle
    // case 1:  user has not created an account already when pressing login button
    //case 2:
    private void makeJsonObjReq(JSONObject user, String path) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                path, user,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        msgResponse.setText(response.toString());
//                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                hideProgressDialog();
            }
        }) {

//            /**
//             * Passing some request headers
//             * */
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
////                params.put("name", "Androidhive");
////                params.put("email", "abc@androidhive.info");
////                params.put("pass", "password123");
//
//                return params;
//            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                tag_json_obj);
        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }
    /**
     * Making json object request
     * */

    private void jsonObjGetReq(String path) {
//        showProgressDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,path,null,
                new Response.Listener<JSONObject>()  {

                //expect: "message":"successful"
                    //TODO receive id of user back
                    @Override
                    public void onResponse(JSONObject response) {
//                        setSuccessfulLoginCheck(true);
//                        if (response.has("userName")&&response.has("id")) {
//                            setUser(response, new String[]{"userName", "id"});
//                        }

                        jsonResponse = response.toString();
                        EXTRA_USERNAME = response.toString();
                        Log.d(TAG, jsonResponse);
                        Log.d(TAG, EXTRA_USERNAME);
                        msgResponse.setText(EXTRA_USERNAME);


//                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d(TAG, "Unfortunately we got an error");
                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                hideProgressDialog();
                msgResponse.setText(error.getMessage());
            }
        }) {

            /**
             * Passing some request headers
             * */
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
//                return headers;
//            }

//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
////                params.put("name", "Androidhive");
////                params.put("email", "abc@androidhive.info");
////                params.put("pass", "password123");
//
//                return params;
//            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                tag_json_obj);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    private void setUser(JSONObject copyFrom, String[] names){
        try {
            user = new JSONObject(copyFrom, names);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void setSuccessfulLoginCheck(Boolean successful){
        successfulLogin = successful;
    }
    private void jsonObjPostReq(JSONObject user) {
//        showProgressDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,Const.URL_SAMPLE_CREATE_USER_POST,user,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        jsonResponse = response.toString();
                        Log.d(TAG, jsonResponse);
                        Log.d(TAG, "hheuahfeauhfklsahefkjldhsakjhf");
                        Log.d(TAG, "gotcha!!!!!!!!");
//                        msgResponse.setText(response.toString());
//                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                hideProgressDialog();
            }
        }) {

            /**
             * Passing some request headers
             * */
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
//                return headers;
//            }

//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
////                params.put("name", "Androidhive");
////                params.put("email", "abc@androidhive.info");
////                params.put("pass", "password123");
//
//                return params;
//            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                tag_json_obj);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

//    /**
//     * Making json array request
//     * */
    private void makeJsonArrayReq() {
//        showProgressDialog();
        JsonArrayRequest req = new JsonArrayRequest(Const.URL_SERVER_USERS,
                    new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, Const.URL_SERVER_USERS);
                        Log.d(TAG, response.toString());
//                        msgResponse.setText(response.toString());
//                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
//                hideProgressDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req,
                tag_json_arry);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_arry);
    }




//TODO add parameter JSONObject user and send to main page`s greetings and profile
    public void sendToLoginPage(View view) {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//        intent.putExtra("jsonUser", user.toString());
        startActivity(intent);
    }


}