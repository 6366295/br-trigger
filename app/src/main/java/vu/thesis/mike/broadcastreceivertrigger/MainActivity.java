package vu.thesis.mike.broadcastreceivertrigger;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        PackageManager pm = getPackageManager();
//        ComponentName cn = new ComponentName(this, vu.thesis.mike.broadcastreceivertrigger.MainActivity.class);
//        pm.setComponentEnabledSetting(cn, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        Toast.makeText(this, "Registered BRTrigger", Toast.LENGTH_SHORT).show();

        finish();
    }
}
