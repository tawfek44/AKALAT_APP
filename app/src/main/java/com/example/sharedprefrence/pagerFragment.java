package com.example.sharedprefrence;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.sharedprefrence.classes.categories;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;


public class pagerFragment extends Fragment implements mealAdapter.onMealClickListener{

    RecyclerView mealsRec;
    mealAdapter mealAdapter;
    categories.CategoriesBean category;
    myViewModel vm;
    ProgressBar progressBar;
    List<categories.CategoriesBean> categs;
    public pagerFragment() {

    }

    public pagerFragment(categories.CategoriesBean category) {
        this.category = category;
    }

    public pagerFragment(categories.CategoriesBean category, List<categories.CategoriesBean> categs) {
        this.category = category;
        this.categs = categs;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager, container, false);
        mealsRec = view.findViewById(R.id.rec_meals);
        progressBar=(ProgressBar) view.findViewById(R.id.pb);
        progressBar.setVisibility(View.VISIBLE);
        final HashMap<String,Integer>map = getFav();
        vm = ViewModelProviders.of(this).get(myViewModel.class);
        vm.getCategoryMeals(category.getStrCategory());
        mealAdapter=new mealAdapter();
        mealAdapter.setListener(this);
        vm.getCategoryLiveData().observe(getViewLifecycleOwner(), new Observer<catogeryMeals>() {
            @Override
            public void onChanged(catogeryMeals catogeryMeals) {
                mealAdapter.setMeals(catogeryMeals.getMeals());
                mealAdapter.setFav(map);
                mealsRec.setAdapter(mealAdapter);
                mealsRec.setLayoutManager(new GridLayoutManager(getContext(),2));
                mealsRec.setHasFixedSize(true);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        return view;
    }

    @Override
    public void onMealClick(String id) {
        Intent intent = new Intent(getContext(), MealDetails.class);
        intent.putExtra("mealId",id);
        Bundle bundle = new Bundle();
        bundle.putSerializable("category",(Serializable)categs);
        intent.putExtra("category",bundle);
        intent.putExtra("index",categs.indexOf(category));
        startActivity(intent);
        getActivity().finish();
    }

    public HashMap<String,Integer> getFav(){
        final HashMap<String,Integer> map = new HashMap<>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String user = preferences.getString("user","tofe");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Favorite").child(user);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    mealDetail.MealsBean meal = snapshot.getValue(mealDetail.MealsBean.class);
                    map.put(meal.getIdMeal(),1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return map;
    }
}