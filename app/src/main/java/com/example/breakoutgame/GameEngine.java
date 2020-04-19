package com.example.breakoutgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameEngine extends SurfaceView implements Runnable {
    /*
        Note: SurfaceView class provides a dedicated drawing surface embedded inside of a view hierarchy.
              You can control the format of this surface and, its size if needed.
              The SurfaceView takes care of placing the surface at the correct location on the screen.
              https://developer.android.com/reference/android/view/SurfaceView
     */

    //This will be responsible for performing other threads of code that runs separately
    //from the user interface thread
    //https://developer.android.com/reference/java/lang/Thread
    private Thread gameThread = null;

    //SurfaceHolder is needed when we use paint and canvas in a Thread
    //This will help do the drawing on the screen within the Thread
    private SurfaceHolder holder;

    //This will keep track weather the game is playing or paused
    private volatile boolean playing;

    /*
        Note: Volatile keyword in Java is used to indicate that the variable
        will be accessed from both within and outside the thread
    */

    //This will only be used internally inside the Thread, and will
    //point out weather the game is running or not. By default, it's should be set to true
    //unless an event happens that starts the game.
    private boolean paused = true;

    //Declare a canvas and a paint object
    //This will actually allow to draw in the screen
    private Canvas canvas;
    private Paint paint;

    //This will contain the screen resolution that is passed from MainActivity.java class
    //This will be needed from time to time, through out this class
    private int screenX;
    private int screenY;

    //This keeps track of the screen's frame rate
    private long fps;

    //This helps calculate the frame rate per second (fps)
    private long timeThisFrame;

    //Declare the paddle
    Paddle paddle;

    //Declare the ball
    Ball ball;

    /**
     * This constructor is called when the object is first created
     * @param x comes from MainActivity class' onCreate method when it initialized gameEngine,
     * @param y comes from MainActivity class' onCreate method when it initialized gameEngine
     */
    public GameEngine(Context context, int x, int y) {
        //This calls the default constructor (SurfaceView) to setup the rest of the object
        super(context);

        //Initialize "SurfaceHolder holder" with a call to getHolder method
        holder = getHolder();

        //Initialize paint object
        paint = new Paint();

        //Initialize screenX and screenY using the x and y parameters passed from MainActivity
        screenX = x;
        screenY = y;

        //Initialize the paddle
        paddle = new Paddle(screenX, screenY);

        //Initialize the ball
        ball = new Ball();

        //Reset the position of the ball
        restart();
    }

    /**
     * This method runs when the Operating system calls the onPause() method from the MainActivity.java class
     */
    public void pause() {
        //This boolean is set to false to pause all the of the code inside of the run() method
        playing = false;

        try {
            //join() stops the Thread
            gameThread.join();
        }
        catch (InterruptedException e) {
            //Output an error just in case the operation fails so we can see it in the log console
            Log.e("Error:", "Joining Thread");
        }

        /*
            Note: try/catch block must be used because it's the way the Thread class is coded
         */
    }

    /**
     * This method runs when the Operating system calls the onResume() method from the MainActivity.java class
     */
    public void resume() {
        //This boolean is set to true to run all of the code inside of the run() method
        playing = true;

        //Initialize the game Thread
        gameThread = new Thread(this);

        /*
            Note: "this" is a reference to the current class. Because the current class
                  implements Runnable, the code above works. If Runnable wasn't implemented,
                  the code above won't work
         */

        //This starts the Thread. By starting the Thread, the run() method will begin to execute
        //on a regular basis
        gameThread.start();
    }

    /**
     * This method is executed when the Thread is started
     */
    @Override
    public void run() {
        //While the user is playing
        while (playing) {
            //Grab the current time in milliseconds in the startFrameTime field
            long startFrameTime = System.currentTimeMillis();

            //Update the frame as long as the game isn't paused
            if (!paused) {
                //Update the position of all of the game objects
                update();
            }

            //Draw the frame
            //Updates the drawing in every single frame
            draw();

            //Calculate the fps in this frame
            //The result is an answer on exactly how long it took to run, update, and draw
            //and it can be used to time animations, etc
            timeThisFrame = System.currentTimeMillis() - startFrameTime;

            //By checking weather timeThisFrame is greater than or equal to 1,
            //we're making sure that timeThisFrame isn't equal to 0, or else dividing it by zero would
            //crash the game.
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
        /*
        Side Note: If the user isn't playing or the game is paused the objects won't be updated.
                   All objects will stop moving, calculations will be stopped, but they will
                   still be drawn.
         */
    }

    private void update() {
        //Move the paddle by calling it's update method and passing the frame rate
        paddle.update(fps);

        //Move the ball by calling it's update method and passing the frame rate
        ball.update(fps);
    }

    /**
     * This method will be used to restart/reset the ball, score, etc
     */
    public void restart() {
        //Put the ball back to it's starting position
        ball.reset(screenX, screenY);
    }

    /**
     * This method draws the objects in the canvas
     */
    private void draw() {
        //If the drawing surface is valid
        if (holder.getSurface().isValid()) {
            //Prepare the canvas to be drawn upon
            canvas = holder.lockCanvas();

            //Draws the background color
            //Note: This is done in the beginning of every drawing process so it gets rid of
            //      everything that was there previously
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            //Choose the brush color for the drawing
            //Changes the drawing color
            paint.setColor(Color.argb(255, 255, 255, 255));

            //Draw everything to the screen\\
            //Draw the paddle
            canvas.drawRect(paddle.getRectangle(), paint);

            //Draw the ball
            canvas.drawRect(ball.getBall(), paint);

            //Show everything that's been drawn
            holder.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * This method is called whenever the player touches the screen.
     * Note: The SurfaceView class implements onTouchListener so this onTouchEvent can
     * be overridden be used to detect screen touches.
     * @param motionEvent
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            //This case occurs when the player touches the screen
            case MotionEvent.ACTION_DOWN:
                //Set the game's state to resume because the case statement means
                //"the player touched the screen, resume/start the game". This will
                //also execute the update() method, which is in an if statement in the
                //run() method
                paused = false;

                //Get the x point of the MotionEvent and see if it's greater
                //than half of the width of the screen. If the condition is true,
                //that means the player has touched the right hand side of the screen
                if (motionEvent.getX() > screenX / 2) {
                    //Move the paddle to the right
                    paddle.setMovement(paddle.RIGHT);
                }
                else {
                    //Move the paddle to the left
                    paddle.setMovement(paddle.LEFT);
                }
                break;

            //This case occurs when the player removes their finger off of the screen
            case MotionEvent.ACTION_UP:
                //Stop the paddle
                paddle.setMovement(paddle.STOP);
                break;
        }
        return true;
    }
}
