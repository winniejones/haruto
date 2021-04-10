package org.example.core.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import org.example.core.components.*;

public class EntityFactory {
    public static Entity createCharacter(float x, float y) {
        Entity entity = new Entity();

        PositionComponent position = new PositionComponent();
        position.pos.set(x, y);
        position.zOrder = (byte)100;
        entity.add(position);

        CharacterComponent character = new CharacterComponent();
        character.walkSpeed = 2f;
        entity.add(character);

        TextureComponent texture = new TextureComponent();
        texture.texture = createTexture(1, 1, Color.ROYAL);
        entity.add(texture);

        entity.add(new ControllableComponent());
        return entity;
    }

    public static Entity createPlayer(float x, float y) {
        Entity character = createCharacter(x, y);

        CameraFocusComponent camFocus = new CameraFocusComponent();
        camFocus.zoomTarget = 0.5f;
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
