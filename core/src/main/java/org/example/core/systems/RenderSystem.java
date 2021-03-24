package org.example.core.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import org.example.core.components.PositionComponent;
import org.example.core.components.RenderComponent;
import org.example.core.components.ZComparator;

import java.util.Comparator;

public class RenderSystem extends SortedIteratingSystem {
    private static final int VIEWPORT_WIDTH_IN_METERS = 38;
    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<RenderComponent> rm = ComponentMapper.getFor(RenderComponent.class);
    private SpriteBatch spriteBatch;
    private Array<Entity> renderQueue; // an array used to allow sorting of images allowing us to draw images on top of each other
    private Comparator<Entity> comparator; // a comparator to sort images based on the z position of the transfromComponent
    private OrthographicCamera cam; // a reference to our camera

    public RenderSystem(SpriteBatch spriteBatch) {
        super(Family.all(PositionComponent.class, RenderComponent.class).get(), new ZComparator());
        // create the array for sorting entities
        renderQueue = new Array<>();
        this.spriteBatch = spriteBatch;
        Vector3 position = new Vector3(17, 40, 0);
        // set up the camera to match our screen size
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, VIEWPORT_WIDTH_IN_METERS,
                VIEWPORT_WIDTH_IN_METERS * (height / width));
        cam.position.set(position);
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        renderQueue.sort(comparator);
        // update camera and sprite batch
        cam.update();
        spriteBatch.setProjectionMatrix(cam.combined);
        spriteBatch.enableBlending();
        spriteBatch.begin();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    public OrthographicCamera getCamera() {
        return cam;
    }
}
