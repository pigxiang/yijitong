package org.hehe7xiao.yijitong;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private YJT user;
    private List<Geo> geos = Data.geos;
    private Geo geo = geos.get(0);
    private PendingIntent pendingIntent;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                tv.setText(msg.getData().getString("result"));
            }
        }
    };

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);

        handleSpinner();
        handleEditText();
        handleButton();
        startAlarm();
        tv = (TextView) findViewById(R.id.tv);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setText("开始签到");
                new WorkThread().start();
            }
        });
    }

    public Activity getActivity() {
        return this;
    }

    private String readIccid() {
        String iccid = "";
        try {
            TelephonyManager tm = (TelephonyManager) MainActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
            iccid = tm.getSimSerialNumber();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iccid;
    }

    private class WorkThread extends Thread {
        @Override
        public void run() {
            String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
            String password = ((EditText) findViewById(R.id.password)).getText().toString();
            String iccid = ((EditText) findViewById(R.id.iccid)).getText().toString();

            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
//            String iccid = sharedPref.getString("iccid", "");
//
//            if ("".equals(iccid)) {
//                iccid = readIccid();
//            }

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("phone", phone);
            editor.putString("password", password);
            editor.putString("iccid", iccid);
            editor.commit();

            user = new YJT(phone, password, iccid, geo);
//            user = new YJT(phone, password, iccid);
            String result = user.attendance();

            Message msg = new Message();
            msg.what = 0;
            Bundle bundle = new Bundle();
            bundle.putString("result", result);
            msg.setData(bundle);
            handler.sendMessage(msg);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_about:
                Toast.makeText(MainActivity.this, R.string.version, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void handleEditText() {
        EditText phoneEdit = (EditText) findViewById(R.id.phone);
        EditText passwordEdit = (EditText) findViewById(R.id.password);
        EditText iccidEdit = (EditText) findViewById(R.id.iccid);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String phone = sharedPref.getString("phone", "");
        String password = sharedPref.getString("password", "");
        String iccid = sharedPref.getString("iccid", "");

        if (!"".equals(phone)) phoneEdit.setText(phone);
        if (!"".equals(password)) passwordEdit.setText(password);
        if (!"".equals(iccid)) iccidEdit.setText(iccid);
    }

    private void handleSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.geo);
        ArrayAdapter<Geo> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, geos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                geo = geos.get(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    private void handleButton() {
        Button button = (Button) findViewById(R.id.auto_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String iccid = readIccid();
                EditText iccidEdit = (EditText) findViewById(R.id.iccid);
                iccidEdit.setText(iccid);
            }
        });

    }

    private void startAlarm() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 1000 * 10;

        /* Set the alarm to start at 10:30 AM */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 2);
        calendar.set(Calendar.MINUTE, 58);

        /* Repeating on every 20 minutes interval */
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                interval, pendingIntent);
    }
}
