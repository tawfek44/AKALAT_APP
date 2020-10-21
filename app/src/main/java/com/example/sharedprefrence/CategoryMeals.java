package com.example.sharedprefrence;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.sharedprefrence.classes.categories;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class CategoryMeals extends AppCompatActivity {

    ViewPager vp;
    TabLayout tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_meals);
        tab = (TabLayout)findViewById(R.id.tab);
        vp = (ViewPager)findViewById(R.id.vp);

        Bundle bundle = getIntent().getBundleExtra("category");
        List<categories.CategoriesBean> categs = (List<categories.CategoriesBean>)bundle.getSerializable("category");
        int index = getIntent().getIntExtra("index",0);

        pagerAdapter adapter = new pagerAdapter(getSupportFragmentManager());
        for(int i=0;i<categs.size();i++){
            adapter.addFragment(new pagerFragment(categs.get(i),categs),categs.get(i).getStrCategory());
        }

        vp.setAdapter(adapter);
        tab.setupWithViewPager(vp);
        vp.setCurrentItem(index);
    }

    public void onComponentsClick(View view){
        int id = view.getId();
        if(id == R.id.back_btn){
            Intent intent = new Intent(CategoryMeals.this, CategoriesActivity.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CategoryMeals.this, CategoriesActivity.class);
        startActivity(intent);
        finish();
    }
}


