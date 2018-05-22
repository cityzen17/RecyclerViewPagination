package com.example.cityzen10.recyclerviewpagination.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PersonResponse implements Parcelable {
    @SerializedName("page")
    private int page;
    @SerializedName("data")
    private List<Person> data;
    @SerializedName("per_page")
    private int perPage;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("total")
    private int total;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Person> getData() {

        return data;
    }

    public void setData(List<Person> data) {
        this.data = data;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.page);
        dest.writeTypedList(this.data);
        dest.writeInt(this.perPage);
        dest.writeInt(this.total);
        dest.writeInt(this.totalPages);

    }

    private PersonResponse(Parcel in) {
        this.page = in.readInt();
        this.data = in.createTypedArrayList(Person.CREATOR);
        this.total = in.readInt();
        this.totalPages = in.readInt();
        this.perPage = in.readInt();

    }

    public static final Creator<PersonResponse> CREATOR = new Creator<PersonResponse>() {
        @Override
        public PersonResponse createFromParcel(Parcel source) {
            return new PersonResponse(source);
        }

        @Override
        public PersonResponse[] newArray(int size) {
            return new PersonResponse[size];
        }
    };
}
