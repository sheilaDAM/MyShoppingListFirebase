package com.sheilajnieto.myshoppinglistfirebase.models.adapters;/*
@author sheila j. nieto 
@version 0.1 2024 -01 - 26
*/

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.sheilajnieto.myshoppinglistfirebase.ProductsInCategoryListActivity;
import com.sheilajnieto.myshoppinglistfirebase.R;
import com.sheilajnieto.myshoppinglistfirebase.models.Category;
import com.sheilajnieto.myshoppinglistfirebase.models.ListClass;

public class CategoryListAdapter extends FirestoreRecyclerAdapter<Category, CategoryListAdapter.CategoryViewHolder> {

    public CategoryListAdapter(@NonNull FirestoreRecyclerOptions<Category> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CategoryListAdapter.CategoryViewHolder holder, int position, @NonNull Category model) {
        holder.bindListClass(model);
    }

    @NonNull
    @Override
    public CategoryListAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_list, parent, false);
        return new CategoryListAdapter.CategoryViewHolder(v);
    }


    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivCategoryImage;
        private TextView tvCategoryName;
        private final Context context;

        public CategoryViewHolder(@NonNull View itemview) {
            super(itemview);
            this.context = itemview.getContext();
            this.ivCategoryImage = itemview.findViewById(R.id.ivCategoryImage);
            this.tvCategoryName = itemview.findViewById(R.id.tvCategoryListName);

            itemview.setOnClickListener(this);

        }

        public void bindListClass(Category category) {
            String productImagePath = category.getImage();
            String imageNameWithoutExtension = productImagePath.replace(".jpg", "");
            int drawableID = context.getResources().getIdentifier(imageNameWithoutExtension, "drawable", context.getPackageName());

            //verificamos que se encontró el recurso (que existe la imagen en drawable)
            // Cargar la imagen como Bitmap
            Bitmap imageBitmap = BitmapFactory.decodeResource(context.getResources(), drawableID);
            ivCategoryImage.setImageBitmap(imageBitmap);
            tvCategoryName.setText(category.getName());
        }

        @Override
        public void onClick(View v) {
            // Obtenemos el ID de la categoría seleccionada
            String categoryId = getSnapshots().getSnapshot(getBindingAdapterPosition()).getId();

            // Iniciar la actividad de la lista de productos en esa categoría
            Intent intent = new Intent(v.getContext(), ProductsInCategoryListActivity.class);
            intent.putExtra("categoryId", categoryId);
            intent.putExtra("categoryName", tvCategoryName.getText().toString());
            v.getContext().startActivity(intent);
        }
    }
}
