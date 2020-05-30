package multi.android.infortainmentw.navi;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.MapView;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import multi.android.infortainmentw.MainActivity;
import multi.android.infortainmentw.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NaviFragment extends Fragment {

    MapView mapView = null;
    public TMapView tMapView;
    Context mContext = null;
    String apiKey = "l7xxfce37b4d926e4607a7d14ba4bba09475";
    TMapGpsManager tMapGpsManager = null;
    double latitude;
    double longitude;
    EditText keywordView;
    ArrayAdapter<POI> mAdapter;
    ListView listView;
    TMapPoint start, end;
    Socket socket;
    InputStream is;
    DataInputStream dis;
    InputStreamReader isr;
    BufferedReader br;
    OutputStream os;
    PrintWriter pw;
    String ip = "70.12.224.148";
    int port = 33336;
    View rootView;
    NaviFragment naviFragment;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        //if 권한설정 else 기능실행
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getActivity(), "앱 실행을 위해서는 위치 권한이 필요합니다", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            View view = inflater.inflate(R.layout.fragment_navi_main, container, false);
            return view;

        } else {
            final NaviAsyncTask naviAsyncTask = new NaviAsyncTask();
            final Button button; // 길찾기
            Button btn; // 목적지설정
            rootView = inflater.inflate(R.layout.fragment_navi, container, false);
            keywordView = rootView.findViewById(R.id.edit_keyword);

            mapView = rootView.findViewById(R.id.map);
            tMapView = new TMapView(getActivity());
            tMapGpsManager = new TMapGpsManager(getActivity());
            tMapGpsManager.setMinTime(1000);
            tMapGpsManager.setMinDistance(1);
            //tMapGpsManager.setProvider(tMapGpsManager.NETWORK_PROVIDER);
            tMapGpsManager.setProvider(tMapGpsManager.GPS_PROVIDER);
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
            listView = rootView.findViewById(R.id.listView);

            mAdapter = new ArrayAdapter<POI>(getActivity(), android.R.layout.simple_list_item_1);
            ArrayList<TMapPoint> alTMapPoint = new ArrayList<TMapPoint>();
            listView.setAdapter(mAdapter);


//            StartPointListener startPointListener = new StartPointListener();
//
//
//            listView.setOnItemClickListener(startPointListener);

            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();


                    boolean isTracking = tMapView.getIsTracking();
                    if (isTracking == true) {
                        tMapView.setLocationPoint(longitude, latitude);
                        tMapView.setMapPosition(TMapView.POSITION_DEFAULT);
                    }

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
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


            tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
                @Override
                public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
                    String message = null;

                    start = tMapMarkerItem.getTMapPoint();
                    message = "start";


                    Toast.makeText(getActivity(), message + " setting" + start.getLongitude() + "" + start.getLatitude(), Toast.LENGTH_SHORT).show();
                }
            });
            btn = rootView.findViewById(R.id.btn_search);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    searchPOI();
                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    tMapView.setTrackingMode(false);

                    POI poi = (POI) listView.getItemAtPosition(position);
                    //tMapView.setLocationPoint(poi.item.getPOIPoint().getLatitude(), poi.item.getPOIPoint().getLongitude());
//                    String a = String.valueOf(poi.item.getPOIPoint().getLatitude());
//                    String b = String.valueOf(poi.item.getPOIPoint().getLongitude());

                    tMapView.setCenterPoint(poi.item.getPOIPoint().getLongitude(), poi.item.getPOIPoint().getLatitude(), true);
                    //tMapView.setCenterPoint(poi.item.getPOIPoint().getLatitude(), poi.item.getPOIPoint().getLongitude());
                    return;

                }
            });

            btn = rootView.findViewById(R.id.road);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tMapView.setTrackingMode(true);
                    if (start != null) {
                        TMapPoint point = tMapGpsManager.getLocation();
                        searchRoute(point, start);
                        start = end = null;
                    } else {
                        Toast.makeText(getActivity(), "start or end is null", Toast.LENGTH_SHORT).show();
                    }
                }
            });

//            button = rootView.findViewById(R.id.road);
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    naviAsyncTask.execute();
//                    button.setEnabled(false);
//                }
//            });
//            btn = rootView.findViewById(R.id.findAddress);
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(getContext(), "message", Toast.LENGTH_LONG).show();
//                    ((MainActivity) getActivity()).replaceFragmentFindAddress();
//
//                }
//            });

            mapView.addView(tMapView);
            return rootView;

        }

    }

    //==================검색기능=============================
    private void searchPOI() {
        TMapData data = new TMapData();
        String keyword = keywordView.getText().toString();
        if (!TextUtils.isEmpty(keyword)) {
            data.findAllPOI(keyword, new TMapData.FindAllPOIListenerCallback() {
                @Override
                public void onFindAllPOI(final ArrayList<TMapPOIItem> arrayList) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tMapView.removeAllMarkerItem();
                            mAdapter.clear();

                            for (TMapPOIItem poi : arrayList) {
                                addMarker(poi);
                                mAdapter.add(new POI(poi));
                            }

                            if (arrayList.size() > 0) {
                                TMapPOIItem poi = arrayList.get(0);
                                tMapView.moveToZoomPosition(poi.getPOIPoint().getLatitude(), poi.getPOIPoint().getLongitude());

                            }
                        }
                    });
                }
            });
        }
    }

    public void addMarker(TMapPOIItem poi) {
        TMapMarkerItem item = new TMapMarkerItem();
        item.setTMapPoint(poi.getPOIPoint());
        Bitmap icon = ((BitmapDrawable) ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_menu_compass)).getBitmap();
        item.setIcon(icon);
        item.setPosition(0.5f, 1);
        item.setCalloutTitle(poi.getPOIName());
        //item.setCalloutSubTitle(poi.getPOIContent());
        Bitmap left = ((BitmapDrawable) ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_dialog_alert)).getBitmap();
        item.setCalloutLeftImage(left);
        Bitmap right = ((BitmapDrawable) ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_input_get)).getBitmap();
        item.setCalloutRightButtonImage(right);
        item.setCanShowCallout(true);

        tMapView.addMarkerItem(poi.getPOIID(), item);

    }

    public void addMarker(double lat, double lng, String title) {
       /* TMapMarkerItem item = new TMapMarkerItem();
        TMapPoint point = new TMapPoint(lat,lng);


        //Bitmap icon = ((BitmapDrawable) ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_menu_compass)).getBitmap();
        Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(), android.R.drawable.ic_menu_compass);
        item.setIcon(icon);
        item.setPosition(0.5f, 1);
        item.setTMapPoint(point);
        item.setCalloutTitle(title);
        //item.setCalloutSubTitle("sub " + title);
        //Bitmap left = ((BitmapDrawable) ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_dialog_alert)).getBitmap();
        Bitmap left = BitmapFactory.decodeResource(getContext().getResources(), android.R.drawable.ic_dialog_alert);
        item.setCalloutLeftImage(left);
        //Bitmap right = ((BitmapDrawable) ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_input_get)).getBitmap();
        Bitmap right = BitmapFactory.decodeResource(getContext().getResources(), android.R.drawable.ic_input_get);
        item.setCalloutRightButtonImage(right);
        item.setCanShowCallout(true);

        tMapView.addMarkerItem("m" , item);
        Log.d("chat",tMapView.getMarkerItemFromID("m").toString());

        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        Log.d("chat","addmarker");*/
        TMapPoint destination = new TMapPoint(lat, lng);

        tMapView.setTrackingMode(true);
        if (destination != null) {
            TMapPoint point = tMapGpsManager.getLocation();
            searchRoute(point, destination);
            destination = null;

        } else {
            Toast.makeText(getActivity(), "start or end is null", Toast.LENGTH_SHORT).show();
        }
    }

    boolean isInitialized = false;

    private void setupMap() {


    }

    //
//    class StartPointListener implements AdapterView.OnItemClickListener{
//
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Toast.makeText(getContext(), "message", Toast.LENGTH_LONG).show();
//            tMapView.removeAllMarkerItem();
//            mAdapter.clear();
//           //addMarker(poi);
//           // mAdapter.add(new POI(poi));
//        }
//
//
//    }
    private void searchRoute(TMapPoint start, TMapPoint end) {
        TMapData data = new TMapData();
        data.findPathData(start, end, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(final TMapPolyLine path) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        path.setLineWidth(5);
                        path.setLineColor(Color.RED);
                        tMapView.addTMapPath(path);
                        Bitmap s = ((BitmapDrawable) ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_input_delete)).getBitmap();
                        Bitmap e = ((BitmapDrawable) ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_input_get)).getBitmap();
                        tMapView.setTMapPathIcon(s, e);

                    }
                });
            }
        });
    }

    //===============================================

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
                SystemClock.sleep(10000);
                TMapPoint point = tMapGpsManager.getLocation();
                TMapPoint tMapPointEnd = new TMapPoint(37.570841, 126.985302); // SKT타워(출발지)
                Log.d("check", "여기");
                publishProgress(point);
                try {
                    TMapPolyLine tMapPolyLine = new TMapData().findPathData(tMapPointEnd, point);
                    tMapPolyLine.setLineColor(Color.BLUE);
                    tMapPolyLine.setLineWidth(2);
                    tMapView.addTMapPolyLine("Line1", tMapPolyLine);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //리턴은 한번만 경로호출을 한번만 실행할 때 활성화;
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
