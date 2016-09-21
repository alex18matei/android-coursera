package course.labs.graphicslab;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class BubbleView extends View {

    // These variables are for testing purposes, do not modify
    private final static int RANDOM = 0;
    private final static int SINGLE = 1;
    private final static int STILL = 2;
    private static int speedMode = RANDOM;

    private static final int BITMAP_SIZE = 64;
    private static final int REFRESH_RATE = 40;
    private final Paint mPainter = new Paint();
    private ScheduledFuture<?> mMoverFuture;
    private int mScaledBitmapWidth;
    private Bitmap mScaledBitmap;
    // The Main view
    private RelativeLayout mFrame;

    // location, speed and direction of the bubble
    private float mXPos, mYPos, mDx, mDy, mRadius, mRadiusSquared;
    private long mRotate, mDRotate;

    BubbleView(Context context, float x, float y, RelativeLayout layout) {
        super(context);

        mFrame = layout;

        // Create a new random number generator to
        // randomize size, rotation, speed and direction
        Random r = new Random();

        // Creates the bubble bitmap for this BubbleView
        createScaledBitmap(r);

        // Radius of the Bitmap
        mRadius = mScaledBitmapWidth / 2;
        mRadiusSquared = mRadius * mRadius;

        // Adjust position to center the bubble under user's finger
        mXPos = x - mRadius;
        mYPos = y - mRadius;

        // Set the BubbleView's speed and direction
        setSpeedAndDirection(r);

        // Set the BubbleView's rotation
        setRotation(r);

        mPainter.setAntiAlias(true);

    }

    private void setRotation(Random r) {

        if (speedMode == RANDOM) {

            // TODO - set rotation in range [1..3]
            mDRotate = 0;


        } else {
            mDRotate = 0;

        }
    }

    private void setSpeedAndDirection(Random r) {

        // Used by test cases
        switch (speedMode) {

            case SINGLE:

                mDx = 20;
                mDy = 20;
                break;

            case STILL:

                // No speed
                mDx = 0;
                mDy = 0;
                break;

            default:

                // TODO - Set movement direction and speed
                // Limit movement speed in the x and y
                // direction to [-3..3] pixels per movement.


        }
    }

    private void createScaledBitmap(Random r) {

        if (speedMode != RANDOM) {
            mScaledBitmapWidth = BITMAP_SIZE * 3;

        } else {
            //TODO - set scaled bitmap size in range [1..3] * BITMAP_SIZE
            mScaledBitmapWidth = 0;

        }

        // TODO - create the scaled bitmap using size set above
        mScaledBitmap = null;
    }

    // Start moving the BubbleView & updating the display
    private void start() {

        // Creates a WorkerThread
        ScheduledExecutorService executor = Executors
                .newScheduledThreadPool(1);

        // Execute the run() in Worker Thread every REFRESH_RATE
        // milliseconds
        // Save reference to this job in mMoverFuture
        mMoverFuture = executor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {

                // TODO - implement movement logic.
                // Each time this method is run the BubbleView should
                // move one step. If the BubbleView exits the display,
                // stop the BubbleView's Worker Thread.
                // Otherwise, request that the BubbleView be redrawn.


            }
        }, 0, REFRESH_RATE, TimeUnit.MILLISECONDS);
    }

    // Returns true if the BubbleView intersects position (x,y)
    private synchronized boolean intersects(float x, float y) {

        // TODO - Return true if the BubbleView intersects position (x,y)


        return false;
    }

    // Cancel the Bubble's movement
    // Remove Bubble from mFrame
    // Play pop sound if the BubbleView was popped

    private void stop(final boolean wasPopped) {

        if (null != mMoverFuture && !mMoverFuture.isDone()) {
            mMoverFuture.cancel(true);
        }

        // This work will be performed on the UI Thread
        mFrame.post(new Runnable() {
            @Override
            public void run() {

                // TODO - Remove the BubbleView from mFrame


                // TODO - If the bubble was popped by user,
                // play the popping sound
                if (wasPopped) {


                }
            }
        });
    }

    // Change the Bubble's speed and direction
    private synchronized void deflect(float velocityX, float velocityY) {

        //TODO - set mDx and mDy to be the new velocities divided by the REFRESH_RATE


    }

    // Draw the Bubble at its current location
    @Override
    protected synchronized void onDraw(Canvas canvas) {

        // TODO - save the canvas


        // TODO - increase the rotation of the original image by mDRotate


        // TODO Rotate the canvas by current rotation
        // Hint - Rotate around the bubble's center, not its position


        // TODO - draw the bitmap at its new location


        // TODO - restore the canvas


    }

    // Returns true if the BubbleView is still on the screen after the move
    // operation
    private synchronized boolean moveWhileOnScreen() {

        // TODO - Move the BubbleView


        return false;
    }

    // Return true if the BubbleView is off the screen after the move
    // operation
    private boolean isOutOfView() {

        // TODO - Return true if the BubbleView is off the screen after
        // the move operation


        return false;
    }
}