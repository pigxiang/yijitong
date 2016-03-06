package org.hehe7xiao.yijitong;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private YJT user;

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

        handleEditText();
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

    private class WorkThread extends Thread {
        @Override
        public void run() {
            String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
            String password = ((EditText) findViewById(R.id.password)).getText().toString();
            String iccid = ((EditText) findViewById(R.id.iccid)).getText().toString();
            String userid = ((EditText) findViewById(R.id.userid)).getText().toString();

            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("phone", phone);
            editor.putString("password", password);
            editor.putString("iccid", iccid);
            editor.putString("userid", userid);
            editor.commit();

            user = new YJT(phone, password, iccid, userid);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
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
        EditText useridEdit = (EditText) findViewById(R.id.userid);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String phone = sharedPref.getString("phone", "");
        String password = sharedPref.getString("password", "");
        String iccid = sharedPref.getString("iccid", "");
        String userid = sharedPref.getString("userid", "");

        if (!"".equals(phone)) phoneEdit.setText(phone);
        if (!"".equals(password)) passwordEdit.setText(password);
        if (!"".equals(iccid)) iccidEdit.setText(iccid);
        if (!"".equals(userid)) useridEdit.setText(userid);
    }
}
