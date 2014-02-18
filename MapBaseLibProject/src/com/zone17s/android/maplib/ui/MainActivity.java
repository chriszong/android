package com.zone17s.android.maplib.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zone17s.android.maplib.R;

public class MainActivity extends FragmentActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBtn();
    }

    private void initBtn() {
        ((Button) findViewById(R.id.main_btn_to_frag_map)).setOnClickListener(this);
        ((Button) findViewById(R.id.main_btn_to_ctrl_map)).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_to_frag_map:
                Intent toFragMap = new Intent();
                toFragMap.setClass(this, FragMapActivity.class);
                startActivity(toFragMap);
                break;
            case R.id.main_btn_to_ctrl_map:
                Intent toCtrlMap = new Intent();
                toCtrlMap.setClass(this, CtrlMapActivity.class);
                startActivity(toCtrlMap);
                break;
            default:
                break;
        }
    }

}
