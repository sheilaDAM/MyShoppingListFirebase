package com.sheilajnieto.myshoppinglistfirebase.activities;/*
@author sheila j. nieto 
@version 0.1 2024 -01 - 28
*/

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sheilajnieto.myshoppinglistfirebase.R;
import com.sheilajnieto.myshoppinglistfirebase.SwipeToDelete;
import com.sheilajnieto.myshoppinglistfirebase.interfaces.UpdateListFragmentAfterDelete;
import com.sheilajnieto.myshoppinglistfirebase.models.Category;
import com.sheilajnieto.myshoppinglistfirebase.models.Product;
import com.sheilajnieto.myshoppinglistfirebase.models.adapters.CategoryListAdapter;
import com.sheilajnieto.myshoppinglistfirebase.models.adapters.ProductListAdapter;

import java.util.ArrayList;

// ------- ESTA ACTIVITY ES PARA MOSTRAR LOS PRODUCTOS QUE SE AÑADAN EN LA LISTA SELECCONADA -------
public class ProductsInShoppingList extends AppCompatActivity implements UpdateListFragmentAfterDelete {

    private Toolbar toolbar;
    private Button btnAdd;
    private ProductListAdapter productListAdapter;
    private FirebaseFirestore db;
    private CollectionReference productsCollection;
    private DocumentReference categoryDocument;
    private FirestoreRecyclerOptions<Product> options;
    private RecyclerView listRecView;
    private String shoppingListSelectedId;
    private String shoppingListSelectedName;
    private String categorySelectedId;
    private CategoryListAdapter categoryListAdapter;
    private CollectionReference categoriesCollection;
    private FirestoreRecyclerOptions<Category> options2;
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener el ID de la categoría de la actividad anterior
        shoppingListSelectedName = getIntent().getStringExtra("shoppingListName");
        shoppingListSelectedId = getIntent().getStringExtra("shoppingListId");
        categorySelectedId = getIntent().getStringExtra("categoryId");

        Toast.makeText(this, "Entró en listas para mostrarlas ", Toast.LENGTH_SHORT).show();

        btnAdd = findViewById(R.id.btAdd);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(shoppingListSelectedName);
        setSupportActionBar(toolbar);

        Log.d("PRODUCT CLICKED", "ID selected shopping list: " + shoppingListSelectedId);
        Log.d("PRODUCT CLICKED", "ID selected category: " + categorySelectedId);
        Log.d("LIST NAME", "Name selected shopping list: " + shoppingListSelectedName);

        loadProductsInShoppingListSelected(shoppingListSelectedId);

        btnAdd.setOnClickListener(v -> {
            loadCategoryLists();
        });

    } //fin onCreate

    public void loadProductsInShoppingListSelected(String shoppingListSelectedId) {

        db = FirebaseFirestore.getInstance();
        listRecView = findViewById(R.id.recView);

        // Obtenemos la referencia a la colección de productos en la lista de compras seleccionada.
        CollectionReference productsInListCollection = db.collection("myLists")
                .document(shoppingListSelectedId)
                .collection("productsInList");

        // Configuramos las opciones del adaptador para obtener productos específicos añadidos de la lista de compras.
        options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(productsInListCollection, Product.class)
                .build();

        Log.d("LOAD PRODUCT", "ID selected shopping list: " + shoppingListSelectedId);
        Log.d("LOAD PRODUCT", "Product name: " + options.toString());

        productListAdapter = new ProductListAdapter(options, shoppingListSelectedId, categorySelectedId);
        itemTouchHelper = new ItemTouchHelper(new SwipeToDelete<>(productListAdapter, this));
        itemTouchHelper.attachToRecyclerView(listRecView);
        listRecView.setAdapter(productListAdapter);
        listRecView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        listRecView.setHasFixedSize(true);
        listRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        productListAdapter.startListening();
    }

    private void loadCategoryLists() {

        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Categorías");
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();
        // Obtenemos la referencia al documento de la lista en Firebase Firestore.
        DocumentReference listRef = db.collection("categories").document(shoppingListSelectedId);

        // Realizamos la consulta para obtener el documento de la lista.
        listRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                listRecView = findViewById(R.id.recView);

                categoriesCollection = db.collection("categories");

                // Configuramos las opciones del adaptador, es decir, los datos que le pasamos al adaptador para que los cargue en el RecyclerView
                options2 = new FirestoreRecyclerOptions.Builder<Category>()
                        .setQuery(categoriesCollection, Category.class)
                        .build();

                // Inicializamos el adaptador con las opciones (las listas) que rescatamos de firebase
                categoryListAdapter = new CategoryListAdapter(options2, shoppingListSelectedId, shoppingListSelectedName);
                // Establecemos el adaptador en el RecyclerView
                listRecView.setAdapter(categoryListAdapter);
                listRecView.addItemDecoration(new DividerItemDecoration(ProductsInShoppingList.this, DividerItemDecoration.VERTICAL));
                listRecView.setHasFixedSize(true);
                listRecView.setLayoutManager(new LinearLayoutManager(ProductsInShoppingList.this, LinearLayoutManager.VERTICAL, false));
                categoryListAdapter.startListening();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductsInShoppingList.this, "Error al obtener la lista de productos de la categoría.", Toast.LENGTH_SHORT).show();
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

    @Override
    public void updateFragmentVisibility() {
        loadProductsInShoppingListSelected(shoppingListSelectedId);
    }
}
