package org.example.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import org.example.core.components.CameraFocusComponent;
import org.example.core.components.PositionComponent;
import org.example.core.screens.MainGameScreen;
import org.example.core.utils.Mappers;

public class CameraSystem extends IteratingSystem {
    private final OrthographicCamera cam;
    private float zoomSpeed = 3;

    public CameraSystem() {
        super(Family.all(CameraFocusComponent.class, PositionComponent.class).get());
        this.cam = MainGameScreen.cam;
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        CameraFocusComponent cameraFocus = entity.getComponent(CameraFocusComponent.class);
        PositionComponent position = Mappers.position.get(entity);

        //set camera position to entity
        cam.position.x = position.pos.x;
        cam.position.y = position.pos.y;

//        animateZoom(delta, cameraFocus.zoomTarget);
        cam.update();
    }

    private void animateZoom(float delta, float zoomTarget) {
        //todo: pan / zoom speed, pan / zoom interpolation curve
        if (cam.zoom != zoomTarget) {
            //zoom in/out
            float scaleSpeed = zoomSpeed * delta;
            cam.zoom += (cam.zoom < zoomTarget) ? scaleSpeed : -scaleSpeed;

            //if zoom is close enough, just set it to target
            if (Math.abs(cam.zoom - zoomTarget) < 0.2) {
                cam.zoom = zoomTarget;
            }
        }
        cam.zoom = MathUtils.clamp(cam.zoom, 0.001f, 100000);
    }
}
