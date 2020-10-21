package com.example.sharedprefrence;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

public class mealAdapter extends RecyclerView.Adapter<mealAdapter.mealViewHolder> {
    private  List<catogeryMeals.MealsBean> meals;
    private  List<mealDetail.MealsBean> searchMeals;
    private onMealClickListener listener;
    public List<catogeryMeals.MealsBean> getMeals() {
        return meals;
    }
    public void setMeals(List<catogeryMeals.MealsBean> meals) {
        this.meals = meals;
    }
    Context context;
    HashMap<String, Integer>fav;
    private String type;

    public mealAdapter() {

    }

    public mealAdapter(String type) {
        this.type = type;
    }



    @NonNull
    @Override
    public mealAdapter.mealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new mealViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull mealAdapter.mealViewHolder holder, int position) {

        if(type!= null && type.equals("search")){
            Glide.with(context).load(searchMeals.get(position).getStrMealThumb()).into(holder.mealImg);
            holder.mealName.setText(searchMeals.get(position).getStrMeal());
            holder.heart.setVisibility(View.GONE);
        }
        else if(type!= null && type.equals("fav")){
            Glide.with(context).load(meals.get(position).getStrMealThumb()).into(holder.mealImg);
            holder.mealName.setText(meals.get(position).getStrMeal());
            holder.heart.setVisibility(View.GONE);
        }
        else{
            Glide.with(context).load(meals.get(position).getStrMealThumb()).into(holder.mealImg);
            holder.mealName.setText(meals.get(position).getStrMeal());
            if(fav != null && fav.containsKey(meals.get(position).getIdMeal())){
                holder.heart.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart));
            }
            else{
                holder.heart.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart_outline));
            }
        }
    }

    @Override
    public int getItemCount() {
        if(type!= null && type.equals("search")) {
            if (searchMeals == null) return 0;
            return searchMeals.size();
        }
        else{
            if (meals == null) return 0;
            return meals.size();
        }
    }

    interface onMealClickListener{
        void onMealClick(String id);
    }

    public onMealClickListener getListener() {
        return listener;
    }

    public void setListener(onMealClickListener listener) {
        this.listener = listener;
    }

    public HashMap<String, Integer> getFav() {
        return fav;
    }

    public void setFav(HashMap<String, Integer> fav) {
        this.fav = fav;
    }

    public List<mealDetail.MealsBean> getSearchMeals() {
        return searchMeals;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSearchMeals(List<mealDetail.MealsBean> searchMeals) {
        this.searchMeals = searchMeals;
    }

    public class mealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView heart,mealImg;
        TextView mealName;
        public mealViewHolder(@NonNull View itemView) {
            super(itemView);
            heart=(ImageView)itemView.findViewById(R.id.heart);
            mealImg=(ImageView)itemView.findViewById(R.id.mealImg);
            mealName=(TextView) itemView.findViewById(R.id.mealName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(type!= null && type.equals("search"))
                listener.onMealClick(searchMeals.get(getLayoutPosition()).getIdMeal());
            else
                listener.onMealClick(meals.get(getLayoutPosition()).getIdMeal());
        }
    }
}
