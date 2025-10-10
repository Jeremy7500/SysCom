package UPsay.decouverteAndroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MonView extends View {
    float xText, yText;
    float tailleTexte = 0;
    float x1, x2, y1, y2;
    Sensor accelerometre;



    // Attributs pour le Timer
    Handler timerHandler = new Handler();
    Runnable updateTimerThread = new Runnable() {
        public void run() {
            tailleTexte += 1; // On augmente la taille
            // Cette ligne redéclenche le timer dans 100ms
            timerHandler.postDelayed(this, 100);
            // Force le redessin de la vue, ce qui appelle onDraw()
            invalidate();
        }
    };

    // Method to set text coordinates
    public void setXYText (float x, float y) {
        xText = x;
        yText = y;
    }

    // Constructor where you should initialize your variables
    public MonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Initialize text coordinates here
        setXYText(600, 600);
        timerHandler.postDelayed(updateTimerThread, 10);
        OnTouchListener onTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String direction = "";
                switch (event.getAction()) {
                    case (MotionEvent.ACTION_DOWN): // Quand l'utilisateur pose le doigt
                        x1 = event.getX();
                        y1 = event.getY();
                        break;
                    case (MotionEvent.ACTION_UP): { // Quand l'utilisateur relève le doigt
                        x2 = event.getX();
                        y2 = event.getY();
                        float dx = x2 - x1;
                        float dy = y2 - y1;

                        // On détermine la direction
                        if (Math.abs(dx) > Math.abs(dy)) {
                            if (dx > 0)
                                direction = "right";
                            else
                                direction = "left";
                        } else {
                            if (dy > 0)
                                direction = "down";
                            else
                                direction = "up";
                        }
                        // Affiche la direction dans les logs
                        Log.i("pacman", "relâché vers " + direction);
                        break;
                    }
                }
                invalidate();
                return true;
            }
        };
        setOnTouchListener(onTouchListener); // On attache le listener à notre vue

        // --- Code pour l'accéléromètre ---
        SensorManager m = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometre = m.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        final SensorEventListener mSensorEventListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // Pas nécessaire pour ce tutoriel
            }

            public void onSensorChanged(SensorEvent sensorEvent) {
                // Récupère les valeurs de l'accéléromètre sur les axes X et Y
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];

                // Affiche les valeurs dans le Logcat
                Log.i("accelerometer", "X=" + x + " Y=" + y);
            }
        };
        m.registerListener(mSensorEventListener, accelerometre, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onDraw(Canvas canvas) {
        Paint p = new Paint();

        // Step 1: Draw the background
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, getWidth(), getHeight(), p);

        // Step 2: Draw the image
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.nom);
        canvas.drawBitmap(b, 200, 200, p);

        // Step 3: Draw the text
        p.setColor(Color.GREEN);
        //p.setTextSize(100);
        p.setTextSize(tailleTexte);
        p.setTextAlign(Paint.Align.CENTER);
        String texte = "Bonjour MONDE";
        canvas.drawText(texte, xText, yText, p);
    }

    /*@Override
    public boolean onTouchEvent (MotionEvent event){
        xText = event.getX();
        yText = event.getY();
        invalidate(); // Force redraw
        return true; // Return true to indicate the event was handled
    }*/

}
