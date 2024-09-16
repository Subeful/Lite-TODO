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

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    Context context;
    List<PackageModel> list;

    public void setList(List<PackageModel> list) {
        this.list = list;
    }

    public RecordAdapter(Context context, List<PackageModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.record_model, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {

        holder.text.setText(list.get(position).getPack_name());

        holder.edit.setOnClickListener(view -> {

            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
        });
        

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static final class RecordViewHolder extends RecyclerView.ViewHolder{

        TextView text;
        ImageButton edit;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.PackageModelText);
            edit = itemView.findViewById(R.id.PackageModelEdit);
        }
    }
}
