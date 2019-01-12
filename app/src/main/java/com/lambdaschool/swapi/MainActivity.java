package com.lambdaschool.swapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*(new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<Transport> allTransports = SwApiDao.getAllTransports();
                for(Transport transport: allTransports) {
                    Log.i("Transports Result", transport.toString());
                }
            }
        })).start();*/


        AtomicBoolean canceled = new AtomicBoolean(false);
        SwApiDao.getAllPlanets(canceled, new SwApiDao.ObjectCallback<ArrayList<Planet>>() {
            @Override
            public void returnPlanets(final ArrayList<Planet> planets) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView) findViewById(R.id.output_text_view)).setText(planets.toString());
                    }
                });
            }
        });
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        canceled.set(true);

        /*final long start = System.currentTimeMillis();
        SwApiDao.getAllTransports(new SwApiDao.ObjectCallback<Transport>() {
            @Override
            public void returnPlanets(final Transport transports) {
                Log.i("Transport Timer", Long.toString(System.currentTimeMillis() - start));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView) findViewById(R.id.output_text_view)).append(
                                transports.getClass().getSimpleName() +
                                " - " +
                                transports.getName() +
                                "\n");
                    }
                });
            }
        });*/

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (allPlanets) {
                        allPlanets.wait();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView) findViewById(R.id.output_text_view)).setText(allPlanets.toString());
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }
}
