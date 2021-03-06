package com.example.breakoutgame;

import android.graphics.RectF;

public class Paddle {
    //This is an object that holds four coordinates. (Ex. For a rectangle or square)
    private RectF paddle;

    //This keeps track of the paddle length
    private float length;

    //This will be used to hold the horizontal position on the screen of the paddle
    private float x;

    //This keeps track of the paddle's speed, measured by pixels per second
    private float paddleSpeed;

    //Directions the paddle can move
    final int STOP = 0;
    final int LEFT = 1;
    final int RIGHT = 2;

    //This keeps track of which way the paddle is moving, which uses the fields above
    private int paddleMovement = STOP;

    /**
     * This is the constructor for the paddle. This is called when the key word "new" is used.
     * For example: Paddle p = new Paddle(parameters);
     */
    public Paddle(int screenX, int screenY) {
        //Specify the dimensions of the paddle (20px x 130px)
        float height = 20; //Note: The height value declaration is only needed in the constructor
        length = 130;

        //Start the paddle in the bottom center
        x = screenX / 2;
        float y = screenY - 20;
        ///////float y = screenY - (screenY / 12); (Dynamic to every screen, need to test later on before removing above)////////
        //Note: If 20 isn't subtracted from screenY, the paddle will start offscreen

        //Initialize the paddle using the height and length specified above
        paddle = new RectF(x, y, x + length, y + height);
        //Note: RectF(startPositionX, startPositionY, paddleDimensionX, paddleDimensionY)

        //Speed of the paddle movement
        paddleSpeed = screenX / 3; //It will take 3 seconds for the paddle to get across the screen
    }

    /**
     * This method is used to pass the created rectangle in any other classes needed.
     * This method will ensure that the GameEngine class has the most up-to-date position.
     */
    public RectF getPaddle() {
        return paddle; //Return the current state of the paddle object
    }

    /**
     * This method is used to change the movement direction of the paddle
     */
    public void setMovement(int direction) {
        paddleMovement = direction;
    }

    /**
     * This update method will be called from the GameEngine's update method.
     * It will determine if the paddle needs to move which will change the coordinates contained
     * in the variable rectangle if necessary
     * @param fps The most recent frame rate
     */
    public void update(long fps) {
        if (paddleMovement == LEFT) {
            x -= paddleSpeed / fps;
            //Subtract x to paddleSpeed and divide by fps,
            //so that the movement to the left is consistent and smooth based on frame rate
        }
        else if (paddleMovement == RIGHT) {
            x += paddleSpeed / fps;
            //Add x to paddleSpeed and divide by fps,
            //so that the movement to the right is consistent and smooth based on frame rate
        }

        //Changing the left and right rectangle object coordinates
        //will update the position of the paddle
        paddle.left = x;
        paddle.right = x + length;
    }
}
