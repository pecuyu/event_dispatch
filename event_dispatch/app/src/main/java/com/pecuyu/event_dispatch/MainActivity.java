package com.pecuyu.event_dispatch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyListView listView = (MyListView) findViewById(R.id.lv);
        ArrayList<Map<String, String>> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            HashMap<String, String> item = new HashMap<>();
            item.put("name", "this is " + i);
            data.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_1, new String[]{"name"}, new int[]{android.R.id.text1});
        listView.setAdapter(adapter);
    }


    public void ripple(View view) {
        Toast.makeText(getApplicationContext(),"ripple click",Toast.LENGTH_SHORT).show();

    }
}
