package com.example.root.lcpower;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String TAG = "LcPower main";
    Button BtButton;
    Button WifiButton;
    TextView textView;
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    boolean BtTest = false;
    int btTesttimes = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Dove, hello LcPower");
        BtButton = findViewById(R.id.BtBtn);
        BtButton.setOnClickListener(mBtListener);
        WifiButton = findViewById(R.id.WifiBtn);
        WifiButton.setOnClickListener(mWifiListener);
        textView = findViewById(R.id.TextView);
    }

    @Override
    public void onStart() {

        super.onStart();

        registerReceiver(mBtReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    private final BroadcastReceiver mBtReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int BtState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothDevice.ERROR);
                if (BtTest == true) {
                    if (BtState == BluetoothAdapter.STATE_ON) {
                        Log.d(TAG, "Dove, in Receiver, btstate turn:  " + BtState);
                        textView.setText("BT state " + BtState);
                        adapter.disable();
                    }
                    if (BtState == BluetoothAdapter.STATE_OFF) {
                        Log.d(TAG, "Dove, in Receiver, btstate turn:  " + BtState);
                        textView.setText("BT state: " + BtState + " Test times: " + btTesttimes++);
                        adapter.enable();
                    }
                }
            }
        }
    };
    private final OnClickListener mBtListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int BtState = adapter.getState();
            if (BtTest == false) {
                BtTest = true;
                btTesttimes = 0;
                BtButton.setText("BT Test on");
            }else {
                BtTest = false;
                BtButton.setText("BT Test off");
            }
            Log.d(TAG, "Dove, Bt button is clicked, btstate is:  " +  adapter.getState());
            if (BtState == BluetoothAdapter.STATE_ON) {
                Log.d(TAG, "Dove, Set Bt to OFF");
                adapter.disable();
            } else if (BtState == BluetoothAdapter.STATE_OFF) {
                adapter.enable();
                Log.d(TAG, "Dove, Set Bt to ON");
            }

        }
    };

    private final OnClickListener mWifiListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "Dove, Wifi button is clicked");
        }
    };
}
