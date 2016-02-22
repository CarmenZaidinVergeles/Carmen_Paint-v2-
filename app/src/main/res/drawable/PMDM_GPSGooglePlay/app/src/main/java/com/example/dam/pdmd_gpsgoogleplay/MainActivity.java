package drawable.PMDM_GPSGooglePlay.app.src.main.java.com.example.dam.pdmd_gpsgoogleplay;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.sql.Connection;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,com.google.android.gms.location.LocationListener {

    private final int CTEPLAY=1;
    private Location ultimaLocalizacion;
    private GoogleApiClient cliente;
    private LocationRequest peticionLocalizaciones;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv= (TextView) findViewById(R.id.textView);
        ini();
    }

    @Override
    protected void onStart() {
        super.onStart();
        cliente.connect();
        Log.v("Buscar", "onStart");
    }



    @Override
    protected void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(cliente, this);
        Log.v("Buscar", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        cliente.disconnect();
        Log.v("Buscar", "onStop");
    }

    @Override
    public void onConnected(Bundle bundle) {
        ultimaLocalizacion = LocationServices.
                FusedLocationApi.getLastLocation(cliente);
        if (ultimaLocalizacion != null) {

        }
        peticionLocalizaciones = new LocationRequest();
        peticionLocalizaciones.setInterval(10000);
        peticionLocalizaciones.setFastestInterval(5000);
        peticionLocalizaciones.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(cliente, peticionLocalizaciones, this);
    }

    private void ini(){
        int status=GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(status== ConnectionResult.SUCCESS) {
            cliente = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            cliente.connect();
        }else if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
            GooglePlayServicesUtil.getErrorDialog(status, this, CTEPLAY).show();
        } else {
            Toast.makeText(this, "No", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CTEPLAY) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "No", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        String a=""+location.getAccuracy();
        String b=""+location.getAltitude();
        String c=""+location.getLatitude();
        String d=""+location.getLongitude();
        String e=""+location.getProvider();
        tv.append("\n"+a+" "+b+" "+c+" "+d+" "+e);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
