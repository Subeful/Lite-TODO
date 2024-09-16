package com.example.mytodo.Helper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytodo.Category;
import com.example.mytodo.MainActivity;
import com.example.mytodo.Model.PackageModel;
import com.example.mytodo.R;
import com.example.mytodo.Record;

import java.util.Calendar;
import java.util.List;

public class AlertHelper {

    public static void createAlert(Context context, List<PackageModel> list, String description){
        View promptsView = LayoutInflater.from(context).inflate(R.layout.add_package, null);

        TextView textView = promptsView.findViewById(R.id.alert_tital);
        textView.setText(description);

        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
        mDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText)
                promptsView.findViewById(R.id.alert_text);

        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                if(context.equals(MainActivity.context))
                                    MainActivity.createNewPackage(userInput);
                                else if(context.equals(Category.context))
                                    Category.createNewPackage(userInput);
                                else if(context.equals(Record.context))
                                    Record.createNewPackage(userInput);
                            }
                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.back_2);
        alertDialog.show();
    }


}
