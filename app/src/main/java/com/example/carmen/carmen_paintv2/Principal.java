package com.example.carmen.carmen_paintv2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Principal extends Activity {
    private Vista vista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        vista = (Vista) findViewById(R.id.lienzo);
        System.out.println("vista " + vista);

    }

    public void dibujarCirculo(View v) {
        vista.setForma("circulo");
    }

    public void dibujarDiana(View v) {
        vista.setForma("diana");
    }

    public void dibujarRectangulo(View v) {
        vista.setForma("rectangulo");
    }

    public void dibujarRecta(View v) {
        vista.setForma("rectaPoligono");
    }

    public void borrar(View v) {
        vista.borraPantalla();
    }

    public void borrarCon(View v){
        vista.setBorrar(true);
    }

    //Guardar Archivo
    public void guardar(View v) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("¿Desea guardar la imagen?");

        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                File carpeta = new File(Environment.getExternalStorageDirectory().getPath());
                File archivo = new File(carpeta, "dibujo.png");
                FileOutputStream fos = null;
                BufferedOutputStream bos;
                try {
                    fos = new FileOutputStream(archivo);
                    bos = new BufferedOutputStream(fos);
                    Bitmap  bm = Bitmap.createBitmap(
                            vista.getWidth(), vista.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bm);
                    vista.draw(canvas);
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//
                Toast.makeText(getApplicationContext(), "La foto ha sido guardada",
                        Toast.LENGTH_LONG).show();
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        alert.show();
    }

    //Abrir archivo
    public void cargarArchivo(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            Uri uri = data.getData();
            if (uri != null) {
                try {
                    Bitmap b = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    vista.setMapaDeBits(b);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    //Elegir tamaño del pincel
    public void tamañoPincel(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("¿Qué tamaño desea?");

        alert.setPositiveButton("Grande", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                vista.setPincel(getResources().getInteger(R.integer.grande));

            }
        });
        alert.setNeutralButton("Mediano", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                vista.setPincel(getResources().getInteger(R.integer.mediano));
            }
        });
        alert.setNegativeButton("Pequeño", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                vista.setPincel(getResources().getInteger(R.integer.pequeño));
            }
        });
        alert.show();
    }

    public void picker(View v){
        final ColorPicker cp = new ColorPicker(this, 100, 63, 196);
        cp.show();
        Button okColor = (Button)cp.findViewById(R.id.okColorButton);
        okColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vista.setColor(cp.getColor());
                cp.dismiss();
            }
        });
    }
}
