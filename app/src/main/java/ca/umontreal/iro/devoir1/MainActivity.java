package ca.umontreal.iro.devoir1;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

/**
 * Temps de réaction
 *
 *
 * La classe MainActivity fournit des méthodes pour la main activity
 * qui affiche un champ de texte où le numéro de l'essai est indiqué 
 * et un immense bouton occupant l'espace restant.
 *
 */
public class MainActivity extends AppCompatActivity {
    private TextView attempt;
    private Chronometer chrono;
    private Button button;
    private AlertDialog alertDialog;

    private int attempts, maxAttempts = 5;
    private long elapsed;
    private boolean waiting;
    private Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        attempt = findViewById(R.id.attempt);
        chrono = findViewById(R.id.chronometer);
        button = findViewById(R.id.button);

        alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(R.string.dialog_title);
        alertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE,
                getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        reset();
                        dialog.dismiss();
                    }
                }
        );
    }

    public void onButtonClick(View v) {
        if (attempts == 0) {
            attempt.setVisibility(View.VISIBLE);
            chrono.setVisibility(View.VISIBLE);

            newAttempt();
            return;
        }

        if (waiting) {
            setButtonEarly();
        } else {
            setButtonGood();
        }
    }

    // Si les 5 essais sont complétés avec succès, affichage d'un popup
    // Sinon nouvel essai et retour à la phase d'attente
    private void newAttempt() {
        if (++attempts > maxAttempts) {
            double average = elapsed / (maxAttempts * 1000.);
            alertDialog.setMessage(getString(R.string.dialog_message, average));
            alertDialog.show();
            return;
        }

        attempt.setText(getString(R.string.attempt, attempts, maxAttempts));
        setButtonWait();
    }

    // Phase d'attente
    private void setButtonWait() {
        button.setText(getString(R.string.button_wait));
        button.getBackground().clearColorFilter();
        button.setClickable(true);

        waiting = true;
        chrono.reset();
        chrono.stop();

        button.postDelayed(new Runnable() {
            @Override
            public void run() {
                setButtonNow();
            }
        }, rand.nextInt(7001) + 3000);
    }

    // L'usager clique avant que le bouton soit jaune,
    // le bouton devient rouge et un message d'erreur s'affiche
    private void setButtonEarly() {
        button.setText(getString(R.string.button_early));
        button.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        button.setClickable(false);

        button.getHandler().removeCallbacksAndMessages(null);
        button.postDelayed(new Runnable() {
            @Override
            public void run() {
                setButtonWait();
            }
        }, 1500);
    }

    // Fin de la période d'attente, le bouton devient jaune
    private void setButtonNow() {
        button.setText(getString(R.string.button_now));
        button.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
        //button.setClickable(true);

        waiting = false;
        chrono.reset();
        chrono.start();
    }

    // L'usager clique lorsque le bouton est jaune,
    // le bouton devient vert et un message de succès s'affiche
    private void setButtonGood() {
        button.setText(getString(R.string.button_good));
        button.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
        button.setClickable(false);

        chrono.stop();
        elapsed += chrono.getElapsedTime();

        button.postDelayed(new Runnable() {
            @Override
            public void run() {
                newAttempt();
            }
        }, 1500);
    }

    // Retour à la phase de repos
    private void reset() {
        attempts = 0;
        elapsed = 0;

        attempt.setVisibility(View.GONE);
        chrono.setVisibility(View.GONE);

        button.setText(R.string.button_start);
        button.getBackground().clearColorFilter();
        button.setClickable(true);
    }
}
