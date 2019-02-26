package com.example.asus.miniproject1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String Tag1 = "Debug";

    //declare items in layout.
    EditText etAmount;
    TextView tvResult;
    Spinner spinnerBase;
    Spinner spinnerTarget;

    //declare the JSON object as null.
    JSONObject jsonRates = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //finding the view id in layout.
        etAmount = (EditText) findViewById(R.id.etAmount);
        tvResult = (TextView) findViewById(R.id.tvResult);
        spinnerBase = (Spinner) findViewById(R.id.spinnerBase);
        spinnerTarget = (Spinner) findViewById(R.id.spinnerTarget);

        //calling function.
        configureBaseSpinner();
        configureTargetSpinner();
    }

    //creates menu items in activity.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //resources menu.name of menu, menu folder.
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //for user interfaces uses.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //to compare the id clicked by users.
        if(item.getItemId() == R.id.currency_details) {

            //compare the validity.
            if(!isValid())
                return false;

            ExchangeRates er = new ExchangeRates(true);
            er.execute();
        }
        return super.onOptionsItemSelected(item);
    }

    //declaring internet available check (true / false).
    private boolean isInternetAvailable() {

        //when there are no connection, the manager == null.
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager == null) {

            return false;
        }

        else {

            return true;
        }
    }

    //checking the user input amount (true / false).
    //checking the validity of the internet.
    private boolean isValid() {

        String tempAmount = etAmount.getText().toString();
        if (!isInternetAvailable()) {

            Toast.makeText(MainActivity.this, "No internet connection.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (tempAmount.isEmpty()) {

            Toast.makeText(MainActivity.this, "Please enter a valid amount. Not = 0.", Toast.LENGTH_SHORT).show();

            //return to the main function. [Only true will proceed downward.]
            return false;
        }

        //checking the input value not = 0.
        if (Double.parseDouble(tempAmount) == 0) {

            Toast.makeText(MainActivity.this, "Please enter a valid amount. Not = 0.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    //create and configure adapter for base spinner.
    private void configureBaseSpinner() {

        spinnerBase = (Spinner) findViewById(R.id.spinnerBase);

        //find the array according to strings.xml into the adapter.
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.base, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerBase.setAdapter(adapter1);
    }

    //create and configure adapter for target spinner.
    private void configureTargetSpinner() {

        String[] rawCurrencies = getResources().getStringArray(R.array.target);
        ArrayList<String> temp = new ArrayList<>();

        Log.d(Tag1,"Length ="+ rawCurrencies.length);
        //to split the currency name and the symbol decimal code.
        for (int i = 0; i < rawCurrencies.length; i++) {

            String data = rawCurrencies[i];
            temp.add(data);
            Log.d(Tag1,"i= "+rawCurrencies[i]);
        }

        //create a temporary array to store the array name.
        String[] currenciesName = temp.toArray(new String[0]);
        spinnerTarget = (Spinner) findViewById(R.id.spinnerTarget);

        //find the array according to strings.xml into the adapter.
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item, currenciesName);

        //create the checkboxes in the spinner.
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerTarget.setAdapter(adapter);
    }

    public void executeTask (View view) {

        //String targetItem = spinnerTarget.getSelectedItem().toString();
        //Toast.makeText(MainActivity.this, targetItem, Toast.LENGTH_SHORT).show();
        //if not valid (isValid = false).
        if(!isValid()) {

            //return to the main function.
            return;
        }

        ExchangeRates exchangeRates = new ExchangeRates(false);
        exchangeRates.execute();
    }

    private class ExchangeRates extends AsyncTask {

        ProgressDialog progressDialog;
        int serverCode;

        //declare a flag to differentiate between show_all list and direct convert.
        boolean showDetails;
        ExchangeRates(boolean flag) {

            //when the flag is true.
            showDetails = flag;
            Log.d ("Tag", "The flag is " + flag);
        }

        @Override
        protected void onPreExecute() {

            Log.d (Tag1, "Executing onPreExecute Method.");

            //open the progress dialog when loading.
            progressDialog = ProgressDialog.show(MainActivity.this, "Please Wait.", "Please wait while we get the exchange rate from the server.", true, false);
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {

            HttpURLConnection connection = null;

            Log.d (Tag1, "Executing doInBackground Method.");
            try {

                //set up the URL link of the server and get the response code.
                URL url = new URL ("https://openexchangerates.org/api/latest.json?app_id=5ec216f94af1485dae24ca3bbd2158d4");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                serverCode = connection.getResponseCode();

                Log.d (Tag1, "server code is: " + serverCode);

                if (serverCode == 200) {

                    //read and parse the data.
                    //get the input stream from connection.
                    InputStream inputStream = connection.getInputStream();

                    //open the stream with BufferReader to read.
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";

                    //create a StringBuffer.
                    StringBuffer data = new StringBuffer();

                    //read line from buffer and assign to line object.
                    //if line read is null [no more line], terminate loop.
                    while ((line = bufferedReader.readLine()) != null) {

                        data.append(line);
                    }

                    connection.disconnect();

                    Log.d (Tag1, "Downloaded data as string: " + data);

                    //convert the StringBuffer to JSON root.
                    JSONObject jsonRootObject = new JSONObject(data.toString());

                    //read the rates in JSONRates object.
                    jsonRates = jsonRootObject.getJSONObject("rates");
                }

            }

            catch (MalformedURLException e) {

                Log.d (Tag1, "Malformed URL exception." + e.toString());
                e.printStackTrace();
            }

            catch (IOException e) {

                Log.d (Tag1, "IO Exception." + e.toString());
                e.printStackTrace();
            }

            catch (JSONException e) {

                Log.d (Tag1, "JSON exception." + e.toString());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {

            Log.d (Tag1, "Executing onPostExecute Method.");

            //dismiss the progress dialog.
            progressDialog.dismiss();
            super.onPostExecute(o);

            //get the selected item from the spinnerTarget.
            String target = spinnerTarget.getSelectedItem().toString();

            //get the text from the etAmount [Edit Text] and convert it into double [decimal integer]
            double amount = Double.parseDouble(etAmount.getText().toString());

            //setting the decimal to only 2 decimal places.
            DecimalFormat decimalFormat = new DecimalFormat("#.00");

            //security on preventing apps crash/ stop working.
            if(serverCode == 200) {

                if (jsonRates == null)
                {

                    Toast.makeText(MainActivity.this, "You have out of quota.", Toast.LENGTH_SHORT).show();
                }

                else {
                    try {
                        if(showDetails) {
                            //starting the new activity.
                            Intent intent = new Intent(MainActivity.this, CurrencyList.class);
                            intent.putExtra("rates", jsonRates.toString());
                            intent.putExtra("amount", etAmount.getText().toString());

                            startActivity(intent);
                        }

                        else {
                            //arithmetic calculation part.
                            double currentRate = jsonRates.getDouble(target);
                            double result = currentRate * amount;

                            //setting the result for user interface.
                            tvResult.setText(decimalFormat.format(result) + " " + target);
                        }
                    }

                    catch (JSONException e) {

                        Log.d(Tag1, "JSON Exception " + e.toString());
                        e.printStackTrace();
                    }
                }
            }

            //security on preventing apps crash/ stop working.
            else {

                Toast.makeText(MainActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
