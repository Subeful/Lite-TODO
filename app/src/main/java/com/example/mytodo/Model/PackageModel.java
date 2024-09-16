package com.example.mytodo.Model;

public class PackageModel {
    int pack_id;
    String pack_name;

    public PackageModel(int pack_id, String pack_name) {
        this.pack_id = pack_id;
        this.pack_name = pack_name;
    }

    public int getPack_id() {
        return pack_id;
    }

    public void setPack_id(int pack_id) {
        this.pack_id = pack_id;
    }

    public String getPack_name() {
        return pack_name;
    }

    public void setPack_name(String pack_name) {
        this.pack_name = pack_name;
    }
}
