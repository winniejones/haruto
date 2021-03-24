package org.example.core.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.List;

public class RenderComponent implements Component {
    private List<Sprite> sprites;

    public RenderComponent(Sprite sprite) {
        this.sprites = new ArrayList<>();
        this.sprites.add(sprite);
    }

    public RenderComponent() {
        this.sprites = new ArrayList<>();
    }

    public List<Sprite> getSprites() {
        return sprites;
    }

    public void clear() {
        this.sprites.clear();
    }

    public void add(Sprite sprite){
        this.sprites.add(sprite);
    }
}
