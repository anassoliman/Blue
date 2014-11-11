package com.example.ibrahim.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Set;


public class Bluetooth extends ActionBarActivity implements View.OnClickListener {

    Button btnBTon, btnBToff, btnVisible, btnList, btnSearch;
    ListView listViewOfDevices;
    private ArrayAdapter<String> BTArrayAdapter;
    private Set<BluetoothDevice> pairedDevices;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        btnBTon = (Button) findViewById(R.id.btnBTon);
        btnBTon.setOnClickListener(this);

        btnBToff = (Button) findViewById(R.id.btnBToff);
        btnBToff.setOnClickListener(this);

        btnVisible = (Button) findViewById(R.id.btnVisible);
        btnVisible.setOnClickListener(this);

        btnList = (Button) findViewById(R.id.btnList);
        btnList.setOnClickListener(this);

        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        listViewOfDevices = (ListView) findViewById(R.id.listofdevicesview);

         BTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listViewOfDevices.setAdapter(BTArrayAdapter);


    }

    @Override
    protected void onPause() {
        super.onPause();
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnBTon:


                if (mBluetoothAdapter == null) {
                    // Device does not support Bluetooth
                    Toast.makeText(getApplicationContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();

                } else if (!mBluetoothAdapter.isEnabled()) {

                    TurnOnBT();
                    Toast.makeText(getApplicationContext(), "Bluetooth on", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth already on", Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.btnBToff:

                if (mBluetoothAdapter.isEnabled()) {
                    Toast.makeText(getApplicationContext(), "Bluetooth off", Toast.LENGTH_SHORT).show();
                    TurnoffBT();
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth is already off", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnVisible:

                Visible();

                break;

            case R.id.btnList:


                getPairedDevices();

                break;
            case R.id.btnSearch:

                SearchDevices();
                break;
            default:
                break;

        }


    }

    final BroadcastReceiver bReceiver = new BroadcastReceiver() {


        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // add the name and the MAC address of the object to the arrayAdapter
                BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                BTArrayAdapter.notifyDataSetChanged();
            }
        }

    };


    private void SearchDevices() {


        if (mBluetoothAdapter.isDiscovering()) {
            // the button is pressed when it discovers, so cancel the discovery
            mBluetoothAdapter.cancelDiscovery();
        } else {
            BTArrayAdapter.clear();
            mBluetoothAdapter.startDiscovery();

            registerReceiver(bReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        }
    }

    private void Visible() {
        //Gör enheten synlig
        Intent getVisible = new Intent(BluetoothAdapter.
                ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }

    private void getPairedDevices() {
        // ger dig enheterna som är kopplade eller var kopplade.

        pairedDevices = mBluetoothAdapter.getBondedDevices();


        for (BluetoothDevice bt : pairedDevices)
            BTArrayAdapter.add(bt.getName());

        Toast.makeText(getApplicationContext(), "Showing Paired Devices",
                Toast.LENGTH_SHORT).show();
       // BTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        //listViewOfDevices.setAdapter(BTArrayAdapter);


    }


    private void TurnoffBT() {

        //Stänger av bluetooth
        mBluetoothAdapter.disable();
    }

    private void TurnOnBT() {
        //Sätter på bluetooth
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, 1);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        unregisterReceiver(bReceiver);
    }
}
