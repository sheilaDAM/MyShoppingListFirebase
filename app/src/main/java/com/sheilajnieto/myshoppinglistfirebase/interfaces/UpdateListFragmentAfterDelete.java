package com.sheilajnieto.myshoppinglistfirebase.interfaces;/*
@author sheila j. nieto 
@version 0.1 2024 -01 - 21
*/


/*
 ------- ESTA INTERFAZ LA UTILIZAMOS PARA PODER ACCEDER A UN MÉTODO DEL MAIN DESDE LA CLASE SwipeToDelete
    y esto lo queremos para que al deslizar y borrar la última lista de la compra y quedarse el listado vacío
    nos vuelva a actualizar el fragment y poner que no hay listas creadas
*/
public interface UpdateListFragmentAfterDelete {
        void updateFragmentVisibility();
}
