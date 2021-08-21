package org.example.core.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import org.example.core.screens.MainGameScreen;
import org.example.core.utils.Mappers;

import java.awt.*;

import static org.example.core.utils.Dimensions.ENTITY_SIZE;

public class BodyFactory {

    public static Rectangle createPlayerBody(float x, float y, Entity entity) {
        TextureRegion txregion = Mappers.texture.get(entity).region;
        return createRectangle(x, y, txregion.getRegionWidth() * ENTITY_SIZE / 3, txregion.getRegionHeight() * ENTITY_SIZE / 3);
    }

    public static Body createCircle(float x, float y, float radius) {
        Body body;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);
        body = MainGameScreen.world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 2.0f;
        fixtureDef.restitution = 0.1f; // Make it bounce a little bit

        // Create our fixture and attach it to the body
        body.createFixture(fixtureDef);

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        circle.dispose();

        return body;
    }

    public static Body createRect(float x, float y, float width, float height, BodyDef.BodyType bodyType) {
        Body body;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(x, y);
        body = MainGameScreen.world.createBody(bodyDef);

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width / 2, height / 2);
        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = poly;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit

        // Create our fixture and attach it to the body
        body.createFixture(fixtureDef);

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        poly.dispose();
        return body;
    }

    public static Rectangle createRectangle(float x, float y, float width, float height) {
        return new Rectangle((int) x, (int) y, (int) width, (int) height);
    }
}
