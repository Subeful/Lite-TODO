package com.example.mytodo.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodo.DB.DbManager;
import com.example.mytodo.MainActivity;
import com.example.mytodo.Model.PackageModel;
import com.example.mytodo.R;
import com.example.mytodo.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    Context context;
    List<PackageModel> list;
    DbManager dbManager;

    public void setList(List<PackageModel> list) {
        this.list = list;
    }

    public RecordAdapter(Context context, List<PackageModel> list, DbManager dbManager) {
        this.context = context;
        this.list = list;
        this.dbManager = dbManager;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.record_model, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.text.setText(dbManager.getRecordText(list.get(position).getPack_id()));

        holder.edit.setOnClickListener(view -> {
            setAlertForDeletePackage(position);
        });

        holder.text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                dbManager.updateRecordText(list.get(position).getPack_id(), editable.toString());
            }
        });

        

    }

    private void deleteRecordFromDB(int position){
        try {
            dbManager.deleteRecord(list.get(position).getPack_name());
            list = getRecordFromDB();

            Record.updateAdapter();

            if(dbManager.getPackageNamesList().isEmpty()) Record.recordId = 0;

        }catch (Exception e){
            Toast.makeText(context, String.valueOf(e.getMessage()), Toast.LENGTH_LONG).show();
        }
    }

    public void setAlertForDeletePackage(int position){
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        mDialogBuilder.setTitle("Удаление записи")
                .setMessage("Вы хотите удалить запись: \"" + list.get(position).getPack_name() + "\"").setCancelable(false)
                .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        deleteRecordFromDB(position);
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {dialog.cancel();}});

        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.back_2);
        alertDialog.show();
    }
    private List<PackageModel> getRecordFromDB(){

        List<PackageModel> dbList = new ArrayList<>();
        try {
            Map<Integer, String> dbBeforeList = dbManager.getRecordNamesAndIdList(Record.CG_ID_R);
            for (Map.Entry<Integer, String> map: dbBeforeList.entrySet()) {
                dbList.add(new PackageModel(map.getKey(), map.getValue()));
            }

        }catch (Exception e){Toast.makeText(context, "error getCategoryFromDB", Toast.LENGTH_SHORT).show();}

        return dbList;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static final class RecordViewHolder extends RecyclerView.ViewHolder{

        EditText text;
        ImageButton edit;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.PackageModelText);
            edit = itemView.findViewById(R.id.PackageModelEdit);
        }
    }
}
