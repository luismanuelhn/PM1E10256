package com.example.pm1e1luis.Models;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pm1e1luis.MainActivity;
import com.example.pm1e1luis.R;
import com.example.pm1e1luis.configuracion.SQLiteConexion;
import com.example.pm1e1luis.configuracion.Transacciones;

import java.util.ArrayList;

public class ListaActivity extends AppCompatActivity {

    SQLiteConexion conexion;
    ListView listView;
    ArrayList<Contactos> listcontact;
    Button btnEliminar;

    ArrayList<String> ArregloContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        try
        {
            // Establecemos una conxion a base de datos
            conexion = new SQLiteConexion(this, Transacciones.namedb, null, 1);
            listView = (ListView) findViewById(R.id.listcontactos);
            GetContac();
            btnEliminar=findViewById(R.id.btnEliminar);




            ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1,ArregloContactos);
            listView.setAdapter(adp);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                private long lastClickTime = 0;
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    long clickTime = System.currentTimeMillis();
                    if (clickTime - lastClickTime < 500) {
                        // Doble clic detectado
                        lastClickTime = 0;

                        // Obtén el dato seleccionado desde el adaptador o base de datos
                        int selectedData = listcontact.get(i).getTelefono();

                        mostrarAlertDialog(selectedData);


                        // Inicia la segunda Activity y pasa el dato
                       // Intent intent = new Intent(ListaActivity.this, ActivityLLamar.class);
                        //intent.putExtra("datoSeleccionado", selectedData);
                        //startActivity(intent);
                    } else {
                        lastClickTime = clickTime;
                    }

                    String ItemPerson = listcontact.get(i).getNombres();
                    Toast.makeText(ListaActivity.this, "Nombre " + ItemPerson, Toast.LENGTH_LONG).show();

                    btnEliminar.setOnClickListener(new View.OnClickListener(){


                        @Override

                        public void onClick(View v) {
                         Integer ItemPerson=listcontact.get(i).getId();
                            deleteContact(ItemPerson);
                          adp.notifyDataSetChanged();


                        }

                    });

                }
            });




        }
        catch (Exception ex)
        {
            ex.toString();
        }
    }

    private void GetContac() {

        SQLiteDatabase db = conexion.getReadableDatabase();
        Contactos contac = null;
        listcontact = new ArrayList<Contactos>();

        Cursor cursor = db.rawQuery(Transacciones.SelectTablePersonas,null);
        while(cursor.moveToNext())
        {
            contac = new Contactos();
            contac.setId(cursor.getInt(0));
            contac.setNombres(cursor.getString(2));
            contac.setTelefono(cursor.getInt(3));


            listcontact.add(contac);
        }

        cursor.close();
        FillList();
    }

    private void deleteContact(int contactId) {
        SQLiteDatabase db = conexion.getWritableDatabase();


        String tableName = "personas";
        String whereClause = "id = ?";


        String[] whereArgs = { String.valueOf(contactId) };


        int rowsDeleted = db.delete(tableName, whereClause, whereArgs);

        if (rowsDeleted > 0) {

            Toast.makeText(ListaActivity.this, "Registro eliminado", Toast.LENGTH_LONG).show();
            listView.notifyAll();

        } else {
            Toast.makeText(ListaActivity.this, "Error al eliminar  eliminado", Toast.LENGTH_LONG).show();
        }

        // Cierra la base de datos
        db.close();
    }

    private void FillList()
    {
        ArregloContactos = new ArrayList<String>();

        for(int i = 0; i < listcontact.size(); i++)
        {
            ArregloContactos.add(listcontact.get(i).getId() + " - " +
                    listcontact.get(i).getNombres() + " - " +
                    listcontact.get(i).getTelefono() );
        }
    }

    private void mostrarAlertDialog(final int selectedData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListaActivity.this);
        builder.setTitle("Confirmación");
        builder.setMessage("¿Deseas lLAMAR a este Numero?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // El usuario ha confirmado, así que iniciamos la segunda Activity
                Intent intent = new Intent(ListaActivity.this, ActivityLLamar.class);
                intent.putExtra("datoSeleccionado", selectedData);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Cierra el AlertDialog
            }
        });

        builder.create().show();
    }





}