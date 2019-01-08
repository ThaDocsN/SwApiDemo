package com.lambdaschool.swapi;

import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("This is a test", "test");

        /*(new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<Transport> allTransports = SwApiDao.getAllTransports();
                for(Transport transport: allTransports) {
                    Log.i("Transports Result", transport.toString());
                }
            }
        })).start();*/

        /*SwApiDao.getAllTransports(new SwApiDao.TransportCallback() {
            @Override
            public void returnTransports(ArrayList<Transport> objects) {
                Log.i("Transports Result", objects.toString());
            }
        });*/

        final ArrayList<Transport> transports = new ArrayList<>();
        SwApiDao.getAllTransports(transports);
    }
}
