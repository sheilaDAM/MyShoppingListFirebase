package com.sheilajnieto.myshoppinglistfirebase.models.adapters;

import android.content.Context;
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
import com.sheilajnieto.myshoppinglistfirebase.R;
import com.sheilajnieto.myshoppinglistfirebase.models.Product;

public class ProductListAdapter extends FirestoreRecyclerAdapter<Product, ProductListAdapter.ProductViewHolder> {


    public ProductListAdapter(@NonNull FirestoreRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model) {
        holder.bindProduct(model);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.griditem_product, parent, false);
        return new ProductViewHolder(v);
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProductImage;
        private TextView tvProductName;
        private final Context context;

        public ProductViewHolder(@NonNull View itemview) {
            super(itemview);
            this.context = itemview.getContext();
            this.ivProductImage = itemview.findViewById(R.id.ivProductImage);
            this.tvProductName = itemview.findViewById(R.id.tvProductName);

        }

        public void bindProduct(Product product) {
            String productImagePath = product.getImage();
            String imageNameWithoutExtension = productImagePath.replace(".jpg", "");
            int drawableID = context.getResources().getIdentifier(imageNameWithoutExtension, "drawable", context.getPackageName());

            //verificamos que se encontró el recurso (que existe la imagen en drawable)
            // Cargar la imagen como Bitmap
            Bitmap imageBitmap = BitmapFactory.decodeResource(context.getResources(), drawableID);
            ivProductImage.setImageBitmap(imageBitmap);
            tvProductName.setText(product.getName());
        }
    }
}