package com.example.ibrahim.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;


public class Bluetooth extends ActionBarActivity implements OnClickListener {

    Button btnBTon, btnBToff, btnVisible, btnList, btnSearch;
    ListView listViewOfDevices;
    private ArrayAdapter<String> BTArrayAdapter;
    private Set<BluetoothDevice> pairedDevices;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

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


        ClickItemList();
    }

    @Override
    protected void onPause() {
        super.onPause();
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

        Toast.makeText(getApplicationContext(), "Searching for Devices", Toast.LENGTH_LONG).show();
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
        Toast.makeText(getApplicationContext(), "Making Device visible", Toast.LENGTH_SHORT).show();
    }

    private void getPairedDevices() {
        // ger dig enheterna som är kopplade eller var kopplade.

        pairedDevices = mBluetoothAdapter.getBondedDevices();


        for (BluetoothDevice bt : pairedDevices)
            BTArrayAdapter.add(bt.getName());

        Toast.makeText(getApplicationContext(), "Showing Paired Devices",
                Toast.LENGTH_SHORT).show();


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


    private void ClickItemList() {
        ListView list = (ListView) findViewById(R.id.listofdevicesview);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "You have click device : " + position, Toast.LENGTH_SHORT).show();

            }
        });
    }


    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket);
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private void manageConnectedSocket(BluetoothSocket mmSocket) {

    }
}


