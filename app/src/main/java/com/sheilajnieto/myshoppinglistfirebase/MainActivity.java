package com.sheilajnieto.myshoppinglistfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sheilajnieto.myshoppinglistfirebase.models.adapters.ProductListAdapter;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser; //esto es para obtener el usuario que está logueado

    private ProductListAdapter productAdapter;
    private Button btnAdd;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //esto es para que se muestre la toolbar

        btnAdd = findViewById(R.id.btAdd);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //esto es para obtener el usuario que está logueado

      //  if (firebaseUser != null) {
      //      Toast.makeText(this, "Bienvenido: " + firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
            FirebaseFirestore db = FirebaseFirestore.getInstance(); //esto es para obtener la instancia de la base de datos

        db.collection("myLists")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                // No hay listas, mostrar la actividad "No hay listas creadas"
                                showEmptyShoppingLists();
                            } else {
                                // Hay listas, mostrar la actividad de shoppinglist
                                showShoppingLists();
                            }
                        }else {
                            Log.e(MainActivity.class.getSimpleName(), "Error getting documents (products): ", task.getException());
                        }
                    }
                });

        /*
            db.collection("myLists")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Verificar si hay documentos en la colección
                            if (task.getResult().isEmpty()) {
                                // No hay listas, mostrar la actividad "No hay listas creadas"
                                showEmptyShoppingLists();
                            } else {
                                // Hay listas, mostrar la actividad de shoppinglist
                                showShoppingLists();
                            }
                        } else {
                            // Ocurrió un error al realizar la consulta o no se obtuvieron listas porque no existen
                            Log.e("Firebase", "Error al obtener las listas", task.getException());
                            //si no hay listas creadas, mostrar la actividad "No hay listas creadas"
                            showEmptyShoppingLists();
                        }
                    });

         */

           /*
            RecyclerView rvProductos = findViewById(R.id.recView);

            CollectionReference ref = db.collection("product");
            Query query = ref.orderBy("price");
            FirestoreRecyclerOptions<Product> opciones = new FirestoreRecyclerOptions.Builder<Product>()
                    .setQuery(query, Product.class)
                    .build();
            productAdapter = new ProductAdapter(opciones);
            rvProductos.setAdapter(productAdapter);
            rvProductos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            productAdapter.startListening();

            */

/*
            List<Product> productsInCategory1 = new ArrayList<>();
            productsInCategory1.add(new Product("Manzana", false, "apples.jpg"));
            productsInCategory1.add(new Product("Pepino", false, "cucumber.jpg"));
            productsInCategory1.add(new Product("Uvas", false, "grapes.jpg"));
            productsInCategory1.add(new Product("Yuka", false, "yuka.jpg"));
            productsInCategory1.add(new Product("Patatas", false, "potatoes.jpg"));
            productsInCategory1.add(new Product("Boniato", false, "sweetpotatoe.jpg"));
            productsInCategory1.add(new Product("Zanahoria", false, "carrots.jpg"));
            productsInCategory1.add(new Product("Calabacín", false, "courgette.jpg"));
            productsInCategory1.add(new Product("Aguacate", false, "avocado.jpg"));
            productsInCategory1.add(new Product("Cebolla", false, "onion.jpg"));
            Category category1 = new Category("Frutas, verduras, tubérculos", "vegetablesfruits.jpg", productsInCategory1);

            List<Product> productsInCategory2 = new ArrayList<>();
            productsInCategory2.add(new Product("Leche de coco", false, "coconutmilk.jpg"));
            productsInCategory2.add(new Product("Yogur de coco", false, "cocoyoghurt.jpg"));
            Category category2 = new Category("Bebidas vegetales", "vegetablemilk.jpg", productsInCategory2);

            List<Product> productsInCategory3 = new ArrayList<>();
            productsInCategory3.add(new Product("Yogur queso cabra", false, "goatyoghurt.jpg"));
            Category category3 = new Category("Lácteos", "milks.jpg", productsInCategory3);


            List<Product> productsInCategory4 = new ArrayList<>();
            productsInCategory4.add(new Product("Limpiador de baños", false, "bathcleaner.jpg"));
            productsInCategory4.add(new Product("Limpiador vajilla", false, "dishcleaner.jpg"));
            productsInCategory4.add(new Product("Servilletas", false, "napkins.jpg"));
            productsInCategory4.add(new Product("Toallitas", false, "wipes.jpg"));
            productsInCategory4.add(new Product("Papel higiénico", false, "toiletpaper.jpg"));
            Category category4 = new Category("Productos de limpieza", "cleaning.jpg", productsInCategory4);

            List<Product> productsInCategory5 = new ArrayList<>();
            productsInCategory5.add(new Product("Aceite de oliva", false, "oliveoil.jpg"));
            productsInCategory5.add(new Product("Sal", false, "salt.jpg"));
            productsInCategory5.add(new Product("Vinagre de manzana", false, "vinegar.jpg"));
            productsInCategory5.add(new Product("Orégano", false, "oregano.jpg"));
            Category category5 = new Category("Condimentos", "condiments.jpg", productsInCategory5);

            List<Product> productsInCategory6 = new ArrayList<>();
            productsInCategory6.add(new Product("Arroz Basmati", false, "basmatirice.jpg"));
            Category category6 = new Category("Cereales", "cereal.jpg", productsInCategory6);

            List<Product> productsInCategory7 = new ArrayList<>();
            productsInCategory7.add(new Product("Proteína", false, "eggs.jpg"));
            Category category7 = new Category("Proteínas", "protein.jpg", productsInCategory7);

            db.collection("categories").add(category7)
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
*/

        /*
        } else {
            Log.d(MainActivity.class.getSimpleName(), "No hay usuarix logueado");
            Toast.makeText(this, "Usuarix desconocido", Toast.LENGTH_SHORT).show();
            ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult o) {
                            if (o.getResultCode() == RESULT_OK) {
                                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                Toast.makeText(MainActivity.this, "Bienvenidx: " + firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Acceso denegado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

            );

            resultLauncher.launch(AuthUI.getInstance().createSignInIntentBuilder().build());
        }

         */
    } // ------- fin onCreate ---------

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing() && productAdapter != null) {
            productAdapter.stopListening();
        }
    }

    private void showEmptyShoppingLists() {
        Intent intent = new Intent(this, ShoppingListEmpty.class);
        startActivity(intent);
    }

    private void showShoppingLists() {
        Intent intent = new Intent(this, ShoppingListsActivity.class);
        startActivity(intent);
    }

    //LAS OPCIONES DE MENÚ DEVUELVEN TRUE OR FALSE según si ha sido tratado
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); //qué queremos insertar y dónde queremos insertarlo (1ºelegimos el layout y el 2º dónde lo insertamos)
        return true; //true porque lo hemos tratado dándole un menú determinado
    }

    //SEGÚN LA OPCIÓN DEL MENÚ QUE ELIJAMOS SE CARGARÁ UN FRAGMENT U OTRO
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