package ca.umontreal.iro.devoir1;

import android.content.Context;
import android.os.SystemClock;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

import java.util.Locale;

/**
 * Dû à la faible fréquence de rafraîchissement de la valeur courante de temps
 * et du texte affiché, qu'il n'est pas possible de modifier ces valeurs à
 * travers une sous-classe et que le niveau d'encapsulation ne permet pas
 * d'obtenir facilement le lapse de temps, nous sommes dans l'obligation de
 * créer une version minimale de la classe qui convient à nos besoins.
 *
 * @see android.widget.Chronometer
 */
public class Chronometer extends AppCompatTextView {
    private long mBase;
    private long mNow;
    private boolean mVisible;
    private boolean mStarted;
    private boolean mRunning;

    public Chronometer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setBase(long base) {
        mBase = base;
        updateText(SystemClock.elapsedRealtime());
    }

    public void start() {
        mStarted = true;
        updateRunning();
    }

    public void stop() {
        mStarted = false;
        updateRunning();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVisible = false;
        updateRunning();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == VISIBLE;
        updateRunning();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        updateRunning();
    }

    private synchronized void updateText(long now) {
        mNow = now;
        double seconds = (now - mBase) / 1000.;
        String text = String.format(Locale.getDefault(), "%.3f", seconds);

        setText(text);
    }

    private void updateRunning() {
        boolean running = mVisible && mStarted && isShown();
        if (running != mRunning) {
            if (running) {
                updateText(SystemClock.elapsedRealtime());
                postDelayed(mTickRunnable, 50);
            } else {
                removeCallbacks(mTickRunnable);
            }
            mRunning = running;
        }
    }

    private final Runnable mTickRunnable = new Runnable() {
        @Override
        public void run() {
            if (mRunning) {
                updateText(SystemClock.elapsedRealtime());
                postDelayed(mTickRunnable, 50);
            }
        }
    };

    public void reset() {
        setBase(SystemClock.elapsedRealtime());
    }

    public long getElapsedTime() {
        return mNow - mBase;
    }
}
