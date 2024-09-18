package com.example.mytodo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.example.mytodo.Adapter.CategoryAdapter;
import com.example.mytodo.DB.DbManager;
import com.example.mytodo.Model.PackageModel;

import java.util.ArrayList;
import java.util.List;

public class Category extends AppCompatActivity {

    static Context context;

    static RecyclerView recycler;
    static CategoryAdapter adapter;

    static DbManager dbManager;

    public TextView name;
    public static int categoryId = 0;

    public static int PC_ID_R;

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
        dbManager = new DbManager(context);
        setApplicationRecycle(new ArrayList<>());

    }
    private void setUI(){
        try {
            context = Category.this;
            recycler = findViewById(R.id.CategoryRecycler);
            name = findViewById(R.id.categoryNames);
            name.setText(getIntent().getStringExtra("Package"));
        }catch (Exception e){Toast.makeText(context, "set UI", Toast.LENGTH_SHORT).show();}
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbManager.openDb();

        PC_ID_R = dbManager.getPackageId(name.getText().toString());
        Toast.makeText(context, "PC_ID_R - " + String.valueOf(PC_ID_R), Toast.LENGTH_SHORT).show();

        updateAdapter();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.closeDb();
    }
//
    private void setApplicationRecycle(List<PackageModel> listApp) {
        try {
            GridLayoutManager layoutManagers = new GridLayoutManager(this, 1);
            recycler.setLayoutManager(layoutManagers);
            adapter = new CategoryAdapter(this, listApp, dbManager);
            recycler.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(this, "error in recycler", Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("Range")
    private static List<PackageModel> getCategoryFromDB(){

        List<PackageModel> dbList = new ArrayList<>();
        try {
            categoryId = 0;
            for(String s: dbManager.getCategoryNamesList(PC_ID_R)){
                dbList.add(new PackageModel(categoryId++, s));
            }

        }catch (Exception e){
            Toast.makeText(context, "error getCategoryFromDB", Toast.LENGTH_SHORT).show();
        }


        return dbList;
    }

    public void addCategory(View v){
        View promptsView = LayoutInflater.from(context).inflate(R.layout.add_package, null);

        TextView textView = promptsView.findViewById(R.id.alert_tital);
        textView.setText("Добавить категорию");

        final EditText userInput = (EditText) promptsView.findViewById(R.id.alert_text);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
        mDialogBuilder.setView(promptsView);

        mDialogBuilder.setCancelable(false)
                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        if(!userInput.getText().toString().isEmpty())
                            addAllStructureCategory(userInput.getText().toString());
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {dialog.cancel();}});

        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.back_2);
        alertDialog.show();
    }
    private void addAllStructureCategory(String text) {
        try {

            if(!isExistText(text)){
                dbManager.insertToCategory(dbManager.getCategoryLastIndex(), text, PC_ID_R);
                updateAdapter();
                Toast.makeText(context, "last index - " + String.valueOf(dbManager.getCategoryLastIndex()), Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "id: " + String.valueOf(dbManager.getCategoryLastIndex()) + "; text: " + text + "; pc_id: " + PC_ID_R, Toast.LENGTH_LONG).show();
                Toast.makeText(context, "count category -" + String.valueOf(dbManager.getCategoryNamesList(PC_ID_R).size()), Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(context, "Такая категория уже есть", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context, "error add", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isExistText(String text){
        try {
            for(String name: dbManager.getCategoryNamesList(PC_ID_R)){
                if((name.toLowerCase().trim()).equals(text.toLowerCase().trim())) return true;
            }
        }catch (Exception e){Toast.makeText(context, "error isExistText", Toast.LENGTH_SHORT).show();}
        return false;
    }


    public static void updateAdapter(){
        try {
            adapter.setList(getCategoryFromDB());
            recycler.setAdapter(adapter);
        }catch (Exception e){Toast.makeText(context, "error update", Toast.LENGTH_SHORT).show();}
    }

}