package org.example.core.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import org.example.core.screens.MainGameScreen;
import org.example.core.utils.Dimensions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ObstacleSystem extends EntitySystem {
    public static final String COLLISION_LAYER = "collision";

    private TiledMap map;
    private List<Rectangle> obstacles;

    public ObstacleSystem(TiledMap map) {
        this.map = map;
        this.obstacles = new ArrayList<>();
    }

    @Override
    public void addedToEngine(Engine engine) {
        populateObstacles();
    }

    private void populateObstacles() {
        MapLayer layer = map.getLayers().get(COLLISION_LAYER);
        MapObjects objects = layer.getObjects();
        Iterator<MapObject> objectIt = objects.iterator();

        while (objectIt.hasNext()) {
            RectangleMapObject object;
            MapObject mapObject = objectIt.next();
            if(mapObject instanceof RectangleMapObject) {
                object = (RectangleMapObject) mapObject;
            } else {
                continue;
            }
            Rectangle rectangle = object.getRectangle();

            float width = rectangle.getWidth() / Dimensions.PPM;
            float height = rectangle.getHeight() / Dimensions.PPM;

            float x = (rectangle.getX() / Dimensions.PPM);
            float y = (rectangle.getY() / Dimensions.PPM);

            obstacles.add(new Rectangle(x, y, width, height));

            float x_offset = x + width / 2;
            float y_offset = y + height / 2;

            createObstacleBody(x_offset, y_offset, width, height);
        }
    }

    private Body createObstacleBody(float x, float y, float width, float height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);
        Body body = MainGameScreen.world.createBody(bodyDef);
        PolygonShape square = new PolygonShape();
        square.setAsBox(width / 2, height / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 2.0f;
        fixtureDef.restitution = 0.1f;
        fixtureDef.shape = square;

        body.createFixture(fixtureDef);
        square.dispose();

        return body;
    }
}
