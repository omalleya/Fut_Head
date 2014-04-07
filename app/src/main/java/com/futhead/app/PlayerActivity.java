package com.futhead.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends Activity {

    private ProgressDialog pDialog;
    public Elements name;
    public Elements num;
    public ArrayList<String> statName = new ArrayList<String>();
    public ArrayList<String> statNum = new ArrayList<String>();
    JSONParser jParser = new JSONParser();
    private myArrayAdapter adapter;
    private ListView lv;
    String url = "http://www.futhead.com/14/players/141";
    String phpurl = "http://localhost/android_connect/get_all_products.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PLAYER = "player";
    private static final String TAG_ID = "ID";
    private static final String TAG_NAME = "Name";

    JSONArray products = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);

        lv = (ListView) findViewById(R.id.listView);

        /*mProgressDialog = new ProgressDialog(PlayerActivity.this);
        mProgressDialog.setTitle("Player Data");
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();*/
        new Logo().execute();
        //new LoadAllProducts().execute();
        new statistics().execute();
        adapter = new myArrayAdapter(this,statName,statNum);
        //mProgressDialog.dismiss();

    }

    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PlayerActivity.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(phpurl, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PLAYER);

                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_ID);
                        url = url + id;
                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(),
                            MainActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
        }
    }

    private class statistics extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... arg) {
            Document document;
            try {
                document = Jsoup.connect(url).get();
                /*player = document.select("div data-player-full-name");
                playerName.clear();
                for (Element element : num) {
                    statNum.add(element.ownText());
                }*/
                num = document.select("div#stats-base p");
                statNum.clear();
                for (Element element : num) {
                    statNum.add(element.ownText());
                }
                name = document.select("div#stats-base span");
                statName.clear();
                for (Element element : name) {
                    statName.add(element.ownText());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }
        @Override
        protected void onPostExecute(String result) {

            lv.setAdapter(adapter);
        }
    }

    private class Logo extends AsyncTask<Void, Void, Void> {
        Bitmap bitmap;
        Bitmap bitmap1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                // Connect to the web site
                Document document = Jsoup.connect(url).get();
                // Using Elements to get the class data
                Elements img = document.select("div.playercard-picture img[src]");
                Elements img1 = document.select("div.playercard-club img[src]");
                // Locate the src attribute
                String imgSrc = img.attr("src");
                String imgSrc1 = img1.attr("src");
                // Download image from URL
                InputStream input = new java.net.URL(imgSrc).openStream();
                InputStream input1 = new java.net.URL(imgSrc1).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
                bitmap1 = BitmapFactory.decodeStream(input1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set downloaded image into ImageView
            ImageView logoimg = (ImageView) findViewById(R.id.playerPic);
            ImageView clubImg = (ImageView) findViewById(R.id.imageView);
            logoimg.setImageBitmap(bitmap);
            clubImg.setImageBitmap(bitmap1);
        }
    }
}
