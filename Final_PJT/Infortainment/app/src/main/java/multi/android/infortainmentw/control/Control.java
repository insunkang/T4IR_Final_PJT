package multi.android.infortainmentw.control;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import multi.android.infortainmentw.MainActivity;
import multi.android.infortainmentw.R;

public class Control extends Fragment {
    double latitude;
    double longitude;
    double km = 0;
    String temperature = "";
    String humidity = "";
    boolean airconditionerState = true;
    ImageView airconditioner;
    View view;
    int delay=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_control, container, false);
        final Handler[] mHandler = new Handler[2];
        final SeekBar[] seekBar = {view.findViewById(R.id.seekBar), view.findViewById(R.id.seekBar2)};
        airconditioner = view.findViewById(R.id.aircontioner);
        seekBar[0].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                mHandler[1] = new Handler() {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        Seat v1 = view.findViewById(R.id.seat);
                        v1.dx = v1.cx + (int) (-90 * (Math.cos(((100 - progress) / 10 - 70) * 3.14 / 100d)));
                        v1.dy = v1.cy + (int) (90 * (Math.sin(((100 - progress) / 10 - 70) * 3.14 / 100d)));
                        v1.invalidate();
                        mHandler[1].sendEmptyMessageDelayed(10, 10);  //핸들러함수 콜 10은 식별번호, 지연시간 500밀리세컨드
                    }
                };
                mHandler[1].sendEmptyMessageDelayed(10, 0); // 버튼을 누를 때 0초 이므로 핸들러 함수 안으로 간다.
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBar[1].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.progress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler[0] = new Handler() {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        /*
                        속도계 0~100 아까워서 보관
                        Velocity v1 = view.findViewById(R.id.velocity);
                        v1.dx = v1.cx + (int) (-110 * (Math.cos((progress) * 3.14 / 100d)));
                        v1.dy = v1.cy + (int) (110 * (Math.sin((-progress) * 3.14 / 100d)));
                        v1.invalidate();
                        */

                        airconditioner = view.findViewById(R.id.aircontioner);
                        if (airconditionerState == true) {
                            airconditioner.setColorFilter(Color.rgb((int) (((progress) / 100d) * 255), 0, (int) (((100 - progress) / 100d) * 255)));
                        } else {
                            airconditioner.setColorFilter(Color.rgb(0, 0, 0));
                        }
                        mHandler[0].sendEmptyMessageDelayed(20, 10);  //핸들러함수 콜 10은 식별번호, 지연시간 500밀리세컨드
                    }
                };
                mHandler[0].sendEmptyMessageDelayed(20, 0); // 버튼을 누를 때 0초 이므로 핸들러 함수 안으로 간다.
            }
        });

        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                double tempLatitude = latitude;
                double tempLongitude = longitude;
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                double s = Math.acos(Math.cos(Math.toRadians(90 - tempLatitude)) * Math.cos(Math.toRadians(90 - latitude)) + Math.sin(Math.toRadians(90 - tempLatitude)) * Math.sin(Math.toRadians(90 - latitude)) * Math.cos(Math.toRadians(tempLongitude - longitude))) * 6378.137;
                if (s * 3600 > 60000) {

                } else {
                    Log.d("logCheck1", (s * 5760) + "");
                    km += s * 900;
                    Velocity v1 = view.findViewById(R.id.velocity);
                    v1.dx = v1.cx + (int) (-110 * (Math.cos((s * 5760) * 3.14 / 150d)));
                    v1.dy = v1.cy + (int) (110 * (Math.sin((-s * 5760) * 3.14 / 150d)));
                    v1.invalidate();

                    Oil v2 = view.findViewById(R.id.oil);
                    v2.dx = v2.cx + (int) (50 * (Math.cos((km) * 3.14 / 750d)));
                    v2.dy = v2.cy + (int) (50 * (Math.sin((-km) * 3.14 / 750d)));
                    Log.d("logCheck2", v2.dx + "");
                    Log.d("logCheck2", v2.dy + "");
                    Log.d("logCheck2", km + "");
                    v2.invalidate();

                    TextView tvTemp = view.findViewById(R.id.temporature);
                    TextView tvHumi = view.findViewById(R.id.humidity);
                    tvTemp.setText(temperature + "℃");
                    tvHumi.setText(humidity + "％");

                    TextView tvKm = view.findViewById(R.id.km);
                    tvKm.setTextColor(Color.rgb(255, 255, 255));
                    tvKm.setText(Math.round(km) + " m");
                    delay++;
                    if(delay>3) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.pw.println(MainActivity.family + "/" + MainActivity.andId + "/Oil/"+(int)(100d*(750-km)/750d));
                            }
                        }).start();
                        delay=0;
                    }
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
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);

        final Button right_btn = view.findViewById(R.id.right_btn);
        right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        for (int i = 0; i < 5; i++) {
                            publishProgress();
                            SystemClock.sleep(600);
                        }
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(String... values) {
                        if (view.findViewById(R.id.ff).getVisibility() == View.INVISIBLE) {
                            view.findViewById(R.id.ff).setVisibility(View.VISIBLE);
                            Log.d("test", "check");
                        } else {
                            view.findViewById(R.id.ff).setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        view.findViewById(R.id.ff).setVisibility(View.INVISIBLE);
                    }
                }.execute();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.pw.println(MainActivity.family + "/" + MainActivity.andId+"/LED/rightLight");
                    }
                });
                thread.start();
            }
        });

        final Button left_btn = view.findViewById(R.id.left_btn);
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        for (int i = 0; i < 5; i++) {
                            publishProgress();
                            SystemClock.sleep(600);
                        }
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(String... values) {
                        if (view.findViewById(R.id.rew).getVisibility() == View.INVISIBLE) {
                            view.findViewById(R.id.rew).setVisibility(View.VISIBLE);
                            Log.d("test", "check");
                        } else {
                            view.findViewById(R.id.rew).setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        view.findViewById(R.id.rew).setVisibility(View.INVISIBLE);
                    }
                }.execute();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.pw.println(MainActivity.family + "/" + MainActivity.andId+"/LED/leftLight");
                    }
                });
                thread.start();
            }
        });

        Button emergency_btn = view.findViewById(R.id.emergency_btn);
        emergency_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.pw.println(MainActivity.family + "/" + MainActivity.andId+"/LED/emergency");
                    }
                });
                thread.start();
            }
        });

        final ImageView airconditioner = view.findViewById(R.id.aircontioner);
        airconditioner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (airconditionerState == true) {
                    airconditioner.setColorFilter(Color.rgb(0, 0, 0));
                    airconditionerState = false;
                    Log.d("check1", airconditionerState + "");
                } else {
                    airconditioner.setColorFilter(Color.rgb((int) (((seekBar[1].getProgress()) / 100d) * 255), 0, (int) (((100 - seekBar[1].getProgress()) / 100d) * 255)));
                    airconditionerState = true;
                    Log.d("check1", airconditionerState + "");
                }
            }
        });
        return view;
    }

    public void setKm(double km) {
        this.km = km;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setSeat(int value){
        SeekBar sb = view.findViewById(R.id.seekBar);
        sb.setProgress(value);
    }
    public void setAirconditioner(int value,boolean state){
        airconditionerState = state;
        SeekBar airconSB = view.findViewById(R.id.seekBar2);
        if(state){
            airconSB.setProgress(value);
            airconditioner.setColorFilter(Color.rgb((int) (((value) / 100d) * 255), 0, (int) (((100 - value) / 100d) * 255)));
        } else {
            airconSB.setProgress(value);
            airconditioner.setColorFilter(Color.rgb(0,0,0));

        }
    }
    public String saveState(){
        SeekBar airconSB = view.findViewById(R.id.seekBar2);
        String airconditioner =airconSB.getProgress()+"";
        SeekBar seatSB = view.findViewById(R.id.seekBar);
        String seat = seatSB.getProgress()+"";
        String lat = latitude+"";
        String lon = longitude+"";
        return "airconditioner/"+airconditioner+"/"+"seat/"+seat+"/lat/"+lat+"/lon/"+lon;
    }
}
