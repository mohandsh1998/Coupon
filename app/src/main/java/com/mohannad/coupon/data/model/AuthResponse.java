package com.mohannad.coupon.data.model;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("user")
    private User user;
    @SerializedName("msg")
    private String msg;

    public boolean isStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public String getMsg() {
        return msg;
    }

   public static class User {
        @SerializedName("name")
        private String name;
        @SerializedName("email")
        private String email;
        @SerializedName("token")
        private String token;

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getToken() {
            return token;
        }
    }
}
