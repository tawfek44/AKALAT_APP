package com.example.sharedprefrence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity implements mealAdapter.onMealClickListener {

    RecyclerView favRec;
    mealAdapter adapter;
    List<catogeryMeals.MealsBean> meals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        favRec = (RecyclerView)findViewById(R.id.fav_rec);
        adapter = new mealAdapter();
        meals = new ArrayList<>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String user = preferences.getString("user","tofe");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Favorite").child(user);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    catogeryMeals.MealsBean mealsBean = snapshot.getValue(catogeryMeals.MealsBean.class);
                    meals.add(mealsBean);
                }
                adapter = new mealAdapter();
                adapter.setMeals(meals);
                adapter.setListener(FavoriteActivity.this);
                adapter.setType("fav");
                favRec.setAdapter(adapter);
                favRec.setLayoutManager(new GridLayoutManager(FavoriteActivity.this,2));
                favRec.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void onComponentsClick(View view){
        int id = view.getId();
        if(id == R.id.back_btn){
            Intent intent = new Intent(FavoriteActivity.this,CategoriesActivity.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    public void onMealClick(String id) {
        Intent intent = new Intent(FavoriteActivity.this,MealDetails.class);
        intent.putExtra("mealId",id);
        intent.putExtra("fav",true);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FavoriteActivity.this,CategoriesActivity.class);
        startActivity(intent);
        finish();
    }
}