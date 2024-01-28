package com.sheilajnieto.myshoppinglistfirebase;/*
@author sheila j. nieto 
@version 0.1 2024 -01 - 24
*/

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.sheilajnieto.myshoppinglistfirebase.interfaces.UpdateListFragmentAfterDelete;
import com.sheilajnieto.myshoppinglistfirebase.models.ListClass;
import com.sheilajnieto.myshoppinglistfirebase.models.adapters.CategoryListAdapter;
import com.sheilajnieto.myshoppinglistfirebase.models.adapters.ShoppingListAdapter;
import com.sheilajnieto.myshoppinglistfirebase.ui.ShowAddListBoxDialog;

import java.util.List;

public class ShoppingListsActivity extends AppCompatActivity implements UpdateListFragmentAfterDelete, ShowAddListBoxDialog.OnListAddedListener {

    private Button btnAdd;
    private Toolbar toolbar;
    private ShoppingListAdapter shoppingListAdapter;
    private FirebaseFirestore db;
    private CollectionReference myListCollection;
    private FirestoreRecyclerOptions<ListClass> options;
    private RecyclerView listRecView;
    private ItemTouchHelper itemTouchHelper;
    private boolean shoppingListClicked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "Entró en listas para mostrarlas " , Toast.LENGTH_SHORT).show();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Mis listas compra");
        setSupportActionBar(toolbar); //esto es para que se muestre la toolbar

        btnAdd = findViewById(R.id.btAdd);

        loadShoppingLists();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shoppingListClicked == false) {
                    showAddListBoxDialog();
                }else {

                }
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                shoppingListClicked = false;
                finish();
            }
        });


    } //fin onCreate


    private void loadShoppingLists() {

        // Configuramos el RecyclerView
        listRecView = findViewById(R.id.recView);

        db = FirebaseFirestore.getInstance();
        myListCollection = db.collection("myLists");

        // Configuramos las opciones del adaptador, es decir, los datos que le pasamos al adaptador para que los cargue en el RecyclerView
        options = new FirestoreRecyclerOptions.Builder<ListClass>()
                .setQuery(myListCollection, ListClass.class)
                .build();

        // Inicializamos el adaptador con las opciones (las listas) que rescatamos de firebase
        shoppingListAdapter = new ShoppingListAdapter(options);
        itemTouchHelper = new ItemTouchHelper(new SwipeToDelete(shoppingListAdapter, this));
        itemTouchHelper.attachToRecyclerView(listRecView);
        // Establecemos el adaptador en el RecyclerView
        listRecView.setAdapter(shoppingListAdapter);
        listRecView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        listRecView.setHasFixedSize(true);
        listRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        shoppingListAdapter.startListening();
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

    //ESTE MÉTODO SE EJECUTA CUANDO SE HACE CLICK EN EL BOTÓN DE AÑADIR LISTA PARA MOSTRAR EL CUADRO PARA INSERTAR EL NOMBRE DE LA LISTA
    private void showAddListBoxDialog() {
        ShowAddListBoxDialog dialog = new ShowAddListBoxDialog();
        dialog.setOnListAddedListener(this);
        dialog.show(getSupportFragmentManager(), "AddListDialogFragment");
    }

    //ESTE MÉTODO SE EJECUTA CUANDO SE ACEPTA EL NOMBRE DE LA LISTA QUE SE QUIERE AÑADIR Y SE INSERTA EN LA BASE DE DATOS
    @Override
    public void onListAdded(String listName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentDateTime = DateTimeHelper.getCurrentDateTime();
        // Crea una nueva instancia de ListClass con los datos proporcionados
        ListClass newList = new ListClass(listName, currentDateTime, 0);

        db.collection("myLists").add(newList)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(MainActivity.class.getSimpleName(), "Document added with ID: " + documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(MainActivity.class.getSimpleName(), "Error adding product.: ");
                        Log.d(MainActivity.class.getSimpleName(), e.getMessage());
                    }

                });

        //Una vez añadida la lista a la bd de firebase, la mostraremos en la activity con el recyclerview
        showShoppingListsActivity();

    }

    private void showShoppingListsActivity() {
        Intent intent = new Intent(this, ShoppingListsActivity.class);
        startActivity(intent);
    }

    @Override
    public void updateFragmentVisibility() {
        loadShoppingLists();
    }
}
