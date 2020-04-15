package com.example.breakoutgame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class MainActivity extends AppCompatActivity {
    //Field to access the GameEngine class
    GameEngine gameEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Create a display object and access the user's screen details
        Display display = getWindowManager().getDefaultDisplay();

        //Get the horizontal and vertical size resolution of the user's device
        Point size = new Point();
        display.getSize(size);

        //Initialize the gameEngine field and set it as the gameView
        gameEngine = new GameEngine(this, size.x, size.y); //size.x and size.y is passed from the device's resolution
        setContentView(gameEngine);
    }

    /**
     * This method executes when the player starts the game
     */
    @Override
    protected void onResume() {
        super.onResume(); //Call the default version of onResume

        //Tells the gameView's resume method to execute
        gameEngine.resume();
    }

    /*
        Note: onResume and onPause methods are already part of the Activity class
              https://developer.android.com/reference/android/app/Activity
    */

    /**
     * This method executed when the player quits the game
     */
    @Override
    protected void onPause() {
        super.onPause(); //Call the default version of onPause

        //Tells the gameView's pause method to execute
        gameEngine.pause();
    }
}
