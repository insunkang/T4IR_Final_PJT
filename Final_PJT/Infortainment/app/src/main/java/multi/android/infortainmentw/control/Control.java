package multi.android.infortainmentw.control;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.PrintWriter;

import multi.android.infortainmentw.MainActivity;
import multi.android.infortainmentw.R;

public class Control extends Fragment {
    PrintWriter pw;
    MainActivity ma;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_control, container, false);

        final Handler[] mHandler = new Handler[2];
        final SeekBar[] seekBar = {view.findViewById(R.id.seekBar), view.findViewById(R.id.seekBar2)};
        seekBar[0].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                mHandler[0] = new Handler() {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        Velocity v1 = view.findViewById(R.id.velocity);
                        v1.dx = v1.cx + (int) (-110 * (Math.cos((progress) * 3.14 / 100d)));
                        v1.dy = v1.cy + (int) (110 * (Math.sin((-progress) * 3.14 / 100d)));
                        v1.invalidate();
                        mHandler[0].sendEmptyMessageDelayed(10, 10);  //핸들러함수 콜 10은 식별번호, 지연시간 500밀리세컨드
                    }
                };
                mHandler[0].sendEmptyMessageDelayed(10, 0); // 버튼을 누를 때 0초 이므로 핸들러 함수 안으로 간다.
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBar[1].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                mHandler[1] = new Handler() {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        Acceleration v1 = view.findViewById(R.id.acceleration);
                        ImageView iv = view.findViewById(R.id.aircontioner);
                        v1.dx = v1.cx + (int) (-110 * (Math.cos((progress) * 3.14 / 100d)));
                        v1.dy = v1.cy + (int) (110 * (Math.sin((-progress) * 3.14 / 100d)));
                        iv.setColorFilter(Color.rgb ((int)(((progress)/100d)*255),0,(int)(((100-progress)/100d)*255)));
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

        return view;
    }
}
