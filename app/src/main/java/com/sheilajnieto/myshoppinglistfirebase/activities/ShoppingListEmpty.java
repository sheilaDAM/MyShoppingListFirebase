package com.sheilajnieto.myshoppinglistfirebase.activities;/*
@author sheila j. nieto 
@version 0.1 2024 -01 - 24
*/

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sheilajnieto.myshoppinglistfirebase.DateTimeHelper;
import com.sheilajnieto.myshoppinglistfirebase.R;
import com.sheilajnieto.myshoppinglistfirebase.models.ListClass;
import com.sheilajnieto.myshoppinglistfirebase.ui.ShowAddListBoxDialog;

// ------- ESTA ACTIVITY ES PARA MOSTRAR SI NO HAY LISTAS DE LA COMPRA CREADAS -------
public class ShoppingListEmpty extends AppCompatActivity implements ShowAddListBoxDialog.OnListAddedListener{

    private Button btnAdd;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_shopping_lists);

        Toast.makeText(this, "Entró en listas vacías." , Toast.LENGTH_SHORT).show();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //esto es para que se muestre la toolbar

        toolbar.setTitle("Mis listas compra");

        btnAdd = findViewById(R.id.btAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddListBoxDialog();
            }
        });
    } //fin onCreate

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
}
