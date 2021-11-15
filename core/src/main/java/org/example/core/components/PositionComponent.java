package org.example.core.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent implements Component {
    /* position in world x, y */
    public final Vector2 pos = new Vector2();

    /* Orientation in radians */
    public float rotation = 0.0f;

    /* render order */
    public byte zOrder;

    public void setPos(float nextX, float nextY){
        pos.x = Math.round(nextX);
        pos.y = Math.round(nextY);
    }

    public void setPos(Vector2 nextVec) {
        pos.set(nextVec);
    }
}
