package com.sheilajnieto.myshoppinglistfirebase.models.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sheilajnieto.myshoppinglistfirebase.activities.ProductsInShoppingList;
import com.sheilajnieto.myshoppinglistfirebase.activities.ProductsInShoppingListEmpty;
import com.sheilajnieto.myshoppinglistfirebase.R;
import com.sheilajnieto.myshoppinglistfirebase.models.ListClass;
import com.sheilajnieto.myshoppinglistfirebase.models.ProductList;

import java.util.ArrayList;

public class ShoppingListAdapter extends FirestoreRecyclerAdapter<ListClass, ShoppingListAdapter.ListClassViewHolder> {

    private FirebaseFirestore db;
    private String shoppingListSelectedId;
    private String shoppingListSelectedName;

    public ShoppingListAdapter(@NonNull FirestoreRecyclerOptions<ListClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ListClassViewHolder holder, int position, @NonNull ListClass model) {
        holder.bindListClass(model);
    }

    @NonNull
    @Override
    public ShoppingListAdapter.ListClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_lists, parent, false);
        return new ShoppingListAdapter.ListClassViewHolder(v);
    }


    class ListClassViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvListName;
        private TextView tvCreationDate;
        private TextView tvProductsQuantity;
        private final Context context;

        public ListClassViewHolder(@NonNull View itemview) {
            super(itemview);
            this.context = itemview.getContext();
            this.tvListName = itemview.findViewById(R.id.tvListName);
            this.tvCreationDate = itemview.findViewById(R.id.tvCreationDate);
            this.tvProductsQuantity = itemview.findViewById(R.id.tvProductsQuantity);

            itemview.setOnClickListener(this);

        }

        public void bindListClass(ListClass list) {
            tvListName.setText(list.getName());
            tvCreationDate.setText(list.getDate());
            shoppingListSelectedId = getSnapshots().getSnapshot(getBindingAdapterPosition()).getId();
            // Obtenemos la cantidad de productos en la lista actual.
            getProductsQuantity(shoppingListSelectedId);
        }

        @Override
        public void onClick(View v) {
            shoppingListSelectedId = getSnapshots().getSnapshot(getBindingAdapterPosition()).getId();
            shoppingListSelectedName = tvListName.getText().toString();
            //AL PULSAR SOBRE UNA LISTA CONCRETA COMPROBAMOS SI HAY EN ELLA PRODUCTOS AÑADIDOS O NO
            checkProductsInList(shoppingListSelectedId, shoppingListSelectedName, context);
        }

        // Método para obtener y mostrar la cantidad de productos en la lista actual.
        private void getProductsQuantity(String shoppingListId) {
            db = FirebaseFirestore.getInstance();
            DocumentReference myListsRef = db.collection("myLists").document(shoppingListId);

            myListsRef.collection("productsInList").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot productsSnapshot) {
                    // Actualizamos la cantidad de productos en tvProductsQuantity.
                    tvProductsQuantity.setText(String.valueOf(productsSnapshot.size()));
                    Log.d("PRODUCTS QUANTITY", "Products quantity: " + productsSnapshot.size());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Error getting productsInList quantyty: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } //fin class ListClassViewHolder

        private void checkProductsInList(String shoppingListSelectedId, String shoppingListSelectedName, Context context) {
            db = FirebaseFirestore.getInstance();
            // Obtén la referencia al documento de la lista en Firebase Firestore.
            DocumentReference myListsRef = db.collection("myLists").document(shoppingListSelectedId);

            myListsRef.collection("productsInList").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot productsSnapshot) {
                    // Verificamos si la colección productsInList existe y tiene productos.
                    if (!productsSnapshot.isEmpty()) {
                        Log.d("LIST ADAPTER", "HAY PRODUCTOS");
                        // Si hay productos, vamos a la actividad de la lista de productos.
                        Intent intent = new Intent(context, ProductsInShoppingList.class);
                        intent.putExtra("shoppingListId", shoppingListSelectedId);
                        intent.putExtra("shoppingListName", shoppingListSelectedName);
                        context.startActivity(intent);
                    } else {
                        Log.d("LIST ADAPTER", "NO HAY PRODUCTOS");
                        // Si no hay productos, muestra la actividad de "No hay productos".
                        showEmptyProductsActivity(context);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Maneja el error al obtener la colección productsInList.
                    Toast.makeText(context, "Error al obtener la colección productsInList: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        private void showEmptyProductsActivity(Context context) {
            Intent intent = new Intent(context, ProductsInShoppingListEmpty.class);
            intent.putExtra("shoppingListId", shoppingListSelectedId);
            intent.putExtra("shoppingListName", shoppingListSelectedName);
            context.startActivity(intent);
        }
    }

}


