package com.example.mytodo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import com.example.mytodo.Adapter.PackageModelAdapter;
import com.example.mytodo.DB.Constants;
import com.example.mytodo.DB.DbHelper;
import com.example.mytodo.DB.DbManager;
import com.example.mytodo.Model.PackageModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Context context;

    static RecyclerView recycler;
    static PackageModelAdapter adapter;


    static DbManager dbManager;

    static public int packageId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setUI();
        dbManager = new DbManager(context);
        setApplicationRecycle(new ArrayList<>());

    }

    private void setUI(){
        context = MainActivity.this;
        recycler = findViewById(R.id.PackageRecycler);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbManager.openDb();
        if(dbManager.getLastPackageId(Constants.PACkAGE) != -1)
            packageId = dbManager.getLastPackageId(Constants.PACkAGE);
        setApplicationRecycle(getPackageFromDB());

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.closeDb();
    }

    private void setApplicationRecycle(List<PackageModel> listApp) {
        try {
            GridLayoutManager layoutManagers = new GridLayoutManager(this, 1);
            recycler.setLayoutManager(layoutManagers);
            adapter = new PackageModelAdapter(this, listApp, dbManager);
            recycler.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(this, "error in recycler", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("Range")
    private static List<PackageModel> getPackageFromDB(){
        List<PackageModel> dbList = new ArrayList<>();

        for(String s: dbManager.getPackageNamesList()){
            dbList.add(new PackageModel(packageId++, s));
        }

        return dbList;
    }



public void addPackage(View v){
        View promptsView = LayoutInflater.from(context).inflate(R.layout.add_package, null);

        TextView textView = promptsView.findViewById(R.id.alert_tital);
        textView.setText("Добавить папку");

        final EditText userInput = (EditText) promptsView.findViewById(R.id.alert_text);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
        mDialogBuilder.setView(promptsView);

        mDialogBuilder.setCancelable(false)
                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                if(!userInput.getText().toString().isEmpty()){
                                    try {
                                        addAllStructure(userInput.getText().toString());
                                    } catch (Exception e) {
                                        Toast.makeText(context, "error add", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.back_2);
        alertDialog.show();
    }
    private void addAllStructure(String text) {

        if(!isExistTextInBd(text)){
            dbManager.insertToPackage(packageId, text);
            updateAdapter();
            Toast.makeText(context, "id: " + String.valueOf(packageId-1) + "\ntext: " + text, Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(context, "Такая папка уже есть", Toast.LENGTH_SHORT).show();

    }

    private boolean isExistTextInBd(String text){
        for(String name: dbManager.getPackageNamesList()){
            if((name.toLowerCase().trim()).equals(text.toLowerCase().trim())) return true;
        }
        return false;
    }


    public static void updateAdapter(){
        adapter.setList(getPackageFromDB());
        recycler.setAdapter(adapter);

    }

}