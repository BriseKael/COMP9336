package com.example.brisezoey.comp9336_assignment;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

public class Main2Activity extends AppCompatActivity
{
    private TextView textViewInformation;
    private TextView textViewTCP;

    private Button buttonPrevious;
    private Button buttonRefresh;
    private Button buttonCheckTCP;

    private Handler handler;
    private int statusCode = 0;

    private WifiManager wifiManager;

    private String information;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        textViewInformation = (TextView) findViewById(R.id.textViewInformation);
        textViewTCP = (TextView) findViewById(R.id.textViewTCP);

        // go to previous task
        buttonPrevious = (Button) findViewById(R.id.buttonPrevious);
        buttonPrevious.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        buttonRefresh = (Button) findViewById(R.id.buttonRefresh);
        buttonRefresh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                doAssignment();
                textViewInformation.setText(information);

                // save file
                try
                {
                    File path = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOCUMENTS);

                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    File file = new File(path, "LinkLayer_" + timestamp + ".txt");

                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(information);
                    fileWriter.close();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        buttonCheckTCP = (Button) findViewById(R.id.buttonCheckTCP);
        buttonCheckTCP.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Thread thread = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            URL url = new URL("http://www.google.com.au");
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setConnectTimeout(1000);
                            connection.connect();

                            statusCode = connection.getResponseCode();

                            Message message = new Message();
                            handler.sendMessage(message);

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            statusCode = 0;

                            Message message = new Message();
                            handler.sendMessage(message);
                        }
                    }
                });
                thread.start();

            }
        });

        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);

                if (statusCode != 0)
                {
                    textViewTCP.setText("TCP Connected");
                }
                else
                {
                    textViewTCP.setText("TCP Loss");
                }
            }
        };

        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);
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

        // 1. identify which protocols
        String protocol = "";
        if (wifiInfo.getFrequency() / 1000 == 2)
        {
            if (wifiInfo.getLinkSpeed() / 20 == 0)
            {
                protocol += "802.11b";
            } else if (wifiInfo.getLinkSpeed() / 100 == 0)
            {
                protocol += "802.11g";
            } else
            {
                protocol += "802.11n";
            }
        } else if (wifiInfo.getFrequency() / 1000 == 5)
        {
            if (wifiInfo.getLinkSpeed() / 100 == 0)
            {
                protocol += "802.11a";
            } else if (wifiInfo.getLinkSpeed() / 100 <= 4)
            {
                protocol += "802.11n";
            } else
            {
                protocol += "802.11ac";
            }
        } else
        {
            protocol += "unknow";
        }
        // 2. signal strength
        // 3. data rate
        // 4. ap density
        // cause du, remove it
        int density = 0;

        wifiManager.startScan();
        for (ScanResult scanResult : wifiManager.getScanResults())
        {
            if (scanResult.SSID.toLowerCase().equals("uniwide"))
            {
                density += 1;
            }
        }

        // 5. timestamp
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        if (wifiInfo.getBSSID() != null)
        {
            information = "SSID: " + wifiInfo.getSSID() +
                    "\nIP: " + intToIp(wifiInfo.getIpAddress()) +
                    "\nBSSID: " + wifiInfo.getBSSID().toUpperCase() +
                    "\nProtocol: " + protocol +
                    "\nSignal Strength: " + wifiInfo.getRssi() + "dBm" +
                    "\nData Rate: " + wifiInfo.getLinkSpeed() + "Mbps" +
                    "\nUniWide Ap Density: " + density +
                    "\nTimestamp: " + timestamp;
        }
    }

    private String intToIp(int i)
    {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);
    }
}
