package multi.android.infortainmentw.navi;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
public class NaviAsync extends Fragment {
    MapView mapView=null;
    TMapView tMapView;
    Context mContext = null;
    String apiKey ="l7xxfce37b4d926e4607a7d14ba4bba09475";
    TMapGpsManager tMapGpsManager =null;
    //LocationManager locationManager = mContext;
    private boolean m_bTrackingMode =true;
    public NaviAsync() {

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
            Button button ;
            Button btn;
            final NaviAsyncTask naviAsyncTask = new NaviAsyncTask();



            View rootView = inflater.inflate(R.layout.fragment_navi,container,false);
            mapView = rootView.findViewById(R.id.map);
            tMapView = new TMapView(getActivity());
            tMapGpsManager = new TMapGpsManager(getActivity());
            tMapGpsManager.setMinTime(1000);
            tMapGpsManager.setMinDistance(5);
            tMapGpsManager.setProvider(tMapGpsManager.NETWORK_PROVIDER);
            tMapGpsManager.OpenGps();
             TMapPoint point = tMapGpsManager.getLocation();

            tMapView.setIconVisibility(true);
            tMapView.setSightVisible(true);
            tMapView.setMapPosition(TMapView.POSITION_NAVI);
            tMapView.setSKTMapApiKey(apiKey);
            tMapView.setCompassMode(true);
            tMapView.setZoomLevel(15);
            tMapView.setMapType(TMapView.MAPTYPE_STANDARD);  //일반지도
            tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
            tMapView.setTrackingMode(true);
            mContext = getContext();

            button=rootView.findViewById(R.id.road);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    naviAsyncTask.execute();

                }
            });
            btn =rootView.findViewById(R.id.findAddress);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ((MainActivity)getActivity()).replaceFragmentFindAddress();

                }
            });
         mapView.addView(tMapView);
        return rootView;
        }



    class NaviAsyncTask extends  AsyncTask<Void,TMapPoint,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... integer) {
            int a =0;
           Boolean isRunning =true;
            /*if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
                    &&ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){*/


            while(isRunning) {
                TMapPoint point = tMapGpsManager.getLocation();
                //tMapView.setLocationPoint(point.getLongitude(),point.getLatitude());
                publishProgress(point);
                return String.valueOf(a);
            }
            return String.valueOf(a);
        }


        @Override
        protected void onProgressUpdate(TMapPoint... values) {
            super.onProgressUpdate(values);
            tMapView.setLocationPoint(values[0].getLongitude(),values[0].getLatitude());
            TMapPoint tMapPointStart = new TMapPoint(37.570841, 126.985302); // SKT타워(출발지)
            try {
                TMapPolyLine tMapPolyLine = new TMapData().findPathData(tMapPointStart, values[0]);
                Toast.makeText(getContext(), (int) values[0].getLatitude(),Toast.LENGTH_LONG).show();
                tMapPolyLine.setLineColor(Color.BLUE);
                tMapPolyLine.setLineWidth(2);
                tMapView.addTMapPolyLine("Line1", tMapPolyLine);


            } catch (Exception e) {
                e.printStackTrace();
            }
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
