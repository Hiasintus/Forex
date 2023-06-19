package com.sintus.forex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.media.AudioMetadata;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.text.DecimalFormat;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private ProgressBar loadingProgressBar;
    private SwipeRefreshLayout swipeRefreshLayout1;
    private TextView audTextView, bndTextView, btcTextView, eurTextView, gbpTextView, hkdTextView, inrTextView, jpyTextView,  myrTextView, usdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout1 = findViewById(R.id.swipeRefreshLayout1);
        audTextView = findViewById(R.id.audTextView);
        bndTextView = findViewById(R.id.bndTextView);
        btcTextView = findViewById(R.id.btcTextView);
        eurTextView = findViewById(R.id.eurTextView);
        gbpTextView = findViewById(R.id.gbpTextView);
        hkdTextView = findViewById(R.id.hkdTextView);
        inrTextView = findViewById(R.id.inrTextView);
        jpyTextView = findViewById(R.id.jpyTextView);
        myrTextView = findViewById(R.id.myrTextView);
        usdTextView = findViewById(R.id.usdTextView);
        loadingProgressBar=findViewById(R.id.loadingProgressBar);

        initSwipeRefreshLayout();
        initForex();
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout1.setOnRefreshListener(() -> {
             initForex();
             swipeRefreshLayout1.setRefreshing(false);
        });
    }

    public String fromatNumber(double number, String format){
        DecimalFormat decimalFormat= new DecimalFormat(format);
        return decimalFormat.format(number);
    }


    private void initForex()
    {
        loadingProgressBar.setVisibility(TextView.VISIBLE);
        String url ="https://openexchangerates.org/api/latest.json?app_id=d7bd78251fe041cebf2f8d43ae95c006";

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson gson = new Gson();
                RootModel rootModel = gson.fromJson(new String(responseBody), RootModel.class);
                RatesModel ratesModel = rootModel.getRatesModel();

                double aud= ratesModel.getIDR()/ ratesModel.getAUD();
                double bnd= ratesModel.getIDR()/ ratesModel.getBND();
                double btc= ratesModel.getIDR()/ ratesModel.getBTC();
                double eur= ratesModel.getIDR()/ ratesModel.getEUR();
                double gbp= ratesModel.getIDR()/ ratesModel.getGBP();
                double hkd= ratesModel.getIDR()/ ratesModel.getHKD();
                double inr= ratesModel.getIDR()/ ratesModel.getINR();
                double jpy= ratesModel.getIDR()/ ratesModel.getJPY();
                double myr= ratesModel.getIDR()/ ratesModel.getMYR();
                double idr= ratesModel.getIDR();

                audTextView.setText(fromatNumber(aud, "###,##0.##"));
                bndTextView.setText(fromatNumber(bnd, "###,##0.##"));
                btcTextView.setText(fromatNumber(btc, "###,##0.##"));
                eurTextView.setText(fromatNumber(eur, "###,##0.##"));
                gbpTextView.setText(fromatNumber(gbp, "###,##0.##"));
                hkdTextView.setText(fromatNumber(hkd, "###,##0.##"));
                inrTextView.setText(fromatNumber(inr, "###,##0.##"));
                jpyTextView.setText(fromatNumber(jpy, "###,##0.##"));
                myrTextView.setText(fromatNumber(myr, "###,##0.##"));
                usdTextView.setText(fromatNumber(idr, "###,##0.##"));

                loadingProgressBar.setVisibility(TextView.INVISIBLE);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


}