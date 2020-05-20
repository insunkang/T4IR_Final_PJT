package multi.android.infortainmentw.navi;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.MapView;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import multi.android.infortainmentw.MainActivity;
import multi.android.infortainmentw.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NaviFragment extends Fragment {
    MapView mapView = null;
    TMapView tMapView;
    Context mContext = null;
    String apiKey = "l7xxfce37b4d926e4607a7d14ba4bba09475";
    TMapGpsManager tMapGpsManager = null;
    //LocationManager locationManager = mContext;
    private boolean m_bTrackingMode = true;

    public NaviFragment() {

    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getActivity(), "앱 실행을 위해서는 위치 권한이 필요합니다", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            View view = inflater.inflate(R.layout.fragment_navi_main, container, false);
            return view;

        } else {
            final NaviAsyncTask naviAsyncTask = new NaviAsyncTask();
            final Button button;
            Button btn;
            View rootView = inflater.inflate(R.layout.fragment_navi, container, false);
            mapView = rootView.findViewById(R.id.map);
            tMapView = new TMapView(getActivity());
            tMapGpsManager = new TMapGpsManager(getActivity());
            tMapGpsManager.setMinTime(1000);
            tMapGpsManager.setMinDistance(1);
            tMapGpsManager.setProvider(tMapGpsManager.NETWORK_PROVIDER);
            tMapGpsManager.OpenGps();
            TMapPoint point = tMapGpsManager.getLocation();
            tMapView.setIconVisibility(true);
            tMapView.setSightVisible(true);
            tMapView.setMapPosition(TMapView.POSITION_NAVI);
            tMapView.setSKTMapApiKey(apiKey);
            tMapView.setCompassMode(true);
            tMapView.setZoomLevel(15);
            tMapView.setMapType(TMapView.MAPTYPE_HYBRID);  //일반지도
            tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
            tMapView.setTrackingMode(true);
            mContext = getContext();
            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    tMapView.setLocationPoint(longitude, latitude);


                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            // locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


            button = rootView.findViewById(R.id.road);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    naviAsyncTask.execute();
                    button.setEnabled(false);
                }
            });
            btn = rootView.findViewById(R.id.findAddress);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "message", Toast.LENGTH_LONG).show();
                    ((MainActivity) getActivity()).replaceFragmentFindAddress();

                }
            });
            mapView.addView(tMapView);
            return rootView;

        }

    }


    class NaviAsyncTask extends AsyncTask<Void, TMapPoint, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... integer) {
            int a = 0;
            Boolean isRunning = true;
            TMapData tMapData = new TMapData();
            while (isRunning) {
                TMapPoint point = tMapGpsManager.getLocation();
                TMapPoint tMapPointStart = new TMapPoint(37.570841, 126.985302); // SKT타워(출발지)

                publishProgress(point);

                try {

                    TMapPolyLine tMapPolyLine = new TMapData().findPathData(tMapPointStart, point);
                    tMapPolyLine.setLineColor(Color.BLUE);
                    tMapPolyLine.setLineWidth(2);
                    tMapView.addTMapPolyLine("Line1", tMapPolyLine);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                return String.valueOf(a);
            }
            return String.valueOf(a);
        }
        @Override
        protected void onProgressUpdate(TMapPoint... values) {
            super.onProgressUpdate(values);

        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
        }

    }

}
