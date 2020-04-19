package com.example.breakoutgame;

import android.graphics.RectF;

import java.util.Random;

public class Ball {
    //This will be used for the ball/collision detection/drawing the ball
    private RectF ball;

    //Dimensions of the ball
    private int ballWidth = 10;
    private int ballHeight = 10;

    //Represents the horizontal and vertical speed of the ball
    //Note: These can be negative in value as well to indicate the direction.
    //Ex: -x will travel left, +x will travel right, -y will travel up, and +y will travel down
    private float xVelocity;
    private float yVelocity;

    /**
     * This constructor is called when the ball is created/instantiated
     */
    public Ball() {
        //Initialize the x and y velocity
        //These x and y values set means that it will be traveling right, and up the screen
        xVelocity = 200;
        yVelocity = -400;

        ball = new RectF();
    }

    /**
     * This method is used to pass the created ball in any other classes needed.
     */
    public RectF getBall() {
        return ball; //Return a reference to the ball object
    }

    /**
     * This method recieves the time that the previous frame took
     * @param fps
     */
    public void update(long fps) {
        //Divide x and y velocity to fps to get the new left and top position
        //Dividing if by fps makes sure it is consistent and smooth based on frame rate
        ball.left = ball.left + (xVelocity / fps);
        ball.top = ball.top + (yVelocity / fps);

        //Using the results above, re-initialize right and bottom as well
        //based on the new left and top position
        ball.right = ball.left + ballWidth;
        ball.bottom = ball.top - ballHeight;
    }

    /**
     * This method is used to reverse the y direction of the ball
     * if it collides with the walls or bricks
     */
    public void reverseYVelocity() {
        yVelocity = -yVelocity;
    }

    /**
     * This method is used to reverse the x direction of the ball
     * if it collides with the walls or bricks
     */
    public void reverseXVelocity() {
        xVelocity = -xVelocity;
    }

    /**
     * This method is used to randomize the left and right direction of
     * the ball when it hits the paddle
     */
    public void setRandomXVelocity() {
        Random random = new Random();
        int num = random.nextInt(2);
        if (num == 0) {
            reverseXVelocity();
        }
    }

    /**
     * This method is used for when the ball gets stuck
     * @param y
     */
    public void clearObstacleY(float y) {
        ball.bottom = y;
        ball.top = y - ballHeight;
    }

    /**
     * This method is used for when the ball gets stuck
     * @param x
     */
    public void clearObstacleX(float x) {
        ball.left = x;
        ball.right = x + ballWidth;
    }

    /**
     * This method is used to reset the position of the ball
     * @param x screen resolution
     * @param y screen resolution
     */
    public void reset(int x, int y) {
        //Place the ball at the bottom center of the screen
        ball.left = x / 2;
        ball.top = y - 25;

        //Place the ball bottom right of the screen
        ball.right = x / 2 + ballWidth;
        ball.bottom = y - 20 - ballHeight;
    }
}
