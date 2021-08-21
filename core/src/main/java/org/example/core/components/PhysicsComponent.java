package org.example.core.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

public class PhysicsComponent implements Component {
    public Body body;
    public int x;
    public int y;
    public int width;
    public int height;

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(int width, int height) {
        this.height = height;
        this.width = width;
    }

    public void setRect(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }
}
