package com.sheilajnieto.myshoppinglistfirebase.activities;/*
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sheilajnieto.myshoppinglistfirebase.R;
import com.sheilajnieto.myshoppinglistfirebase.models.Category;
import com.sheilajnieto.myshoppinglistfirebase.models.Product;
import com.sheilajnieto.myshoppinglistfirebase.models.adapters.ProductGridAdapter;
import com.sheilajnieto.myshoppinglistfirebase.models.adapters.ProductListAdapter;

import java.util.List;

public class ProductsInCategoryListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProductGridAdapter productGridAdapter;
    private FirebaseFirestore db;
    private CollectionReference productsCollection;
    private DocumentReference categoryDocument;
    private FirestoreRecyclerOptions<Product> options;
    private RecyclerView listRecView;
    private String categorySelectedId;
    private String shoppingListSelectedId;
    private String categoryClickedName;
    private String shoppingListSelectedName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtener el ID de la categoría de la actividad anterior
        categorySelectedId = getIntent().getStringExtra("categoryId");
        categoryClickedName = getIntent().getStringExtra("categoryName");
        shoppingListSelectedId = getIntent().getStringExtra("shoppingListId");
        shoppingListSelectedName = getIntent().getStringExtra("shoppingListName");

        Toast.makeText(this, "Entró en listas para mostrarlas ", Toast.LENGTH_SHORT).show();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(categoryClickedName);
        setSupportActionBar(toolbar);

        loadProductsInCategoryList(categorySelectedId);

    } //fin onCreate


    public void loadProductsInCategoryList(String categorySelectedId) {

        Toast.makeText(this, "Entró en loadProductsInCategoryList ", Toast.LENGTH_SHORT).show();
        db = FirebaseFirestore.getInstance();
        listRecView = findViewById(R.id.recView);
        categoryDocument = db.collection("categories").document(categorySelectedId);
        productsCollection = categoryDocument.collection("products");

        // Realiza la consulta para obtener el documento de la categoría
        categoryDocument.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Obtenemos la categoría seleccionada
                    Category category = documentSnapshot.toObject(Category.class);

                    if (category != null) {

                        // Creamos las opciones para el adaptador de productos
                        options = new FirestoreRecyclerOptions.Builder<Product>()
                                .setQuery(productsCollection, Product.class)
                                .build();

                        Log.d("CATEGORY", "Number of products in FirestoreRecyclerOptions: " + options.getSnapshots().size());

                        if (options != null) {
                            // Inicializamos el adaptador con las opciones (la lista de productos) que rescatamos de firebase
                            productGridAdapter = new ProductGridAdapter(options, shoppingListSelectedId, categorySelectedId, shoppingListSelectedName);
                            // Establecemos el adaptador en el RecyclerView
                            listRecView.setLayoutManager(new GridLayoutManager(ProductsInCategoryListActivity.this, 3));
                            listRecView.setAdapter(productGridAdapter);
                            productGridAdapter.startListening();
                        } else {
                            Log.e("Firestore", "FirestoreRecyclerOptions is null or empty.");
                        }
                    } else {
                        Log.e("Firestore", "Error parsing Category object");
                    }
                } else {
                    Log.e("Firestore", "Category document doesn't exist");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firestore", "Error getting category document: " + e.getMessage());
            }
        });

        /*

        db = FirebaseFirestore.getInstance();
        listRecView = findViewById(R.id.recView);
        DocumentReference categoryClicked = db.collection("categories").document(categoryClickedId);

        productsCollection = categoryClicked.collection("products");

        Log.d("MI LOG", "ID Categoría: " + categoryClickedId);

        options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(productsCollection, Product.class)
                .build();

        if (options != null && options.getSnapshots() != null) {
            // Inicializamos el adaptador con las opciones (la lista de productos) que rescatamos de firebase
            productListAdapter = new ProductListAdapter(options);
            // Establecemos el adaptador en el RecyclerView
            listRecView.setLayoutManager(new GridLayoutManager(ProductsInCategoryListActivity.this, 3));
            listRecView.setAdapter(productListAdapter);
            productListAdapter.startListening();
        } else {
            Log.e("Firestore", "FirestoreRecyclerOptions is null or empty.");
            // Puedes mostrar un mensaje al usuario o tomar alguna acción aquí
        }

        */

        /*
        productListAdapter = new ProductListAdapter(options);

        listRecView.setAdapter(productListAdapter);
        listRecView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        listRecView.setHasFixedSize(true);
        listRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        productListAdapter.startListening();

         */
        /*
        listRecView.setLayoutManager(new GridLayoutManager(this, 3));
        listRecView.setAdapter(productListAdapter);
        productListAdapter.startListening();

         */

    } // fin loadProductsInCategoryList

}