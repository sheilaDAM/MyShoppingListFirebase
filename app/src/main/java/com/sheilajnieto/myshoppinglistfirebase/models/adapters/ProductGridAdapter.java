package com.sheilajnieto.myshoppinglistfirebase.models.adapters;/*
@author sheila j. nieto
@version 0.1 2024 -01 - 30
*/

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sheilajnieto.myshoppinglistfirebase.R;
import com.sheilajnieto.myshoppinglistfirebase.activities.ProductsInShoppingList;
import com.sheilajnieto.myshoppinglistfirebase.models.Product;

public class ProductGridAdapter extends FirestoreRecyclerAdapter<Product, ProductGridAdapter.ProductViewHolder> {

    private FirebaseFirestore db;
    private String shoppingListSelectedId;
    private String categorySelectedId;
    private String shoppingListSelectedName;

    public ProductGridAdapter(@NonNull FirestoreRecyclerOptions<Product> options, String shoppingListSelectedId, String categorySelectedId, String shoppingListSelectedName) {
        super(options);
        this.shoppingListSelectedId = shoppingListSelectedId;
        this.categorySelectedId = categorySelectedId;
        this.shoppingListSelectedName = shoppingListSelectedName;
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductGridAdapter.ProductViewHolder holder, int position, @NonNull Product model) {
        holder.bindProduct(model);
    }

    @NonNull
    @Override
    public ProductGridAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.griditem_product, parent, false);
        return new ProductGridAdapter.ProductViewHolder(v);
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView ivProductImage;
        private TextView tvProductName;
        private final Context context;

        public ProductViewHolder(@NonNull View itemview) {
            super(itemview);
            this.context = itemview.getContext();
            this.ivProductImage = itemview.findViewById(R.id.ivProductImage);
            this.tvProductName = itemview.findViewById(R.id.tvProductName);

            itemview.setOnClickListener(this);

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

        @Override
        public void onClick(View v) {
            String selectedProductId = getSnapshots().getSnapshot(getBindingAdapterPosition()).getId();
            addSelectedProductInSelectedList(selectedProductId, shoppingListSelectedId);
            Intent intent = new Intent(context, ProductsInShoppingList.class);
            intent.putExtra("shoppingListId", shoppingListSelectedId);
            intent.putExtra("categoryId", categorySelectedId);
            intent.putExtra("productId", selectedProductId);
            intent.putExtra("shoppingListName", shoppingListSelectedName);
            context.startActivity(intent);
        }
    } //fin class ProductViewHolder

    private void addSelectedProductInSelectedList(String selectedProductId, String shoppingListSelectedId) {
        db = FirebaseFirestore.getInstance();

        // Obtenemos la referencia al documento (a la lista seleccionada) en Firebase Firestore.
        DocumentReference listRef = db.collection("myLists").document(shoppingListSelectedId);

        // Obtenemos la referencia al documento del producto seleccionado dentro de la categoría seleccionada.
        // Primero accedemos al documento de la categoría seleccionada, luego a la colección de productos
        DocumentReference productInCategoryRef = db.collection("categories")
                .document(categorySelectedId)  // Reemplaza "categoryId" con la ID de la categoría seleccionada.
                .collection("products")
                .document(selectedProductId);

        // Obtenemos los datos concretos del producto seleccionado.
        productInCategoryRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Obtenemos los datos del producto desde la categoría seleccionada y los guardamos en un objeto Product.
                    Product selectedProduct = documentSnapshot.toObject(Product.class);
                    Log.d("PRODUCT SELECTED", "Product selected: " + selectedProduct.getName());
                    if (selectedProduct != null) {
                        //Si el producto existe, lo añadimos a una nueva colección de productos dentro de la lista seleccionada.
                        //A esta nueva colección dentro de la lista seleccionada la llamaremos "productsInList".
                        DocumentReference productInListRef = listRef.collection("productsInList")
                                .document(selectedProductId);

                        //Ahora añadimos la información del producto en productsInList con su referencia.
                        //Se habrá añadido en la colección de productos de la lista seleccionada.
                        productInListRef.set(selectedProduct)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("ADDING PRODUCT", "Product added to list successfully, product ID: " + selectedProductId);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("MI LOG", "Error adding product to list: " + e.getMessage());
                                    }
                                });
                    }
                }
            }
        });
    }
}
