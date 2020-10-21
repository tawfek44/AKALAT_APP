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
import com.example.sharedprefrence.classes.categories;

import java.util.List;

public class recAdapter extends RecyclerView.Adapter<recAdapter.categoryViewHolder> {

    private List<categories.CategoriesBean>categs;
    private Context context;
    private onItemClickListener listener;

    @NonNull
    @Override
    public categoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new categoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_element,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull categoryViewHolder holder, int position) {
        Glide.with(context).load(categs.get(position).getStrCategoryThumb()).into(holder.img);
        holder.name.setText(categs.get(position).getStrCategory());
    }

    @Override
    public int getItemCount() {
        if(categs == null)
            return 0;
        return categs.size();
    }

    public List<categories.CategoriesBean> getCategs() {
        return categs;
    }
    public void setCategs(List<categories.CategoriesBean> categs) {
        this.categs = categs;
    }
    public onItemClickListener getListener() {
        return listener;
    }
    public void setListener(onItemClickListener listener) {
        this.listener = listener;
    }



    interface onItemClickListener{
        public void onItemClick(List<categories.CategoriesBean>categs,int index);
    }

    public class categoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img;
        TextView name;
        public categoryViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.category_img);
            name = (TextView)itemView.findViewById(R.id.category_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(categs,getLayoutPosition());
        }
    }
}
