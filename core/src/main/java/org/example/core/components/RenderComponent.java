package org.example.core.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.List;

public class RenderComponent implements Component {
    public List<Sprite> sprites = new ArrayList<>();
}
