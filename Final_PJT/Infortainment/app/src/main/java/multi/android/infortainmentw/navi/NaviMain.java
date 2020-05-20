package multi.android.infortainmentw.navi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import multi.android.infortainmentw.MainActivity;
import multi.android.infortainmentw.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NaviMain extends Fragment {

    public NaviMain() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Button mapBtn;
        View mainView = inflater.inflate(R.layout.fragment_navi_main,container,false);
        mapBtn = mainView.findViewById(R.id.openMap);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragmentMap();

            }
        });
        return mainView;
    }
}
