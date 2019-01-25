package com.example.devoir1;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView heading;
    Button bigButton;

    Double time = 0.0;
    int attempt = 0;

    Handler timer;

    boolean isTimeToClick = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize the app
        heading = findViewById(R.id.heading);
        setHeading();

        timer = new Handler();

        bigButton = findViewById(R.id.bigButton);
        bigButton.setOnClickListener(bigButton_listener);
        bigButton.setText("CLIQUER SUR LE BOUTON POUR COMMENCER");
        bigButton.setBackgroundColor(getResources().getColor(R.color.grey));
    }

    //Update the heading
    public void setHeading(){
        heading.setText("Essai "+attempt+" de 5\n"+String.format( "%.3f", time));
    }

    //Set when the button is hit at the right moment
    public void setGreenButton(){
        bigButton.setBackgroundColor(getResources().getColor(R.color.green));
        bigButton.setText("BON TRAVAIL!");
        setHeading();

        bigButton.setClickable(false);
    }

    //Set when the button is hit not at the right moment
    public void setRedButton(){
        bigButton.setBackgroundColor(getResources().getColor(R.color.red));
        bigButton.setText("IL FAUT ATTENDRE QUE LE BOUTON SOIT DEVENU JAUNE AVANT DE CLIQUER!");

        bigButton.setClickable(false);
    }

    //Set when we have to click the button
    public void setYellowButton(){
        bigButton.setBackgroundColor(getResources().getColor(R.color.yellow));
        bigButton.setText("CLIQUER MAINTENANT!");

        //TODO: Afficher le temps (modifier la variable globale time et afficher avec setHeading)
        isTimeToClick = true;
    }

    //Set for the waiting time
    public void setWaitingButton(){
        bigButton.setBackgroundColor(getResources().getColor(R.color.grey));
        bigButton.setText("ATTENDRE QUE LE BOUTON DEVIENNE JAUNE");

        bigButton.setClickable(true);
        isTimeToClick = false;

        //TODO: Mettre un délai aléatoire
        setYellowButton();
    }

    //Update when we pass to the next try
    public void nextTry(){
        if(attempt<5){
            attempt++;
            time = 0.0;

            setHeading();
            setWaitingButton();
        } else {
            //TODO: Afficher pop-up avec les données
            //TODO: recommencer le jeu

        }
    }

    View.OnClickListener bigButton_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //First attempt
            if (attempt == 0) {
                nextTry();
                heading.setVisibility(View.VISIBLE);
            }

            //Next attempt
            else {
                if (isTimeToClick) {
                    setGreenButton();
                    //TODO: Mettre un délai
                    nextTry();
                } else {
                    setRedButton();
                    //TODO: Mettre un délai
                    setWaitingButton();
                }
            }
        }
    };



}
