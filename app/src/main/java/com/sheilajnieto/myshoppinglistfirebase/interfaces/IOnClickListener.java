package com.sheilajnieto.myshoppinglistfirebase.interfaces;

public interface IOnClickListener {
    void onShoppingListClicked(int position, int listId);
    void onCategoryClicked(int position);
    void onProductClicked(int position, int productId);

}
