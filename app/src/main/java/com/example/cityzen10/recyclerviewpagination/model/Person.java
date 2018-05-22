package com.example.cityzen10.recyclerviewpagination.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Person implements Parcelable {
    @SerializedName("id")
    private Integer id;
    @SerializedName("first_name")
    private String firstname;
    @SerializedName("last_name")
    private String lastname;
    @SerializedName("avatar")
    private String avatarpics;

    public Person() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAvatarpics() {
        return avatarpics;
    }

    public void setAvatarpics(String avatarpics) {
        this.avatarpics = avatarpics;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.firstname);
        dest.writeString(this.lastname);
        dest.writeValue(this.avatarpics);
    }

    private Person(Parcel in) {
        this.avatarpics = in.readString();
        this.firstname = in.readString();
        this.lastname = in.readString();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());

    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
