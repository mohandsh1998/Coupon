package com.mohannad.coupon.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoriesResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("stores")
    private List<Category> categories;

    public boolean isStatus() {
        return status;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public static class Category {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
