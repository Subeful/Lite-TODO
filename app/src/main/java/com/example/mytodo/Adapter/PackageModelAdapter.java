package com.example.mytodo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodo.Category;
import com.example.mytodo.DB.DbHelper;
import com.example.mytodo.DB.DbManager;
import com.example.mytodo.MainActivity;
import com.example.mytodo.Model.PackageModel;
import com.example.mytodo.R;

import java.util.ArrayList;
import java.util.List;

public class PackageModelAdapter extends RecyclerView.Adapter<PackageModelAdapter.PackageViewHolder> {

    Context context;
    List<PackageModel> list;
    DbManager dbManager;
    public void setList(List<PackageModel> list) {
        this.list = list;
    }

    public List<PackageModel> getList() {
        return list;
    }

    public PackageModelAdapter(Context context, List<PackageModel> list, DbManager dbManager) {
        this.context = context;
        this.list = list;
        this.dbManager = dbManager;
    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.package_model, parent, false);
        return new PackageViewHolder(view);
    }

    @SuppressLint({"WrongConstant", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {

        holder.text.setText(list.get(position).getPack_name());

        holder.edit.setOnClickListener(view -> {
            try {
                dbManager.deleteItem(list.get(position).getPack_name());
                list = getPackageFromDB();

                MainActivity.updateAdapter();

                if(dbManager.getPackageNamesList().isEmpty()) MainActivity.packageId = 0;

            }catch (Exception e){
                Toast.makeText(context, String.valueOf(e.getMessage()), Toast.LENGTH_LONG).show();
            }
        });
        holder.text.setOnClickListener(view -> {

            Intent intent = new Intent(context, Category.class);
            intent.putExtra("Package", list.get(position).getPack_name());
            context.startActivity(intent);
        });



    }
    List<PackageModel> getPackageFromDB(){
        List<PackageModel> dbNewList = new ArrayList<>();

        List<String> dbBeforeList = dbManager.getPackageNamesList();


        for(int i = 0; i < dbBeforeList.size(); i++){
            dbNewList.add(new PackageModel(i, dbBeforeList.get(i)));
        }

        return dbNewList;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static final class PackageViewHolder extends RecyclerView.ViewHolder{

        TextView text;
        ImageButton edit;

        public PackageViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.PackageModelText);
            edit = itemView.findViewById(R.id.PackageModelEdit);
        }
    }
}
