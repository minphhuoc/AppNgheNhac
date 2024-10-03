package com.example.musicapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicapp.SongListActivity;
import com.example.musicapp.databinding.CategoryItemsRecyclerRow2Binding;
import com.example.musicapp.databinding.CategoryItemsRecyclerRow3Binding;
import com.example.musicapp.model.CategoryModel;
import com.example.musicapp.model.CategoryModel2;
import com.example.musicapp.model.CategoryModel3;


import java.util.List;

public class CategoryAdapter3 extends RecyclerView.Adapter<CategoryAdapter3.MyViewHolder> {

    private final List<CategoryModel3> categoryList3;

    public CategoryAdapter3(List<CategoryModel3> categoryList3) {
        this.categoryList3 = categoryList3;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final CategoryItemsRecyclerRow3Binding binding;

        public MyViewHolder(CategoryItemsRecyclerRow3Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(CategoryModel3 category) {
            Glide.with(binding.coverImgView3)
                    .load(category.getCoverUrl3())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(32)))
                    .into(binding.coverImgView3);

            final android.content.Context context = binding.getRoot().getContext();
            binding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(context, SongListActivity.class);
                intent.putExtra("CATEGORY3_DATA", category);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CategoryItemsRecyclerRow3Binding binding = CategoryItemsRecyclerRow3Binding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return categoryList3.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CategoryModel3 category = categoryList3.get(position);
        holder.bindData(category);
    }
}



