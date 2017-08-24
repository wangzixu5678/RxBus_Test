package com.example.ftkj.rxbus_test;

/**
 * Created by FTKJ on 2017/8/23.
 */

public class StudentEvent {
    private String id;
    private String name;

    public StudentEvent(String id, String name) {
        this.id = id;
        this.name = name;
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

}
