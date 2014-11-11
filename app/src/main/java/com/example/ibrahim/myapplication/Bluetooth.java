package com.example.ibrahim.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Set;


public class Bluetooth extends ActionBarActivity implements View.OnClickListener {

    Button btnBTon, btnBToff, btnSearch;
    ListView listofdevicesview;
    ArrayAdapter<String> listAdapter;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        btnBTon = (Button) findViewById(R.id.btnBTon);
        btnBTon.setOnClickListener(this);

        btnBToff = (Button) findViewById(R.id.btnBToff);
        btnBToff.setOnClickListener(this);

        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        listofdevicesview = (ListView) findViewById(R.id.listofdevicesview);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 0);
        listofdevicesview.setAdapter(listAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bluetooth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private final BroadcastReceiver mReviever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                listAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnBTon:

                Toast.makeText(getApplicationContext(), "Bluetooth on", Toast.LENGTH_SHORT).show();


                if (mBluetoothAdapter == null) {
                    // Device does not support Bluetooth
                    Toast.makeText(getApplicationContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();

                } else {
                    if (!mBluetoothAdapter.isEnabled()) {

                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, 1);
                    }
                }
                // Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.get

                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                // If there are paired devices
                if (pairedDevices.size() > 0) {
                    // Loop through paired devices
                    for (BluetoothDevice device : pairedDevices) {
                        // Add the name and address to an array adapter to show.

                        Toast.makeText(getApplicationContext(), "The devices : " + device.getName() + "\n" + device.getAddress(), Toast.LENGTH_SHORT).show();

                        //listAdapter.add(device.getName() + "\n" + device.getAddress());

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "There is no paired devices", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btnBToff:


                if (mBluetoothAdapter.isEnabled()) {
                    Toast.makeText(getApplicationContext(), "Bluetooth off", Toast.LENGTH_SHORT).show();
                    mBluetoothAdapter.disable();
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth is already off", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnSearch:
                Toast.makeText(getApplicationContext(), "Searching for devices.. ", Toast.LENGTH_SHORT).show();



                break;
            default:
                break;

        }


    }
}
