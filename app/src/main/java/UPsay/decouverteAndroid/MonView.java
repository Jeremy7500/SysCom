package UPsay.decouverteAndroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MonView extends View {
    float xText, yText;
    float tailleTexte = 0;



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

    @Override
    public boolean onTouchEvent (MotionEvent event){
        xText = event.getX();
        yText = event.getY();
        invalidate(); // Force redraw
        return true; // Return true to indicate the event was handled
    }
}
