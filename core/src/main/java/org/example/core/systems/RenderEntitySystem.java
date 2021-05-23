package org.example.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.example.core.components.CameraFocusComponent;
import org.example.core.components.PositionComponent;
import org.example.core.components.TextureComponent;
import org.example.core.components.ZComparator;
import org.example.core.screens.MainGameScreen;
import org.example.core.utils.Mappers;

import java.util.Comparator;

public class RenderEntitySystem extends SortedIteratingSystem {
    private SpriteBatch spriteBatch;
    private Array<Entity> renderQueue; // an array used to allow sorting of images allowing us to draw images on top of each other
    private Comparator<Entity> comparator; // a comparator to sort images based on the z position of the transfromComponent
    private OrthographicCamera cam; // a reference to our camera

    public RenderEntitySystem() {
        super(Family.all(PositionComponent.class, TextureComponent.class, CameraFocusComponent.class).get(), new ZComparator());
        // create the array for sorting entities
        renderQueue = new Array<>();
        cam = MainGameScreen.cam;
        spriteBatch = MainGameScreen.batch;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        renderQueue.sort(comparator);
        // update camera and sprite batch
        cam.update();
//        spriteBatch.setProjectionMatrix(cam.combined);
        spriteBatch.enableBlending();
        spriteBatch.begin();

        for (Entity entity : renderQueue) {
            renderEntity(entity);
        }
        spriteBatch.end();
//        spriteBatch.disableBlending();
        renderQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    private void renderEntity(Entity entity) {
        TextureComponent textureComponent = Mappers.texture.get(entity);
        PositionComponent positionComponent = Mappers.position.get(entity);
        Vector2 pos = new Vector2(positionComponent.pos.x, positionComponent.pos.y);

        if (textureComponent.region != null) {
            float width = textureComponent.region.getRegionWidth();
            float height = textureComponent.region.getRegionHeight();
            float originX = width * 0.5f; //center
            float originY = height * 0.5f; //center

            spriteBatch.draw(
                    textureComponent.region, (pos.x - originX), (pos.y - originY),
                    originX, originY+0.4f,
                    width, height,
                    (0.65f/16f), (0.65f/16f),
                    0);
        } else {
            float width = textureComponent.texture.getWidth();
            float height = textureComponent.texture.getHeight();
            float originX = width * 0.5f; //center
            float originY = height * 0.5f; //center
            spriteBatch.draw(
                    textureComponent.texture, (pos.x - originX), (pos.y - originY),
                    originX, originY,
                    width, height,
                    0.75f, 0.75f,
                    0,
                    0, 0, (int) width, (int) height, false, false);
        }
    }
}
