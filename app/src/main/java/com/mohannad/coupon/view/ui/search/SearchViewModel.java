package com.mohannad.coupon.view.ui.search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.Coupon;
import com.mohannad.coupon.data.model.MessageResponse;
import com.mohannad.coupon.data.model.SearchResponse;
import com.mohannad.coupon.data.model.UsedCouponResponse;
import com.mohannad.coupon.repository.HomeRepository;
import com.mohannad.coupon.repository.SearchRepository;
import com.mohannad.coupon.repository.UsedCouponRepository;
import com.mohannad.coupon.utils.BaseViewModel;

import java.util.List;

public class SearchViewModel extends BaseViewModel {
    private HomeRepository homeRepository;
    private SearchRepository searchRepository;
    public MutableLiveData<Boolean> isLastPage = new MutableLiveData<>();
    private StorageSharedPreferences sharedPreferences;
    MutableLiveData<List<Coupon>> resultCoupons = new MutableLiveData<>();
    public int currentPageCoupons;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        homeRepository = HomeRepository.newInstance();
        searchRepository = SearchRepository.newInstance();
        sharedPreferences = new StorageSharedPreferences(application);
    }

    // this method will call searchCoupon from repository to get all coupons that contain the same word
    public void searchCoupons(String word, int page) {
        currentPageCoupons = page;
        dataLoading.setValue(true);
        searchRepository.searchCoupon(sharedPreferences.getLanguage(),
                sharedPreferences.getAuthToken(), sharedPreferences.getTokenFCM(), word, page,
                new ResponseServer<LiveData<SearchResponse>>() {
                    @Override
                    public void onSuccess(boolean status, int code, LiveData<SearchResponse> response) {
                        dataLoading.setValue(false);
                        if (response != null && response.getValue() != null) {
                            if (response.getValue().isStatus()) {
                                resultCoupons.setValue(response.getValue().getItems().getCoupons());
                                isLastPage.setValue(page == response.getValue().getItems().getLastPage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        dataLoading.setValue(false);
                        // show error msg
                        toastMessageFailed.setValue(getApplication().getString(R.string.problem_when_try_to_connect));
                    }
                });
    }


    // this method will call filterCoupons from repository to get all coupons that belong to filter
    public void filterCoupons(Integer idStore, Integer idCompany, String filterSpecific, int page) {
        currentPageCoupons = page;
        dataLoading.setValue(true);
        searchRepository.filterCoupon(sharedPreferences.getLanguage(),
                sharedPreferences.getAuthToken(), sharedPreferences.getTokenFCM(), idStore, idCompany, filterSpecific, page,
                new ResponseServer<LiveData<SearchResponse>>() {
                    @Override
                    public void onSuccess(boolean status, int code, LiveData<SearchResponse> response) {
                        dataLoading.setValue(false);
                        if (response != null && response.getValue() != null) {
                            if (response.getValue().isStatus()) {
                                resultCoupons.setValue(response.getValue().getItems().getCoupons());
                                isLastPage.setValue(page == response.getValue().getItems().getLastPage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        dataLoading.setValue(false);
                        // show error msg
                        toastMessageFailed.setValue(getApplication().getString(R.string.problem_when_try_to_connect));
                    }
                });
    }

    // this method will call addOrRemoveCouponFavorite from repository to add or remove the coupon to favorite on server
    public void addOrRemoveCouponFavorite(int idCoupon) {
        // call addOrRemoveCouponFavorite from repository
        homeRepository.addOrRemoveCouponFavorite(sharedPreferences.getLanguage(),
                sharedPreferences.getAuthToken(), sharedPreferences.getTokenFCM(), idCoupon, new ResponseServer<LiveData<MessageResponse>>() {
                    @Override
                    public void onSuccess(boolean status, int code, LiveData<MessageResponse> response) {
                        // check if status success
                        if (status) {
                            if (response != null && response.getValue() != null) {
                                if (response.getValue().isStatus()) {
                                    toastMessageSuccess.setValue(response.getValue().getMessage());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        // show error msg
                        toastMessageFailed.setValue(getApplication().getString(R.string.problem_when_try_to_connect));
                    }
                });
    }
}
