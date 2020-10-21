package com.example.sharedprefrence;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharedprefrence.classes.categories;

import java.io.Serializable;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity implements recAdapter.onItemClickListener,mealAdapter.onMealClickListener{

    myViewModel vm,svm;
    RecyclerView rec,recSearch;
    ProgressBar pb;
    EditText searchEt;
    TextView exitSearch,categLabel;
    mealAdapter searchAdapter;
    ImageView favIcon;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        prepareComponents();

        getSearchResult();

        getCategories();
    }

    private void getSearchResult() {
        svm = ViewModelProviders.of(this).get(myViewModel.class);
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pb.setVisibility(View.VISIBLE);
                recSearch.setVisibility(View.VISIBLE);
                rec.setVisibility(View.GONE);
                categLabel.setVisibility(View.GONE);
                exitSearch.setVisibility(View.VISIBLE);
                searchAdapter = new mealAdapter("search");
                svm.getSearchResult(s.toString());
                svm.getSearchLiveData().observe(CategoriesActivity.this, new Observer<mealDetail>() {
                    @Override
                    public void onChanged(mealDetail mealDetail) {
                        searchAdapter.setSearchMeals(mealDetail.getMeals());
                        searchAdapter.setListener(CategoriesActivity.this);
                        recSearch.setAdapter(searchAdapter);
                        recSearch.setLayoutManager(new GridLayoutManager(CategoriesActivity.this,2));
                        recSearch.setHasFixedSize(true);
                        pb.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        exitSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEt.getText().clear();
                recSearch.setVisibility(View.GONE);
                rec.setVisibility(View.VISIBLE);
                exitSearch.setVisibility(View.GONE);
            }
        });
    }

    public void prepareComponents(){
        searchEt = (EditText)findViewById(R.id.search_et);
        rec = (RecyclerView)findViewById(R.id.rec_categories);
        recSearch = (RecyclerView)findViewById(R.id.rec_search);
        exitSearch = (TextView)findViewById(R.id.exit_search);
        categLabel = (TextView)findViewById(R.id.textView2);
        pb = (ProgressBar)findViewById(R.id.pb);
        favIcon = (ImageView)findViewById(R.id.fav_icon);
        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriesActivity.this,FavoriteActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void getCategories(){
        pb.setVisibility(View.VISIBLE);
        vm=ViewModelProviders.of(this).get(myViewModel.class);
        vm.getCategories();
        vm.getMutableLiveData().observe(this, new Observer<categories>() {
            @Override
            public void onChanged(categories categories) {
                recAdapter adapter = new recAdapter();
                adapter.setCategs(categories.getCategories());
                adapter.setListener(CategoriesActivity.this);
                rec.setAdapter(adapter);
                rec.setLayoutManager(new GridLayoutManager(CategoriesActivity.this,3));
                rec.setHasFixedSize(true);
                pb.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onItemClick(List<categories.CategoriesBean>categs,int index) {
        Bundle bundle=new Bundle();
        bundle.putSerializable("category", (Serializable) categs);
        Intent intent=new Intent(CategoriesActivity.this, CategoryMeals.class);
        intent.putExtra("category",bundle);
        intent.putExtra("index",index);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMealClick(String id) {
        Intent intent = new Intent(CategoriesActivity.this,MealDetails.class);
        intent.putExtra("mealId",id);
        intent.putExtra("search",true);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        if(counter == 0){
            Toast.makeText(this, "Back Again To Exit!!", Toast.LENGTH_SHORT).show();
            counter++;
        }
        else{
            finish();
        }
    }
}