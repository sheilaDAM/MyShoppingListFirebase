package com.sheilajnieto.myshoppinglistfirebase.models.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sheilajnieto.myshoppinglistfirebase.ProductsInShoppingList;
import com.sheilajnieto.myshoppinglistfirebase.ProductsInShoppingListEmpty;
import com.sheilajnieto.myshoppinglistfirebase.R;
import com.sheilajnieto.myshoppinglistfirebase.models.ListClass;
import com.sheilajnieto.myshoppinglistfirebase.models.Product;
import com.sheilajnieto.myshoppinglistfirebase.models.ProductList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListAdapter extends FirestoreRecyclerAdapter<ListClass, ShoppingListAdapter.ListClassViewHolder> {

    private FirebaseFirestore db;
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
            tvProductsQuantity.setText(String.valueOf(list.getProductQuantity()));
        }

        @Override
        public void onClick(View v) {
            String listClickedId = getSnapshots().getSnapshot(getBindingAdapterPosition()).getId();
            checkProductsInList(listClickedId, context);
        }
    }

    private void checkProductsInList(String listClickedId, Context context) {
        db = FirebaseFirestore.getInstance();
        // Obtén la referencia al documento de la lista en Firebase Firestore.
        DocumentReference listRef = db.collection("myLists").document(listClickedId);

        // Realiza la consulta para obtener el documento de la lista.
        listRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // Verifica si el documento existe y contiene la información de productos.
                if (documentSnapshot.exists()) {
                    // Obten la información de productos del documento (de la lista).
                    ArrayList<ProductList> productListInListClicked = documentSnapshot.toObject(ListClass.class).getProductsList();
                    //Comprobamos si hay productos añadidos en la lista pulsada
                    if (productListInListClicked != null && !productListInListClicked.isEmpty()) {
                        // Si hay productos, ir a la actividad de la lista de productos.
                        Intent intent = new Intent(context, ProductsInShoppingList.class);
                        // Pasamos la lista de productos al intent.
                        intent.putExtra("productList", productListInListClicked);
                        intent.putExtra("listClickedId", listClickedId);
                        context.startActivity(intent);
                    } else {
                        // Si no hay productos mostramos la actividad de "No hay productos".
                        Intent intent = new Intent(context, ProductsInShoppingListEmpty.class);
                        intent.putExtra("listClickedId", listClickedId);
                        context.startActivity(intent);
                    }
                } else {
                    Toast.makeText(context, "Error al obtener contenido de la lista pulsada.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Maneja el error al obtener la lista de productos de la categoría.
                Toast.makeText(context, "Error al obtener la lista de productos de la categoría.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}


