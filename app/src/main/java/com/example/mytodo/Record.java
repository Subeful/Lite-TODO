package com.example.mytodo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodo.Adapter.RecordAdapter;
import com.example.mytodo.Model.PackageModel;

import java.util.ArrayList;
import java.util.List;

public class Record extends AppCompatActivity {

    public static Context context;

    static RecyclerView recycler;
    static RecordAdapter adapter;
    static public List<PackageModel> list = new ArrayList<>();

    TextView name;

    public static SharedPreferences preferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_record);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        preferences = getSharedPreferences("Record", MODE_PRIVATE);
        context = Record.this;
        recycler = findViewById(R.id.RecordRecycler);
        name = findViewById(R.id.recordNames);
        name.setText(getIntent().getStringExtra("Package") + "/" + getIntent().getStringExtra("Category"));
        setApplicationRecycle(list);

    }

    @SuppressLint("ResourceAsColor")
    public void addPackage(View v){
        list.add(new PackageModel(list.size()+1, ""));
        try {
            adapter.setList(list);
            recycler.setAdapter(adapter);
        } catch (Exception e) {}
    }


    private void setApplicationRecycle(List<PackageModel> listApp) {
        try {
            GridLayoutManager layoutManagers = new GridLayoutManager(this, 1);
            recycler.setLayoutManager(layoutManagers);
            adapter = new RecordAdapter(this, listApp);
            recycler.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(this, "error in recycler", Toast.LENGTH_SHORT).show();
        }
    }

}