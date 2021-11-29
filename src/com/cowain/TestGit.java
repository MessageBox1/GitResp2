package com.cowain;

import java.io.Serializable;

/**
 * @author: fxw
 */
public class TestGit implements Serializable {
    private String name;
    private int age;
    private  String gender;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public TestGit() {
    }

    public TestGit(String name, int age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }
}
