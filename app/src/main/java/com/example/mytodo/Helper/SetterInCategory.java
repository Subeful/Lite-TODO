package com.example.mytodo.Helper;

import com.example.mytodo.Model.PackageModel;

public class SetterInCategory {
    public static void setList(){
        Cloud.list.add(new PackageModel(1, "Планы"));
        Cloud.list.add(new PackageModel(2, "Просьбы"));
        Cloud.list.add(new PackageModel(3, "Купить"));
        Cloud.list.add(new PackageModel(4, "Учеба"));
    }
}
