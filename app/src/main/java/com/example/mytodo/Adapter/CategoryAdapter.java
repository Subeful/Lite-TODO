package com.example.mytodo.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodo.Category;
import com.example.mytodo.DB.DbManager;
import com.example.mytodo.MainActivity;
import com.example.mytodo.Model.PackageModel;
import com.example.mytodo.R;
import com.example.mytodo.Record;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    List<PackageModel> list;
    DbManager dbManager;

    public void setList(List<PackageModel> list) {
        this.list = list;
    }

    public CategoryAdapter(Context context, List<PackageModel> list, DbManager dbManager) {
        this.context = context;
        this.list = list;
        this.dbManager = dbManager;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.package_model, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        holder.text.setText(list.get(position).getPack_name());

        holder.edit.setOnClickListener(view -> {
            setAlertForDeleteCategory(position);
        });

        holder.text.setOnClickListener(view -> {

            Intent intent = new Intent(context, Record.class);

            intent.putExtra("Package", dbManager.getPackageName(Category.PC_ID_R));
            intent.putExtra("Category", list.get(position).getPack_name());

            context.startActivity(intent);
        });



    }

    private void deleteCategoryFromDB(int position){
        try {
            dbManager.deleteCategory(list.get(position).getPack_name());

            list = getCategoryFromDB();
            Category.updateAdapter();

            if(dbManager.getPackageNamesList().isEmpty()) Category.categoryId = 0;

        }catch (Exception e){Toast.makeText(context, String.valueOf(e.getMessage()), Toast.LENGTH_LONG).show();}
    }

    @SuppressLint({"NewApi", "ResourceAsColor"})
    public void setAlertForDeleteCategory(int position){
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);

        mDialogBuilder.setTitle("Удаление категории")
                .setMessage("Вы хотите удалить категорию: \"" + list.get(position).getPack_name() + "\" и все записи в ней?").setCancelable(false)
                .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        deleteCategoryFromDB(position);
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {dialog.cancel();}});

        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.back_2);
        alertDialog.show();
    }

    private  List<PackageModel> getCategoryFromDB() {

        List<PackageModel> dbList = new ArrayList<>();
        try {
            List<String> dbBeforeList = dbManager.getCategoryNamesList(Category.PC_ID_R);
            for (int i = 0; i < dbBeforeList.size(); i++) {
                dbList.add(new PackageModel(i, dbBeforeList.get(i)));
            }
        } catch (Exception e) {
            Toast.makeText(context, "error getCategoryFromDB", Toast.LENGTH_SHORT).show();
        }

        return dbList;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static final class CategoryViewHolder extends RecyclerView.ViewHolder{

        TextView text;
        ImageButton edit;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.PackageModelText);
            edit = itemView.findViewById(R.id.PackageModelEdit);
        }
    }
}
