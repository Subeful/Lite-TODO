package com.example.mytodo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodo.Category;
import com.example.mytodo.Model.PackageModel;
import com.example.mytodo.R;
import com.example.mytodo.Record;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    List<PackageModel> list;

    public void setList(List<PackageModel> list) {
        this.list = list;
    }

    public CategoryAdapter(Context context, List<PackageModel> list) {
        this.context = context;
        this.list = list;
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

            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
        });
        holder.text.setOnClickListener(view -> {

            Intent intent = new Intent(context, Record.class);
//            intent.putExtra("Package", );
            intent.putExtra("Category", list.get(position).getPack_name());

            context.startActivity(intent);
        });

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
