package com.mohannad.coupon.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompaniesResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("companies")
    private List<Company> companies;

    public boolean isStatus() {
        return status;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public static class Company {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("image")
        private String image;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getImage() {
            return image;
        }
    }
}
