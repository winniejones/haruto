package org.example.core.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class CameraManager {
    private static final String TAG = CameraManager.class.getSimpleName();
    private static final float MAXIMUM_DISTANCE_FROM_SHAKE = 12;
    private static final int VIEWPORT_WIDTH_IN_METERS = 38;
    private static final int Z = 0;

    private OrthographicCamera camera;
    private Vector3 position;

    public CameraManager() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        this.position = new Vector3(17, 40, 0);
        camera = new OrthographicCamera();
        this.camera.setToOrtho(false, VIEWPORT_WIDTH_IN_METERS,
                VIEWPORT_WIDTH_IN_METERS * (height / width));
    }

    public void resize(int width, int height) {
        camera.viewportWidth = VIEWPORT_WIDTH_IN_METERS;
        camera.viewportHeight = VIEWPORT_WIDTH_IN_METERS * height / (float) width;
        camera.update();
    }

    public void render(float delta) {
        camera.position.set(position);
        // camera.position.lerp(position, .2f); use this to get a smooth return

        camera.update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setPosition(float x, float y) {
        this.position = new Vector3(x, y, 0);
    }

}
