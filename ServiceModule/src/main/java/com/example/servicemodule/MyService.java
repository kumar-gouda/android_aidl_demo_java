package com.example.servicemodule;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.Random;

/**
 * A service that provides a random number to clients.
 */
public class MyService extends Service {

    private static final String TAG = "MyService";
    private final Random randomNumberGenerator = new Random();

    /**
     * Implementation of the AIDL interface.
     */
    private final IRandomNumberGenerator.Stub binder = new IRandomNumberGenerator.Stub() {
        /**
         * Returns a random integer between 0 and 99.
         *
         * @return A random integer.
         * @throws RemoteException if the remote call fails.
         */
        @Override
        public int getRandomNumber() throws RemoteException {
            int randomNumber = randomNumberGenerator.nextInt(100);
            Log.d(TAG, getString(R.string.generated_random_number, randomNumber));
            return randomNumber;
        }
    };

    /**
     * Called when the service is being created.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, getString(R.string.service_created));
    }

    /**
     * Returns the binder implementation.
     *
     * @param intent The Intent that was used to bind to this service.
     * @return The IBinder through which clients can call on to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, getString(R.string.service_bound));
        return binder;
    }

    /**
     * Called when all clients have unbound from a particular interface published by the service.
     *
     * @param intent The Intent that was used to bind to this service.
     * @return true if you would like to have the service's onCreate() method called again to create a new instance of the service to handle future binds.
     */
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, getString(R.string.service_unbound));
        return super.onUnbind(intent);
    }

    /**
     * Called when the service is no longer used and is being destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, getString(R.string.service_destroyed));
    }
}
