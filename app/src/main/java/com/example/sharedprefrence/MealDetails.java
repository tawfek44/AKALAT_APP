package com.example.sharedprefrence;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.sharedprefrence.classes.categories;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.List;

public class MealDetails extends AppCompatActivity {

    CollapsingToolbarLayout collapse;
    ImageView mealImg,fav;
    myViewModel vm;
    Toolbar tb;
    TextView categ,count,inst,ing;
    String meal_ing = "";
    LinearLayout yt,src;
    String mealId;
    String user;
    List<categories.CategoriesBean> categs;
    int index;
    boolean search,favv;
    catogeryMeals.MealsBean myMeal;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);
        collapse = (CollapsingToolbarLayout)findViewById(R.id.collapse);
        mealImg = (ImageView)findViewById(R.id.meal_image);
        fav = (ImageView)findViewById(R.id.fav);
        categ = (TextView)findViewById(R.id.meal_category);
        count = (TextView)findViewById(R.id.meal_country);
        inst = (TextView)findViewById(R.id.meal_inst);
        ing = (TextView)findViewById(R.id.meal_ing);
        yt=(LinearLayout)findViewById(R.id.youtube_layout);
        src=(LinearLayout)findViewById(R.id.source_layout);

        mealId = getIntent().getStringExtra("mealId");
        if(getIntent().hasExtra("category"))
            categs = (List<categories.CategoriesBean>) getIntent().getBundleExtra("category").getSerializable("category");

        search = getIntent().getBooleanExtra("search",false);
        favv = getIntent().getBooleanExtra("fav",false);
        index = getIntent().getIntExtra("index",0);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user = preferences.getString("user","tofe");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Favorite").child(user);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    catogeryMeals.MealsBean categ = snapshot.getValue(catogeryMeals.MealsBean.class);
                    if(categ.getIdMeal().equals(mealId))
                    {
                        fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart));
                        fav.setContentDescription("fav");
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myMeal = new catogeryMeals.MealsBean();
        vm = ViewModelProviders.of(this).get(myViewModel.class);
        vm.getMeal(mealId);
        vm.getMealLiveData().observe(this, new Observer<mealDetail>() {
            @Override
            public void onChanged(final mealDetail mealDetail) {
                myMeal.setIdMeal(mealDetail.getMeals().get(0).getIdMeal());
                myMeal.setStrMeal(mealDetail.getMeals().get(0).getStrMeal());
                myMeal.setStrMealThumb(mealDetail.getMeals().get(0).getStrMealThumb());
                Glide.with(MealDetails.this).load(mealDetail.getMeals().get(0).getStrMealThumb()).into(mealImg);
                collapse.setTitle(mealDetail.getMeals().get(0).getStrMeal());
                categ.setText(mealDetail.getMeals().get(0).getStrCategory());
                count.setText(mealDetail.getMeals().get(0).getStrArea());
                inst.setText(mealDetail.getMeals().get(0).getStrInstructions());
                getIngredients(mealDetail);
                ing.setText(meal_ing);
                yt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mealDetail.getMeals().get(0).getStrYoutube()));
                        if(getPackageManager().queryIntentActivities(intent,0).size() > 0){
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(MealDetails.this, "Error!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                src.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mealDetail.getMeals().get(0).getStrSource().toString()));
                        if(getPackageManager().queryIntentActivities(intent,0).size() > 0){
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(MealDetails.this, "Error!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
    }

    public void getIngredients(mealDetail mealDetail){
        String space = "  :   ";
        if(mealDetail.getMeals().get(0).getStrIngredient1() != null && !mealDetail.getMeals().get(0).getStrIngredient1().equals("")){
            meal_ing += mealDetail.getMeals().get(0).getStrIngredient1();
            meal_ing += space;
            meal_ing += mealDetail.getMeals().get(0).getStrMeasure1();
            meal_ing += "\n";
        }
        if(mealDetail.getMeals().get(0).getStrIngredient2() != null&& !mealDetail.getMeals().get(0).getStrIngredient2().equals("")){
            meal_ing += mealDetail.getMeals().get(0).getStrIngredient2();
            meal_ing += space;
            meal_ing += mealDetail.getMeals().get(0).getStrMeasure2();
            meal_ing += "\n";
        }
        if(mealDetail.getMeals().get(0).getStrIngredient3() != null&& !mealDetail.getMeals().get(0).getStrIngredient3().equals("")){
            meal_ing += mealDetail.getMeals().get(0).getStrIngredient3();
            meal_ing += space;
            meal_ing += mealDetail.getMeals().get(0).getStrMeasure3();
            meal_ing += "\n";
        }
        if(mealDetail.getMeals().get(0).getStrIngredient4() != null&& !mealDetail.getMeals().get(0).getStrIngredient4().equals("")){
            meal_ing += mealDetail.getMeals().get(0).getStrIngredient4();
            meal_ing += space;
            meal_ing += mealDetail.getMeals().get(0).getStrMeasure4();
            meal_ing += "\n";
        }
        if(mealDetail.getMeals().get(0).getStrIngredient5() != null&& !mealDetail.getMeals().get(0).getStrIngredient5().equals("")){
            meal_ing += mealDetail.getMeals().get(0).getStrIngredient5();
            meal_ing += space;
            meal_ing += mealDetail.getMeals().get(0).getStrMeasure5();
            meal_ing += "\n";
        }
        if(mealDetail.getMeals().get(0).getStrIngredient6() != null&& !mealDetail.getMeals().get(0).getStrIngredient6().equals("")){
            meal_ing += mealDetail.getMeals().get(0).getStrIngredient6();
            meal_ing += space;
            meal_ing += mealDetail.getMeals().get(0).getStrMeasure6();
            meal_ing += "\n";
        }
        if(mealDetail.getMeals().get(0).getStrIngredient7() != null&& !mealDetail.getMeals().get(0).getStrIngredient7().equals("")){
            meal_ing += mealDetail.getMeals().get(0).getStrIngredient7();
            meal_ing += space;
            meal_ing += mealDetail.getMeals().get(0).getStrMeasure7();
            meal_ing += "\n";
        }
        if(mealDetail.getMeals().get(0).getStrIngredient8() != null&& !mealDetail.getMeals().get(0).getStrIngredient8().equals("")){
            meal_ing += mealDetail.getMeals().get(0).getStrIngredient8();
            meal_ing += space;
            meal_ing += mealDetail.getMeals().get(0).getStrMeasure8();
            meal_ing += "\n";
        }
        if(mealDetail.getMeals().get(0).getStrIngredient9() != null&& !mealDetail.getMeals().get(0).getStrIngredient9().equals("")){
            meal_ing += mealDetail.getMeals().get(0).getStrIngredient9();
            meal_ing += space;
            meal_ing += mealDetail.getMeals().get(0).getStrMeasure9();
            meal_ing += "\n";
        }
        if(mealDetail.getMeals().get(0).getStrIngredient10() != null&& !mealDetail.getMeals().get(0).getStrIngredient10().equals("")){
            meal_ing += mealDetail.getMeals().get(0).getStrIngredient10();
            meal_ing += space;
            meal_ing += mealDetail.getMeals().get(0).getStrMeasure10();
            meal_ing += "\n";
        }
        if(mealDetail.getMeals().get(0).getStrIngredient11() != null&& !mealDetail.getMeals().get(0).getStrIngredient11().equals("")){
            meal_ing += mealDetail.getMeals().get(0).getStrIngredient11();
            meal_ing += space;
            meal_ing += mealDetail.getMeals().get(0).getStrMeasure11();
            meal_ing += "\n";
        }
        if(mealDetail.getMeals().get(0).getStrIngredient12() != null&& !mealDetail.getMeals().get(0).getStrIngredient12().equals("")){
            meal_ing += mealDetail.getMeals().get(0).getStrIngredient12();
            meal_ing += space;
            meal_ing += mealDetail.getMeals().get(0).getStrMeasure12();
            meal_ing += "\n";
        }
        if(mealDetail.getMeals().get(0).getStrIngredient13() != null&& !mealDetail.getMeals().get(0).getStrIngredient13().equals("")){
            meal_ing += mealDetail.getMeals().get(0).getStrIngredient13();
            meal_ing += space;
            meal_ing += mealDetail.getMeals().get(0).getStrMeasure13();
            meal_ing += "\n";
        }
        if(mealDetail.getMeals().get(0).getStrIngredient14() != null&& !mealDetail.getMeals().get(0).getStrIngredient14().equals("")){
            meal_ing += mealDetail.getMeals().get(0).getStrIngredient14();
            meal_ing += space;
            meal_ing += mealDetail.getMeals().get(0).getStrMeasure14();
            meal_ing += "\n";
        }
        if(mealDetail.getMeals().get(0).getStrIngredient15() != null&& !mealDetail.getMeals().get(0).getStrIngredient15().equals("")){
            meal_ing += mealDetail.getMeals().get(0).getStrIngredient15();
            meal_ing += space;
            meal_ing += mealDetail.getMeals().get(0).getStrMeasure15();
            meal_ing += "\n";
        }
        if(mealDetail.getMeals().get(0).getStrIngredient16() != null&& !mealDetail.getMeals().get(0).getStrIngredient16().equals("")){
            meal_ing += mealDetail.getMeals().get(0).getStrIngredient16();
            meal_ing += space;
            meal_ing += mealDetail.getMeals().get(0).getStrMeasure16();
            meal_ing += "\n";
        }
        if(mealDetail.getMeals().get(0).getStrIngredient17() != null&& !mealDetail.getMeals().get(0).getStrIngredient17().equals("")){
            meal_ing += mealDetail.getMeals().get(0).getStrIngredient17();
            meal_ing += space;
            meal_ing += mealDetail.getMeals().get(0).getStrMeasure17();
            meal_ing += "\n";
        }
        if(mealDetail.getMeals().get(0).getStrIngredient18() != null&& !mealDetail.getMeals().get(0).getStrIngredient18().equals("")){
            meal_ing += mealDetail.getMeals().get(0).getStrIngredient18();
            meal_ing += space;
            meal_ing += mealDetail.getMeals().get(0).getStrMeasure18();
            meal_ing += "\n";
        }
        if(mealDetail.getMeals().get(0).getStrIngredient19() != null&& !mealDetail.getMeals().get(0).getStrIngredient19().equals("")){
            meal_ing += mealDetail.getMeals().get(0).getStrIngredient19();
            meal_ing += space;
            meal_ing += mealDetail.getMeals().get(0).getStrMeasure19();
            meal_ing += "\n";
        }
        if(mealDetail.getMeals().get(0).getStrIngredient20() != null&& !mealDetail.getMeals().get(0).getStrIngredient20().equals("")){
            meal_ing += mealDetail.getMeals().get(0).getStrIngredient20();
            meal_ing += space;
            meal_ing += mealDetail.getMeals().get(0).getStrMeasure20();
            meal_ing += "\n";
        }
    }

    public void onComponentsClick(View view){
        int id = view.getId();
        if(id == R.id.back_btn){
            if(search){
                Intent intent = new Intent(MealDetails.this, CategoriesActivity.class);
                startActivity(intent);
                finish();
            }
            else if(favv){
                Intent intent = new Intent(MealDetails.this, FavoriteActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                Intent intent = new Intent(MealDetails.this, CategoryMeals.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("category",(Serializable)categs);
                intent.putExtra("category",bundle);
                intent.putExtra("index",index);
                startActivity(intent);
                finish();
            }
        }
        else if(id == R.id.fav){

            int i=0;
            if(fav.getContentDescription().equals("fav")){
                fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_outline));
                fav.setContentDescription("nofav");
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Favorite");
                reference.child(user).child(mealId).removeValue();
            }
            else if(fav.getContentDescription().equals("nofav")){
                fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart));
                fav.setContentDescription("fav");
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Favorite");
                reference.child(user).child(mealId).setValue(myMeal);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(search){
            Intent intent = new Intent(MealDetails.this, CategoriesActivity.class);
            startActivity(intent);
            finish();
        }
        else if(favv){
            Intent intent = new Intent(MealDetails.this, FavoriteActivity.class);
            startActivity(intent);
            finish();
        }
       else{
            Intent intent = new Intent(MealDetails.this, CategoryMeals.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("category",(Serializable)categs);
            intent.putExtra("category",bundle);
            intent.putExtra("index",index);
            startActivity(intent);
            finish();
        }
    }
}