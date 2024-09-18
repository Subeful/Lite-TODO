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

import com.example.mytodo.Adapter.RecordAdapter;
import com.example.mytodo.DB.DbManager;
import com.example.mytodo.Model.PackageModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Record extends AppCompatActivity {

    static Context context;

    static RecyclerView recycler;
    static RecordAdapter adapter;

    static DbManager dbManager;

    public TextView name;
    public static int recordId = 0;

    public static int CG_ID_R, PC_ID_R;

    String packageName, categoryName;

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

        setUI();

    }

    private void setUI(){
        try {
            context = Record.this;
            recycler = findViewById(R.id.RecordRecycler);
            setName();
            dbManager = new DbManager(context);
            setApplicationRecycle(new ArrayList<>());

        }catch (Exception e){Toast.makeText(context, "set UI", Toast.LENGTH_SHORT).show();}
    }

    @SuppressLint("SetTextI18n")
    private void setName(){
        name = findViewById(R.id.recordNames);
        packageName = getIntent().getStringExtra("Package");
        categoryName = getIntent().getStringExtra("Category");
        name.setText("");
        name.append(packageName);name.append("/");name.append(categoryName);
    }


    @Override
    protected void onResume() {
        super.onResume();
        dbManager.openDb();

        PC_ID_R = dbManager.getPackageId(packageName);
        CG_ID_R = dbManager.getCategoryId(categoryName);

        updateAdapter();
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
            adapter = new RecordAdapter(this, listApp, dbManager);
            recycler.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(this, "error in recycler", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("Range")
    private static List<PackageModel> getRecordFromDB(){

        List<PackageModel> dbList = new ArrayList<>();
        try {
            Map<Integer, String> dbBeforeList = dbManager.getRecordNamesAndIdList(Record.CG_ID_R);
            for (Map.Entry<Integer, String> map: dbBeforeList.entrySet()) {
                dbList.add(new PackageModel(map.getKey(), map.getValue()));
            }

        }catch (Exception e){Toast.makeText(context, "error getCategoryFromDB", Toast.LENGTH_SHORT).show();}

        return dbList;
    }

    public void addRecord(View v){
        View promptsView = LayoutInflater.from(context).inflate(R.layout.add_package, null);

        TextView textView = promptsView.findViewById(R.id.alert_tital);
        textView.setText("Добавить запись");

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
                int index = dbManager.getRecordLastIndex();

                dbManager.insertToRecord(index, text, CG_ID_R);
                dbManager.updateRecordText(index, text);
                updateAdapter();
            }
            else Toast.makeText(context, "Такая категория уже есть", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context, "error add", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isExistText(String text){
        try {

            for(String name: dbManager.getRecordNamesList(CG_ID_R)){
                if((name.toLowerCase().trim()).equals(text.toLowerCase().trim())) return true;
            }
        }catch (Exception e){Toast.makeText(context, "error isExistText", Toast.LENGTH_SHORT).show();}
        return false;
    }


    public static void updateAdapter(){
        try {
            adapter.setList(getRecordFromDB());
            recycler.setAdapter(adapter);
        }catch (Exception e){Toast.makeText(context, "error update", Toast.LENGTH_SHORT).show();}
    }
}