package com.example.musicapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicapp.PlayerActivity;
import com.example.musicapp.SongListActivity;
import com.example.musicapp.databinding.CategoryItemsRecyclerRow2Binding;
import com.example.musicapp.databinding.CategoryItemsRecyclerRow3Binding;
import com.example.musicapp.databinding.CategoryItemsRecyclerRow4Binding;
import com.example.musicapp.model.CategoryModel;
import com.example.musicapp.model.CategoryModel2;
import com.example.musicapp.model.CategoryModel3;
import com.example.musicapp.model.CategoryModel4;


import java.util.List;

public class CategoryAdapter4 extends RecyclerView.Adapter<CategoryAdapter4.MyViewHolder> {

    private final List<CategoryModel4> categoryList4;

    public CategoryAdapter4(List<CategoryModel4> categoryList4) {
        this.categoryList4 = categoryList4;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final CategoryItemsRecyclerRow4Binding binding;

        public MyViewHolder(CategoryItemsRecyclerRow4Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(CategoryModel4 category) {
            binding.txtBaihat.setText(category.getName4());
            binding.txtArtist.setText(category.getTitle4());
            Glide.with(binding.coverImgView4)
                    .load(category.getCoverUrl4())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(32)))
                    .into(binding.coverImgView4);

            final android.content.Context context = binding.getRoot().getContext();
            binding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(context, SongListActivity.class);
                intent.putExtra("CATEGORY4_DATA", category);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CategoryItemsRecyclerRow4Binding binding = CategoryItemsRecyclerRow4Binding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return categoryList4.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CategoryModel4 category = categoryList4.get(position);
        holder.bindData(category);
    }
}



