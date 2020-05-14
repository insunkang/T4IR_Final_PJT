package multi.android.infortainmentw;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import multi.android.infortainmentw.control.Control;

public class MainActivity extends AppCompatActivity {
    Control control = new Control();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager;
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction;
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_control,control);
    }
}
