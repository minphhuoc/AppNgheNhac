package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicapp.adapter.CategoryAdapter2;
import com.example.musicapp.adapter.CategoryAdapter3;
import com.example.musicapp.adapter.CategoryAdapter4;
import com.example.musicapp.databinding.ActivityMainBinding;
import com.example.musicapp.model.CategoryModel2;
import com.example.musicapp.model.CategoryModel3;
import com.example.musicapp.model.CategoryModel4;
import com.example.musicapp.model.InfoModel;
import com.example.musicapp.model.SongModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> songIdList;
    private int currentSongIndex;
    RecyclerView recyclerViewRV;
    FirebaseAuth mAuth;
    SongListActivity songListActivity;
    CategoryAdapter3 categoryAdapter3;
    CategoryAdapter2 categoryAdapter2;
    CategoryAdapter4 categoryAdapter4;
    Intent intent = null;
    private  String userId;

    ImageView user2, search;
    DrawerLayout drawerLayout;
    private ActivityMainBinding binding;
    FirebaseUser user;
    EditText txtTen;
    TextView txtTenuser1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        songIdList = new ArrayList<>();
        songIdList.add("ID bài hát 1");
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TextView myTextView = (TextView) findViewById(R.id.header1);
        Typeface typeFace = ResourcesCompat.getFont(this, R.font.sf1);
        myTextView.setTypeface(typeFace);
        addControls();
        getCategories4();
        getCategories3();
        showPlayerView();
        getCategories2();
        showPlayerView();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        if(user!=null){
            userId=user.getUid();

        }
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SearchViewActivity.class);
                startActivity(intent);
            }
        });
        user2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }
        });
    }


    private void showBottomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);
        txtTenuser1 = dialog.findViewById(R.id.txtTenuser);

        LinearLayout videoLayout = dialog.findViewById(R.id.layoutVideo);
        Button cancelButton = dialog.findViewById(R.id.cancelButton);


        FirebaseFirestore.getInstance().collection("users")
                .document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        InfoModel infoModel = documentSnapshot.toObject(InfoModel.class);
                        if (infoModel != null && infoModel.getNameUser() != null) {
                            txtTenuser1.setText(infoModel.getNameUser());
                        } else {
                            txtTenuser1.setText("Thông tin không khả dụng");
                        }
                    }
                });


        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(MainActivity.this,"Upload a Video is clicked",Toast.LENGTH_SHORT).show();

            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                signOutUser();
            }

            private void signOutUser() {
                Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                SongModel currentSong = MyExoplayer.getCurrentSong();
                if(currentSong != null){
                    MyExoplayer.setCurrentSong();
                }
            }

        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void getCategories4() {
        FirebaseFirestore.getInstance().collection("category4")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<CategoryModel4> categoryList4= queryDocumentSnapshots.toObjects(CategoryModel4.class);
                    setupCategoryRecyclerView4(categoryList4);
                });
    }

    private void setupCategoryRecyclerView4(List<CategoryModel4> categoryList4) {
        categoryAdapter4 = new CategoryAdapter4(categoryList4);
        binding.categoriesRecycleView4.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.categoriesRecycleView4.setAdapter(categoryAdapter4);
    }
    private void getCategories3() {
        FirebaseFirestore.getInstance().collection("category3")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<CategoryModel3> categoryList3= queryDocumentSnapshots.toObjects(CategoryModel3.class);
                    setupCategoryRecyclerView3(categoryList3);
                });
    }

    private void setupCategoryRecyclerView3(List<CategoryModel3> categoryList3) {
        categoryAdapter3 = new CategoryAdapter3(categoryList3);
        binding.categoriesRecycleView3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.categoriesRecycleView3.setAdapter(categoryAdapter3);
    }

    private void getCategories2() {
        FirebaseFirestore.getInstance().collection("category2")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<CategoryModel2> categoryList2 = queryDocumentSnapshots.toObjects(CategoryModel2.class);
                    setupCategoryRecyclerView2(categoryList2);
                });
    }

    private void setupCategoryRecyclerView2(List<CategoryModel2> categoryList2) {
        categoryAdapter2 = new CategoryAdapter2(categoryList2);
        binding.categoriesRecycleView2.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        binding.categoriesRecycleView2.setAdapter(categoryAdapter2);
    }


    private void addControls() {
        txtTenuser1=findViewById(R.id.txtTenuser);

        user2=findViewById(R.id.icUser);
        search=findViewById(R.id.searchicon);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.icUser)
        {
            Intent intent= new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showPlayerView();
    }


    public void showPlayerView() {
        binding.playerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongModel currentSong = MyExoplayer.getCurrentSong();
                if (currentSong != null) {
                    Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                    intent.putStringArrayListExtra("songIdList", new ArrayList<>(songIdList));
                    intent.putExtra("currentSongIndex", currentSongIndex);
                    startActivity(intent);

                }
            }
        });

        SongModel currentSong = MyExoplayer.getCurrentSong();
        if (currentSong != null) {
            binding.playerview.setVisibility(View.VISIBLE);
            binding.songTitleTextView2.setText("Đang phát : " + currentSong.getTitle2());
            Glide.with(binding.songCoverImageView2)
                    .load(currentSong.getCoverUrl2())
                    .apply(new RequestOptions().transform(new RoundedCorners(32)))
                    .into(binding.songCoverImageView2);
        } else {
            binding.playerview.setVisibility(View.GONE);

        }
    }









}