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
import com.example.musicapp.model.CategoryModel;
import com.example.musicapp.model.CategoryModel2;


import java.util.List;

public class CategoryAdapter2 extends RecyclerView.Adapter<CategoryAdapter2.MyViewHolder> {

    private final List<CategoryModel2> categoryList2;

    public CategoryAdapter2(List<CategoryModel2> categoryList2) {
        this.categoryList2 = categoryList2;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final CategoryItemsRecyclerRow2Binding binding;

        public MyViewHolder(CategoryItemsRecyclerRow2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(CategoryModel2 category) {
            binding.nameTextView2.setText(category.getName2());
            binding.nametitle.setText(category.getNametitle2());
            Glide.with(binding.coverImgView2)
                    .load(category.getCoverUrl2())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(32)))
                    .into(binding.coverImgView2);

            final android.content.Context context = binding.getRoot().getContext();
            binding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(context, SongListActivity.class);
                intent.putExtra("CATEGORY2_DATA", category);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CategoryItemsRecyclerRow2Binding binding = CategoryItemsRecyclerRow2Binding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return categoryList2.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CategoryModel2 category = categoryList2.get(position);
        holder.bindData(category);
    }
}




