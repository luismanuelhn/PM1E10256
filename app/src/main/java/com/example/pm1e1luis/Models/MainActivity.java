package com.example.pm1e1luis.Models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm1e1luis.R;
import com.example.pm1e1luis.configuracion.Transacciones;
import com.example.pm1e1luis.configuracion.SQLiteConexion;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    //Luis Manuel Rodriguez Martinez PM1E1-202120060256
    EditText nombres,telefono,nota;
    Spinner pais2;
    static final int peticion_acceso_camara = 101;
    static final int peticion_toma_fotografica = 102;
    String currentPhotoPath;

    ImageView imageView;

    Button btntakefoto;
    Button btnprocesar;
    Button btnlista;
    String pathfoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"Honduras (504)", "Costa Rica", "Guatemala (502)"});

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.cbpais);
        spinner.setAdapter(adapter);



        nombres=(EditText)findViewById(R.id.txtNombre);
        pais2=(Spinner)findViewById(R.id.cbpais);
        telefono=(EditText)findViewById(R.id.txtTelefono);
        nota=(EditText)findViewById(R.id.txtNota);



        imageView=(ImageView) findViewById(R.id.imageView);
        btntakefoto=(Button) findViewById(R.id.btntakefoto);

        btntakefoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });

        btnprocesar = (Button) findViewById(R.id.btnGuardar);
        btnlista=(Button) findViewById(R.id.btnMostrar);

        btnlista.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                {
                   Intent intentcreate = new Intent(getApplicationContext(), ListaActivity.class);
                    startActivity(intentcreate);
                }
            }
        });

        btnprocesar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {


                    AddPerson();
                }
            }
        });




    }


    private void permisos() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.CAMERA},peticion_acceso_camara);
        }else{
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==peticion_acceso_camara){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }
            else{
                Toast.makeText(getApplicationContext(),"Permiso denegado",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void Tomarfoto() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent,peticion_toma_fotografica);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.pm1e1luis.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, peticion_toma_fotografica);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == peticion_toma_fotografica && resultCode == RESULT_OK)
        {
            /*
            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");
            imageView.setImageBitmap(image);
             */

            try
            {
                File foto = new File(currentPhotoPath);
                imageView.setImageURI(Uri.fromFile(foto));
            }
            catch (Exception ex)
            {
                ex.toString();
            }
        }


    }

    private void AddPerson()
    {
        try {
            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.namedb, null,1);
            SQLiteDatabase db =  conexion.getWritableDatabase();


           //Para crear las alertas a los compas para que no queden vacios
            String nombreV= nombres.getText ().toString ();
            String telefonoV = telefono.getText ().toString ();
            String notaV = nota.getText ().toString ();

            boolean todoLleno = true;
            String[] textos = {nombreV,telefonoV,notaV};

            for (String texto : textos) {
                if (texto.isEmpty ()) {
                    //Si alguno está vacío, cambiar el valor de la variable booleana y salir del bucle
                    todoLleno = false;
                    break;
                }
            }

            if (todoLleno) {
                ContentValues valores = new ContentValues();
                valores.put(Transacciones.telefono, telefono.getText().toString());
                valores.put(Transacciones.nombres, nombres.getText().toString());
                valores.put(Transacciones.pais, pais2.getSelectedItem().toString());
                valores.put(Transacciones.notas, nota.getText().toString());
                /**valores.put(Transacciones.correo, correo.getText().toString());**/



                Long Result = db.insert(Transacciones.Tabla, Transacciones.id, valores);

                Toast.makeText(this, getString(R.string.Respuesta), Toast.LENGTH_SHORT).show();
                db.close();
            } else {
                //Si alguno está vacío, mostrar una ventana emergente con un mensaje de advertencia
                AlertDialog.Builder builder = new AlertDialog.Builder (MainActivity.this);
                builder.setTitle ("Atención");
                builder.setMessage ("Debe rellenar todos los campos");
                builder.setPositiveButton ("Aceptar", null);
                builder.show ();
            }





        }
        catch (Exception exception)
        {
            Toast.makeText(this, getString(R.string.ErrorResp), Toast.LENGTH_SHORT).show();
        }

    }
}