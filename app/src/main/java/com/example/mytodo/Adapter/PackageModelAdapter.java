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

import java.util.List;

public class PackageModelAdapter extends RecyclerView.Adapter<PackageModelAdapter.PackageViewHolder> {

    Context context;
    List<PackageModel> list;

    public void setList(List<PackageModel> list) {
        this.list = list;
    }

    public PackageModelAdapter(Context context, List<PackageModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.package_model, parent, false);
        return new PackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {

        holder.text.setText(list.get(position).getPack_name());

        holder.edit.setOnClickListener(view -> {

            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
        });
        holder.text.setOnClickListener(view -> {

            Intent intent = new Intent(context, Category.class);
            intent.putExtra("Package", list.get(position).getPack_name());
            context.startActivity(intent);
        });

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
