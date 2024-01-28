package com.sheilajnieto.myshoppinglistfirebase;/*
@author sheila j. nieto 
@version 0.1 2024 -01 - 27
*/

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import com.sheilajnieto.myshoppinglistfirebase.models.adapters.CategoryListAdapter;

public class ProductsInShoppingListEmpty extends AppCompatActivity {

    private Button btnAdd;
    private Toolbar toolbar;
    private CategoryListAdapter categoryListAdapter;
    private FirebaseFirestore db;
    private CollectionReference myListCollection;
    private FirestoreRecyclerOptions<Category> options;
    private RecyclerView listRecView;
    private String listClickedId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_products);

        listClickedId = getIntent().getStringExtra("listClickedId");

        Toast.makeText(this, "Entró en lista sin productos.", Toast.LENGTH_SHORT).show();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Mi lista");
        setSupportActionBar(toolbar);

        btnAdd = findViewById(R.id.btAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCategoryLists();
            }
        });



    } //fin onCreate

    private void loadCategoryLists() {

        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Mi lista");
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();
        // Obtenemos la referencia al documento de la lista en Firebase Firestore.
        DocumentReference listRef = db.collection("categories").document(listClickedId);

        // Realizamos la consulta para obtener el documento de la lista.
        listRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                listRecView = findViewById(R.id.recView);

                myListCollection = db.collection("categories");

                // Configuramos las opciones del adaptador, es decir, los datos que le pasamos al adaptador para que los cargue en el RecyclerView
                options = new FirestoreRecyclerOptions.Builder<Category>()
                        .setQuery(myListCollection, Category.class)
                        .build();

                // Inicializamos el adaptador con las opciones (las listas) que rescatamos de firebase
                categoryListAdapter = new CategoryListAdapter(options);
                // Establecemos el adaptador en el RecyclerView
                listRecView.setAdapter(categoryListAdapter);
                listRecView.addItemDecoration(new DividerItemDecoration(ProductsInShoppingListEmpty.this, DividerItemDecoration.VERTICAL));
                listRecView.setHasFixedSize(true);
                listRecView.setLayoutManager(new LinearLayoutManager(ProductsInShoppingListEmpty.this, LinearLayoutManager.VERTICAL, false));
                categoryListAdapter.startListening();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductsInShoppingListEmpty.this, "Error al obtener la lista de productos de la categoría.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); //qué queremos insertar y dónde queremos insertarlo (1ºelegimos el layout y el 2º dónde lo insertamos)
        return true; //true porque lo hemos tratado dándole un menú determinado
    }

    //ESTE CÓDIGO ES PARA MANEJAR EL MENÚ DE LA TOOLBAR
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_logout) {
            //Código para cerrar sesión
            return true;
        }
        return false;
    }

}