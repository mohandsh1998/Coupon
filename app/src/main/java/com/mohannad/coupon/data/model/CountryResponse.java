package com.mohannad.coupon.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("countries")
    private List<Country> countries;

    public boolean isStatus() {
        return status;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public static class Country {
        @SerializedName("id")
        private int id;
        @SerializedName("iso_3166_1_alpha2")
        private String iso_3166_1_alpha2;
        @SerializedName("name")
        private String name;
        @SerializedName("flag")
        private String flag;

        public int getId() {
            return id;
        }

        public String getIso_3166_1_alpha2() {
            return iso_3166_1_alpha2;
        }

        public String getName() {
            return name;
        }

        public String getFlag() {
            return flag;
        }
    }
}
