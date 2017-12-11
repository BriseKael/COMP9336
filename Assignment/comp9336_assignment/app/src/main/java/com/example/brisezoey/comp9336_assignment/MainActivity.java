package com.example.brisezoey.comp9336_assignment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
{
    private TextView textView;

    private ListView listView;
    private ArrayAdapter<String> listViewAdapter;
    private List<String> listViewResult;

    private Button buttonStart;
    private Button buttonStop;
    private Button buttonClear;
    private Button buttonNext;

    private Timer timer;
    private TimerTask timerTask;
    private Handler handler;

    private WifiManager wifiManager;
    private ConnectivityManager connectivityManager;

    private String currentData = "";
    private String lastData = "";

    private String currentBSSID = "";
    private String lastBSSID = "";

    private int currentIP = 0;
    private int lastIP = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        listViewAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1);
        listViewResult = new ArrayList<>();

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(listViewAdapter);

        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                timerTask.cancel();
                timerTask = new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        doAssignment();
                        Message message = new Message();
                        handler.sendMessage(message);
                    }
                };
                timer.schedule(timerTask, 0, 1);
            }
        });

        buttonStop = (Button) findViewById(R.id.buttonStop);
        buttonStop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                timerTask.cancel();
            }
        });

        buttonClear = (Button) findViewById(R.id.buttonClear);
        buttonClear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listViewAdapter.clear();
                listViewAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1);
                listView.setAdapter(listViewAdapter);

                // save
                // save file
                try
                {
                    File path = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOCUMENTS);

                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    File file = new File(path, "MobilityAnalysis_" + timestamp + ".txt");

                    FileWriter fileWriter = new FileWriter(file);
                    for (String result : listViewResult)
                    {
                        fileWriter.write(result + "\n");
                    }
                    fileWriter.close();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                listViewResult.clear();
                listViewResult = new ArrayList<String>();

            }
        });

        // go to next task
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });

        timer = new Timer();
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                doAssignment();
                Message message = new Message();
                handler.sendMessage(message);
            }
        };

        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);

                textView.setText(currentData);

                // -- finally
                boolean changed = false;
                if (!lastBSSID.toUpperCase().equals(currentBSSID.toUpperCase()))
                {
                    int index = listViewAdapter.getCount();

                    listViewAdapter.insert((index + 1) + ": AP Changed (last)\n" + lastData, 0);
                    listViewAdapter.insert((index + 2) + ": AP Changed (current)\n" + currentData, 0);

                    listViewResult.add((index + 1) + ": AP Changed (last)\n" + lastData);
                    listViewResult.add((index + 2) + ": AP Changed (current)\n" + currentData);

                    changed = true;

                } else if (lastIP != currentIP)
                {
                    int index = listViewAdapter.getCount();

                    listViewAdapter.insert((index + 1) + ": IP changed (last)\n" + lastData, 0);
                    listViewAdapter.insert((index + 2) + ": IP changed (current)\n" + currentData, 0);

                    listViewResult.add((index + 1) + ": IP Changed (last)\n" + lastData);
                    listViewResult.add((index + 2) + ": IP Changed (current)\n" + currentData);

                    changed = true;
                }
                if (changed)
                {
                    listViewAdapter.notifyDataSetChanged();
                }

                lastData = currentData;
                lastBSSID = currentBSSID;
                lastIP = currentIP;
            }
        };

        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    private void doAssignment()
    {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        // 5. timestamp
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        if (wifiInfo.getBSSID() != null)
        {
            currentData = "SSID: " + wifiInfo.getSSID() +
                    "\nState: " + networkInfo.getState() +
                    "\nLevel: " + wifiInfo.getRssi() +
                    "\nIP: " + intToIp(wifiInfo.getIpAddress()) +
                    "\nBSSID: " + wifiInfo.getBSSID().toUpperCase() +
                    "\nTimestamp: " + timestamp;

            currentBSSID = wifiInfo.getBSSID();
            currentIP = wifiInfo.getIpAddress();
        }
        else
        {
            currentData = "SSID: " + "NULL" +
                    "\nState: " + networkInfo.getState() +
                    "\nLevel: " + "NULL" +
                    "\nIP: " + "NULL" +
                    "\nBSSID: " + "NULL" +
                    "\nTimestamp: " + timestamp;

            currentBSSID = "NULL";
            currentIP = -1;
        }

    }

    private String intToIp(int i)
    {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);
    }
}
