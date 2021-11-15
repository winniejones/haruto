package org.example.core.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Dimensions {
    public static int SCREEN_WIDTH = 1024;

    public static int SCREEN_HEIGHT = 768;

    /* One meter in pixels */
    public static final float PPM = 16.0f;

    /* Meters to pixels */
    public static final float PIXELS_TO_METRES = 1.0f /PPM;

    public static final float ENTITY_SIZE = 1.0f/PPM;

    public static final float BOX_2D_OFFSET = 1/2f;

    public static final float toMeters(int px){
        return (float) px / PPM;
    }

    public static final float toMeters(float px){
        return px / PPM;
    }

    public static final int toPixels(float meters){
        return (int) (meters * PPM);
    }

    public static final Vector2 calculateGlobalPositionInPixelsToMetersRelativeToCenter(float x, float y){
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        float relativeX = x - width / 2f;
        float relativeY = y - height / 2f;

        return new Vector2(toMeters(relativeX), toMeters(relativeY));
    }
}
