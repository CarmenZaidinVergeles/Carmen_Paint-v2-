package com.example.carmen.carmen_paintv2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Carmen on 15/02/2016.
 */
public class Vista extends View {

    private float x0, y0, xi, yi;
    private Path rectaPoligonal = new Path();
    private Bitmap mapaDeBits;
    private Canvas lienzoFondo;
    private Paint pincel,canvasPincel;
    private String pintar;
    private String forma="rectaPoligono";
    private float x=0,y=0;
    private boolean pinta=true;
    private float tamaño;
    private boolean borrar = false;
    private int color;


    public Vista(Context context, AttributeSet attrs) {
        super(context,attrs);
        pincel = new Paint();
        pincel.setColor(Color.GRAY);
        pincel.setAntiAlias(true);
        tamaño = 5;
        pincel.setStrokeWidth(tamaño);
            pincel.setStyle(Paint.Style.STROKE);
            pincel.setStrokeJoin(Paint.Join.ROUND);
            pincel.setStrokeCap(Paint.Cap.ROUND);
            canvasPincel = new Paint(Paint.DITHER_FLAG);

    }

    @Override
    public void onDraw(Canvas lienzo) {
        lienzo.drawBitmap(mapaDeBits, 0, 0, canvasPincel);

        if(pinta) {
            switch (forma) {
                case "recta":

                    break;
                case "rectaPoligono":
                    pincel.setStyle(Paint.Style.STROKE);
                    lienzo.drawPath(rectaPoligonal, pincel);
                    break;
                case "circulo":
                    pincel.setStyle(Paint.Style.FILL);
                    lienzo.drawCircle(x0, y0,
                            (float) Math.sqrt(Math.pow(x0 - xi, 2) + Math.pow(y0 - yi, 2)), pincel);
                    break;
                case "diana":
                    pincel.setStyle(Paint.Style.STROKE);
                    lienzo.drawCircle(x0, y0,
                            (float) Math.sqrt(Math.pow(x0 - xi, 2) + Math.pow(y0 - yi, 2)), pincel);
                    break;

                case "rectangulo":
                    pincel.setStyle(Paint.Style.FILL);
                    lienzo.drawRect(x0,y0,xi,yi,pincel);
                    break;

            }
        }else{
            pinta=true;
        }
    }


    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mapaDeBits = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        lienzoFondo = new Canvas(mapaDeBits);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x0 = xi = event.getX();
                y0 = yi = event.getY();
                rectaPoligonal.moveTo(x,y);//mueve la recta poligonal hasta la pos pulsada
                break;
            case MotionEvent.ACTION_MOVE:
                xi = x;
                yi = y;
                if (getForma().equals("rectaPoligono")) {
                    rectaPoligonal.lineTo(x, y);
                }
                break;
            case MotionEvent.ACTION_UP:
                rectaPoligonal.lineTo(x, y);
                preguntaForma(getForma());
                rectaPoligonal.reset();
                break;
        }
        invalidate();
        return true;
    }

    public Bitmap devolverLienzo(){
        return mapaDeBits;
    }

    public void setMapaDeBits(Bitmap bitmap){
        lienzoFondo.drawBitmap(bitmap, getLeft(), getTop(), pincel);

    }

    public void preguntaForma(String forma){
        switch (forma) {
            case "recta":

                break;
            case "rectaPoligono":
                pincel.setStyle(Paint.Style.STROKE);
                lienzoFondo.drawPath(rectaPoligonal, pincel);
                break;
            case "circulo":
                pincel.setStyle(Paint.Style.FILL);
                lienzoFondo.drawCircle(x0, y0,
                        (float) Math.sqrt(Math.pow(x0 - xi, 2) + Math.pow(y0 - yi, 2)), pincel);
                break;
            case "diana":
                pincel.setStyle(Paint.Style.STROKE);
                lienzoFondo.drawCircle(x0, y0,
                        (float) Math.sqrt(Math.pow(x0 - xi, 2) + Math.pow(y0 - yi, 2)), pincel);
                break;

            case "rectangulo":
                pincel.setStyle(Paint.Style.FILL);
                lienzoFondo.drawRect(x0,y0,xi,yi,pincel);
                break;

        }
    }

    public void borraPantalla() {
        pinta=false;
        if(!pinta){
            lienzoFondo.drawColor(0, PorterDuff.Mode.CLEAR);
            invalidate();
        }
    }

    public String getForma() {
        return forma;
    }

    public void setForma(String f) {
        this.forma = f;
        setBorrar(false);
    }

    public void setColor(int color){
        this.color = color;
        pincel.setColor(color);
    }



    public void setPincel(int nuevoTamaño){
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                nuevoTamaño, getResources().getDisplayMetrics());
        tamaño = pixelAmount;
        pincel.setStrokeWidth(tamaño);
    }

    public void setBorrar(boolean borrar){
        this.borrar=borrar;
        if(this.borrar) {
            pincel.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }else{
            pincel.setXfermode(null);
        }
    }


}