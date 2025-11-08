package com.example.aidldemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aidldemo.databinding.ActivityMainBinding;
import com.example.servicemodule.IRandomNumberGenerator;

/**
 * The main activity of the application. This activity demonstrates how to bind to a remote service
 * and interact with it using AIDL.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private IRandomNumberGenerator randomNumberGenerator;
    private boolean isServiceBound = false;
    private ActivityMainBinding binding;

    /**
     * ServiceConnection to handle the connection to the remote service.
     */
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "Service connected.");
            randomNumberGenerator = IRandomNumberGenerator.Stub.asInterface(service);
            isServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "Service disconnected.");
            randomNumberGenerator = null;
            isServiceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.startServiceButton.setOnClickListener(v -> {
            Log.d(TAG, "Start Service button clicked. package name: " + getPackageName());
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(getPackageName(), "com.example.servicemodule.MyService"));
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        });

        binding.getRandomNumberButton.setOnClickListener(v -> {
            Log.d(TAG, "Get Random Number button clicked.");
            if (isServiceBound) {
                try {
                    int randomNumber = randomNumberGenerator.getRandomNumber();
                    Log.d(TAG, "Random number received: " + randomNumber);
                    binding.textView.setText(getString(R.string.random_number_is, randomNumber));
                } catch (RemoteException e) {
                    Log.e(TAG, getString(R.string.error_getting_random_number), e);
                    Toast.makeText(this, getString(R.string.error_getting_random_number), Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.w(TAG, getString(R.string.service_not_bound));
                Toast.makeText(this, getString(R.string.service_not_bound), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isServiceBound) {
            unbindService(serviceConnection);
            isServiceBound = false;
        }
    }
}
