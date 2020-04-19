package com.example.breakoutgame;

import android.graphics.RectF;

public class Brick {
    //Just like the paddle and ball classes, this will be used for the
    //brick/collision detection/drawing the brick
    private RectF brick;

    //A boolean variable to indicate if the brick is visible on screen
    private boolean isPresent;

    /**
     * This constructor is called when the brick is created/instantiated
     */
    public Brick(int row, int column, int width, int height) {
        //Set the brick's presence to true because the constructor creates the bricks
        isPresent = true;

        //Set the gap 1px between the next brick that gets created
        int padding = 1;

        //Initialize the brick
        float leftStartPosition = column * width + padding;
        float rightStartPosition = column * width + width - padding;
        float topStartPosition = row * height + padding;
        float bottomStartPosition = row * height + height - padding;

        brick = new RectF(leftStartPosition, topStartPosition, rightStartPosition, bottomStartPosition);
    }

    /**
     * This method is used to pass the created brick in any other classes needed
     */
    public RectF getBrick() {
        return this.brick; //Return a reference to the brick object
    }

    /**
     * This method is used to set a brick's visibility to false
     * Therefore, removing it.
     */
    public void removeBrick() {
        isPresent = false;
    }

    /**
     * This method will report the brick's state whether it's present or not.
     * This is used to decide which bricks to draw, to calculate, and apply collision
     */
    public boolean getBrickVisibility() {
        return isPresent;
    }

    /*
        Note: Bricks won't need an update method, because they're
              just a static object with a location. They don't move or anything.
              Once hit by the ball, the bricks get destroyed.
     */
}
