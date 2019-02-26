package com.example.asus.miniproject1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class CurrencyList extends Activity {

    ArrayList<Currency> Currencies = new ArrayList<>();

    //declaring the JSONObject
    JSONObject jsonRates;
    double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //getting the intent from previous activity.
        Intent intent = getIntent();
        String rates = intent.getStringExtra("rates");
        String tempAmount = intent.getStringExtra("amount");

        try {

            jsonRates = new JSONObject(rates);
        }

        catch (JSONException e) {

            Log.d("Tag", "JSON Exception" + e.toString());
            e.printStackTrace();
        }

        amount = Double.parseDouble(tempAmount);
        populateData();

        configureListAdapter();
    }

    //populating the array list.
    private void populateData() {

        String[] rawCurrencies = getResources().getStringArray(R.array.target);
        ArrayList<String> name = new ArrayList<>();

        for (int i = 0; i < rawCurrencies.length; i++) {

            String data = rawCurrencies[i];
            name.add(data);
         }

        try {

            for (int i = 0; i < rawCurrencies.length; i++) {
                Currencies.add(new Currency(name.get(i), jsonRates.getDouble(name.get(i)), amount));
            }

        } catch (JSONException e) {

            Log.d("Tag", "JSON Exception" + e.toString());
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException ex) {

            Log.d("Tag", "Array out of bound. Please check currency resource file.");
            Toast.makeText(CurrencyList.this, "Currencies names and symbols mismatched.", Toast.LENGTH_SHORT).show();
        }
    }

    //configuring array list view.
    private void configureListAdapter() {
        ArrayAdapter<Currency> customAdapter = new myListAdapter();
        ListView listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(customAdapter);
    }

    //showing the variables in the text view.
    private class myListAdapter extends ArrayAdapter<Currency> {
        public myListAdapter() {

            super(CurrencyList.this, R.layout.all_currency_layout, Currencies);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //Make sure we have a view to work with.
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.all_currency_layout, parent, false);
            }

            TextView tvName = (TextView) itemView.findViewById(R.id.tvName);
            TextView tvPerUnit = (TextView) itemView.findViewById(R.id.tvPerUnit);
            TextView tvCountry = (TextView) itemView.findViewById(R.id.tvCountry);
            TextView tvTotalAmount = (TextView) itemView.findViewById(R.id.tvTotalAmount);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.flagIcon);

            Currency current = Currencies.get(position);
            DecimalFormat decimalFormat = new DecimalFormat("#.0000");

            tvName.setText(current.getName());
            tvPerUnit.setText(decimalFormat.format(current.getPerUnit()));
            tvTotalAmount.setText(decimalFormat.format(current.getTotalAmount()));

            // set the imageView directly refer to the imageID array.
            imageView.setImageResource(imageID[position]);

            return itemView;
        }
    }

    // imageID array.
    private Integer[] imageID = {
            R.drawable.aed,
            R.drawable.afn,
            R.drawable.all,
            R.drawable.amd,
            R.drawable.ang,
            R.drawable.aoa,
            R.drawable.ars,
            R.drawable.aud,
            R.drawable.awg,
            R.drawable.azn,
            R.drawable.bam,
            R.drawable.bbd,
            R.drawable.bdt,
            R.drawable.bgn,
            R.drawable.bhd,
            R.drawable.bif,
            R.drawable.bmd,
            R.drawable.bnd,
            R.drawable.bob,
            R.drawable.brl,
            R.drawable.bsd,
            R.drawable.btn,
            R.drawable.btc,
            R.drawable.bwp,
            R.drawable.byn,
            R.drawable.bzd,
            R.drawable.cad,
            R.drawable.cdf,
            R.drawable.chf,
            R.drawable.clp,
            R.drawable.cny,
            R.drawable.cop,
            R.drawable.crc,
            R.drawable.cuc,
            R.drawable.cup,
            R.drawable.cve,
            R.drawable.czk,
            R.drawable.djf,
            R.drawable.dkk,
            R.drawable.dop,
            R.drawable.dzd,
            R.drawable.eek,
            R.drawable.egp,
            R.drawable.ern,
            R.drawable.etb,
            R.drawable.eur,
            R.drawable.fjd,
            R.drawable.fkp,
            R.drawable.gbp,
            R.drawable.gel,
            R.drawable.ggp,
            R.drawable.ghs,
            R.drawable.gip,
            R.drawable.gmd,
            R.drawable.gnf,
            R.drawable.gtq,
            R.drawable.gyd,
            R.drawable.hkd,
            R.drawable.hnl,
            R.drawable.hrk,
            R.drawable.htg,
            R.drawable.huf,
            R.drawable.idr,
            R.drawable.ils,
            R.drawable.imp,
            R.drawable.inr,
            R.drawable.iqd,
            R.drawable.irr,
            R.drawable.isk,
            R.drawable.jep,
            R.drawable.jmd,
            R.drawable.jod,
            R.drawable.jpy,
            R.drawable.kes,
            R.drawable.kgs,
            R.drawable.khr,
            R.drawable.kmf,
            R.drawable.kpw,
            R.drawable.krw,
            R.drawable.kwd,
            R.drawable.kyd,
            R.drawable.kzt,
            R.drawable.lak,
            R.drawable.lbp,
            R.drawable.lkr,
            R.drawable.lrd,
            R.drawable.lsl,
            R.drawable.ltl,
            R.drawable.lvl,
            R.drawable.lyd,
            R.drawable.mad,
            R.drawable.mdl,
            R.drawable.mga,
            R.drawable.mkd,
            R.drawable.mmk,
            R.drawable.mnt,
            R.drawable.mop,
            R.drawable.mro,
            R.drawable.mtl,
            R.drawable.mur,
            R.drawable.mvr,
            R.drawable.mwk,
            R.drawable.mxn,
            R.drawable.myr,
            R.drawable.mzn,
            R.drawable.nad,
            R.drawable.ngn,
            R.drawable.nio,
            R.drawable.nok,
            R.drawable.npr,
            R.drawable.nzd,
            R.drawable.omr,
            R.drawable.pab,
            R.drawable.pen,
            R.drawable.pgk,
            R.drawable.php,
            R.drawable.pkr,
            R.drawable.pln,
            R.drawable.pyg,
            R.drawable.qar,
            R.drawable.ron,
            R.drawable.rsd,
            R.drawable.rub,
            R.drawable.rwf,
            R.drawable.sar,
            R.drawable.sbd,
            R.drawable.scr,
            R.drawable.sdg,
            R.drawable.sek,
            R.drawable.sgd,
            R.drawable.shp,
            R.drawable.sll,
            R.drawable.sos,
            R.drawable.srd,
            R.drawable.std,
            R.drawable.svc,
            R.drawable.syp,
            R.drawable.szl,
            R.drawable.thb,
            R.drawable.tjs,
            R.drawable.tmt,
            R.drawable.tnd,
            R.drawable.top,
            R.drawable.tri,
            R.drawable.ttd,
            R.drawable.twd,
            R.drawable.tzs,
            R.drawable.uah,
            R.drawable.ugx,
            R.drawable.usd,
            R.drawable.uyu,
            R.drawable.uzs,
            R.drawable.vef,
            R.drawable.vnd,
            R.drawable.vuv,
            R.drawable.wst,
            R.drawable.xaf,
            R.drawable.yer,
            R.drawable.zar,
            R.drawable.zmw,
            R.drawable.zwl,
};
}
