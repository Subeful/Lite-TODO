package com.example.mytodo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodo.Adapter.CategoryAdapter;
import com.example.mytodo.Model.PackageModel;

import java.util.ArrayList;
import java.util.List;

public class Category extends AppCompatActivity {

    public  static Context context;

    static RecyclerView recycler;
    static CategoryAdapter adapter;
    static public List<PackageModel> list = new ArrayList<>();

    public static TextView name;

    static int x = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setUI();
        setList();
        setApplicationRecycle(list);

    }
    private void setUI(){
        try {
            context = Category.this;
            recycler = findViewById(R.id.CategoryRecycler);
            name = findViewById(R.id.categoryNames);
            name.setText(getIntent().getStringExtra("Package"));
        }catch (Exception e){Toast.makeText(context, "set UI", Toast.LENGTH_SHORT).show();}
    }
//    public void addPackage(View v){
//        AlertHelper.createAlert(context, "Добавить категорию");
//    }

    public static void createNewPackage(EditText userInput){
        if(!userInput.getText().toString().isEmpty()){
            list.add(new PackageModel(list.size()+1, userInput.getText().toString()));
            try {
                adapter.setList(list);
                recycler.setAdapter(adapter);
            } catch (Exception e) {}
        }
    }


    private void setApplicationRecycle(List<PackageModel> listApp) {
        try {
            GridLayoutManager layoutManagers = new GridLayoutManager(this, 1);
            recycler.setLayoutManager(layoutManagers);
            adapter = new CategoryAdapter(this, listApp);
            recycler.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(this, "error in recycler", Toast.LENGTH_SHORT).show();
        }
    }

    private void setList(){
        if(x == 0){
            list.add(new PackageModel(1, "Домой"));
            list.add(new PackageModel(2, "Подарки"));
            list.add(new PackageModel(3, "Разное"));
            x = 1;
        }
    }
}