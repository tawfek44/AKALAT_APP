package com.example.sharedprefrence;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sharedprefrence.classes.categories;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class myViewModel extends ViewModel {

    private MutableLiveData<categories>mutableLiveData = new MutableLiveData<>();
    private MutableLiveData<mealDetail>mealLiveData = new MutableLiveData<>();
    private MutableLiveData<catogeryMeals> categoryLiveData=new MutableLiveData<>();
    private MutableLiveData<mealDetail> searchLiveData=new MutableLiveData<>();



    public MutableLiveData<categories> getMutableLiveData() {
        return mutableLiveData;
    }
    public void setMutableLiveData(MutableLiveData<categories> mutableLiveData) {
        this.mutableLiveData = mutableLiveData;
    }
    public MutableLiveData<mealDetail> getMealLiveData() {
        return mealLiveData;
    }
    public void setMealLiveData(MutableLiveData<mealDetail> mealLiveData) {
        this.mealLiveData = mealLiveData;
    }
    public MutableLiveData<catogeryMeals> getCategoryLiveData() {
        return categoryLiveData;
    }
    public void setCategoryLiveData(MutableLiveData<catogeryMeals> categoryLiveData) {
        this.categoryLiveData = categoryLiveData;
    }
    public MutableLiveData<mealDetail> getSearchLiveData() {
        return searchLiveData;
    }
    public void setSearchLiveData(MutableLiveData<mealDetail> searchLiveData) {
        this.searchLiveData = searchLiveData;
    }

    private void getCategoriesData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api service = retrofit.create(api.class);

        Call<categories> call = service.getAllCategories();
        call.enqueue(new Callback<categories>() {
            @Override
            public void onResponse(Call<categories> call, Response<categories> response) {
                mutableLiveData.setValue(response.body());
            }
            @Override
            public void onFailure(Call<categories> call, Throwable t) {
            }
        });
    }
    public void getCategories(){
        getCategoriesData();
    }


    private void getMealData(String id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api service = retrofit.create(api.class);
        Call<mealDetail> call = service.getSpecificMeal(id);
        call.enqueue(new Callback<mealDetail>() {
            @Override
            public void onResponse(Call<mealDetail> call, Response<mealDetail> response) {
                mealLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<mealDetail> call, Throwable t) {

            }
        });
    }
    public void getMeal(String id){
        getMealData(id);
    }


    private void getCategoryMealsData(String categoryName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api service=retrofit.create(api.class);
        Call<catogeryMeals> call = service.getCategoryMeals(categoryName);
        call.enqueue(new Callback<catogeryMeals>() {
            @Override
            public void onResponse(Call<catogeryMeals> call, Response<catogeryMeals> response) {
                categoryLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<catogeryMeals> call, Throwable t) {

            }
        });
    }
    public void getCategoryMeals(String categoryName)
    {
        getCategoryMealsData(categoryName);
    }


    private void getMealsFromSearchData(String mealName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api service=retrofit.create(api.class);
        Call<mealDetail> call = service.SearchForMeals(mealName);
        call.enqueue(new Callback<mealDetail>() {
            @Override
            public void onResponse(Call<mealDetail> call, Response<mealDetail> response) {
                searchLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<mealDetail> call, Throwable t) {

            }
        });
       }
    public void getSearchResult(String mealName)
    {
        getMealsFromSearchData(mealName);
    }
}
