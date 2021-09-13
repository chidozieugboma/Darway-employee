package com.example.emp.model;

public class departmentModel {
    String key,id,name,createDt;

    public departmentModel() {
    }

    public departmentModel(String key, String id, String name, String createDt) {
        this.key = key;
        this.id = id;
        this.name = name;
        this.createDt = createDt;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }
}
