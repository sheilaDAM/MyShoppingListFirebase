package com.sheilajnieto.myshoppinglistfirebase;/*
@author sheila j. nieto 
@version 0.1 2024 -01 - 28
*/

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sheilajnieto.myshoppinglistfirebase.models.Category;
import com.sheilajnieto.myshoppinglistfirebase.models.Product;
import com.sheilajnieto.myshoppinglistfirebase.models.adapters.CategoryListAdapter;
import com.sheilajnieto.myshoppinglistfirebase.models.adapters.ProductListAdapter;

import java.util.List;

public class ProductsInCategoryListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProductListAdapter productListAdapter;
    private FirebaseFirestore db;
    private CollectionReference productsCollection;
    private FirestoreRecyclerOptions<Product> options;
    private RecyclerView listRecView;
    private String categoryClickedId;
    private String categoryClickedName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtener el ID de la categoría de la actividad anterior
        categoryClickedId = getIntent().getStringExtra("categoryId");
        categoryClickedName = getIntent().getStringExtra("categoryName");

        Toast.makeText(this, "Entró en listas para mostrarlas ", Toast.LENGTH_SHORT).show();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(categoryClickedName);
        setSupportActionBar(toolbar);

        loadProductsInCategoryList();

    } //fin onCreate


    public void loadProductsInCategoryList() {
        db = FirebaseFirestore.getInstance();
        listRecView = findViewById(R.id.recView);
        DocumentReference categoryClicked = db.collection("categories").document(categoryClickedId);

        productsCollection = categoryClicked.collection("products");

        options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(productsCollection, Product.class)
                .build();

        Log.d("SNAPSHOT", "options: " + options.getSnapshots());

        productListAdapter = new ProductListAdapter(options);

        listRecView.setLayoutManager(new GridLayoutManager(this, 3));
        listRecView.setAdapter(productListAdapter);
        productListAdapter.startListening();

    } // fin loadProductsInCategoryList

}