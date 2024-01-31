package com.sheilajnieto.myshoppinglistfirebase;/*
@author sheila j. nieto 
@version 0.1 2024 -01 - 20
*/

import android.content.Context;
import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.sheilajnieto.myshoppinglistfirebase.interfaces.UpdateListFragmentAfterDelete;
import com.sheilajnieto.myshoppinglistfirebase.models.adapters.ProductListAdapter;
import com.sheilajnieto.myshoppinglistfirebase.models.adapters.ShoppingListAdapter;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

// ------- ESTA CLASE ES PARA ELIMINAR ITEMS DE LISTA EN CUALQUIER ADAPTADOR -------

public class SwipeToDelete<T extends RecyclerView.Adapter<?>> extends ItemTouchHelper.SimpleCallback {

    private final T adapter;
    private final Context mainContext;
    // private final UpdateListFragmentAfterDelete updateListFragmentAfterDelete;


    public SwipeToDelete(T adapter, Context context) {
        super(0, ItemTouchHelper.LEFT);
        this.adapter = adapter;
        this.mainContext = context;
        //this.updateListFragmentAfterDelete = updateListFragmentAfterDelete;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getBindingAdapterPosition();
        DocumentSnapshot documentSnapshot = ((FirestoreRecyclerAdapter<T, ?>) adapter).getSnapshots().getSnapshot(position);
        documentSnapshot.getReference().delete();
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(mainContext, R.color.my_red))
                .addSwipeLeftActionIcon(R.drawable.delete_icon)
                .create()
                .decorate();

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}


// ------- ESTA CLASE ES PARA ELIMINAR UN PRODUCTO DE LA LISTA DE LA COMPRA para un adaptador específico -------
/*
public class SwipeToDelete extends ItemTouchHelper.SimpleCallback{

    private final ShoppingListAdapter adapter;
    private final Context mainContext;
   // private final UpdateListFragmentAfterDelete updateListFragmentAfterDelete;


    public SwipeToDelete(ShoppingListAdapter adapter, Context context) {
        super(0, ItemTouchHelper.LEFT);
        this.adapter = adapter;
        this.mainContext = context;
        //this.updateListFragmentAfterDelete = updateListFragmentAfterDelete;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getBindingAdapterPosition();
        DocumentSnapshot documentSnapshot = adapter.getSnapshots().getSnapshot(position);
        documentSnapshot.getReference().delete();
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(mainContext, R.color.my_red))
                .addSwipeLeftActionIcon(R.drawable.delete_icon)
                .create()
                .decorate();

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
*/