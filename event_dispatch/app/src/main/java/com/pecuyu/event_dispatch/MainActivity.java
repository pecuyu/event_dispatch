package com.pecuyu.event_dispatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lv = (ListView) findViewById(R.id.lv);
        ArrayList<Map<String, String>> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            HashMap<String, String> item = new HashMap<>();
            item.put("name", "this is " + i);
            data.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.listitem, new String[]{"name"}, new int[]{R.id.name});
        lv.setAdapter(adapter);
    }
}
