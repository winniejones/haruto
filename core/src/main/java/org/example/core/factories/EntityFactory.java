package org.example.core.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import org.example.core.components.*;

import static org.example.core.components.StateComponent.ACTION_STANDING;
import static org.example.core.factories.AnimationFactory.*;
import static org.example.core.utils.Direction.*;

public class EntityFactory {
    public static Entity createCharacter(float x, float y) {
        Entity entity = new Entity();

        PositionComponent position = new PositionComponent();
        position.pos.set(x, y);
        position.zOrder = (byte) 100;
        entity.add(position);

        CharacterComponent character = new CharacterComponent();
        character.walkSpeed = 10f;
        entity.add(character);

        AnimationComponent animationComponent = new AnimationComponent();
        animationComponent.animations.put(UP, getItachiUp(0.2f));
        animationComponent.animations.put(DOWN, getItachiDown(0.2f));
        animationComponent.animations.put(LEFT, getItachiLeft(0.2f));
        animationComponent.animations.put(RIGHT, getItachiRight(0.2f));
        entity.add(animationComponent);

        StateComponent state = new StateComponent();
        state.setDirection(DOWN);
        state.setAction(ACTION_STANDING);
        entity.add(state);

        TextureComponent texture = new TextureComponent();
        texture.texture = createTexture(1, 1, Color.ROYAL);
        texture.region = animationComponent.animations.get(state.getDirection()).getKeyFrame(state.time);
        entity.add(texture);

        PhysicsComponent physics = new PhysicsComponent();
        physics.body = BodyFactory.createPlayerBody(x, y, entity);
        entity.add(physics);

        entity.add(new ControllableComponent());
        return entity;
    }

    public static Entity createPlayer(float x, float y) {
        Entity character = createCharacter(x, y);

        CameraFocusComponent camFocus = new CameraFocusComponent();
        camFocus.zoomTarget = 1.0f;
        character.add(camFocus);

        character.add(new ControlFocusComponent());
        return character;
    }

    private static Texture createTexture(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, width, height);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
}
