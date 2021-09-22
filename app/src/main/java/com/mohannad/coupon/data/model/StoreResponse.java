package com.mohannad.coupon.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class StoreResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("stores")
    private ArrayList<Store> stores;

    public boolean isStatus() {
        return status;
    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public static class Store implements Parcelable {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("themes")
        private List<Theme> themes;

        protected Store(Parcel in) {
            id = in.readInt();
            name = in.readString();
            themes = in.createTypedArrayList(Theme.CREATOR);
        }

        public static final Creator<Store> CREATOR = new Creator<Store>() {
            @Override
            public Store createFromParcel(Parcel in) {
                return new Store(in);
            }

            @Override
            public Store[] newArray(int size) {
                return new Store[size];
            }
        };

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<Theme> getThemes() {
            return themes;
        }

        @NonNull
        @Override
        public String toString() {
            return name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeTypedList(themes);
        }
    }

    public static class Theme implements Parcelable {
        @SerializedName("id")
        private int id;
        @SerializedName("theme_id")
        private int themeId;
        @SerializedName("image")
        private String image;
        @SerializedName("logo")
        private String logo;

        protected Theme(Parcel in) {
            id = in.readInt();
            themeId = in.readInt();
            image = in.readString();
            logo = in.readString();
        }

        public static final Creator<Theme> CREATOR = new Creator<Theme>() {
            @Override
            public Theme createFromParcel(Parcel in) {
                return new Theme(in);
            }

            @Override
            public Theme[] newArray(int size) {
                return new Theme[size];
            }
        };

        public int getId() {
            return id;
        }

        public int getThemeId() {
            return themeId;
        }

        public String getImage() {
            return image;
        }

        public String getLogo() {
            return logo;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeInt(themeId);
            dest.writeString(image);
            dest.writeString(logo);
        }
    }
}
