package com.example.pm1e1luis.Models;

import static com.example.pm1e1luis.R.id.callbtn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;



import com.example.pm1e1luis.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ActivityLLamar extends AppCompatActivity {

    EditText phoneNo;
    FloatingActionButton btnLLamar;
    static int PERMISSION_CODE= 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamar);

        phoneNo = findViewById(R.id.editTextPhone);
        btnLLamar = findViewById(R.id.callbtn);

        if (ContextCompat.checkSelfPermission(ActivityLLamar.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(ActivityLLamar.this,new String[]{android.Manifest.permission.CALL_PHONE},PERMISSION_CODE);

        }


        btnLLamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneno = phoneNo.getText().toString();
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:"+phoneNo));
                startActivity(i);

            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            int datoSeleccionado = intent.getIntExtra("datoSeleccionado",0);

                EditText editText = findViewById(R.id.editTextPhone);
                editText.setText(String.valueOf(datoSeleccionado));

        }


    }
}