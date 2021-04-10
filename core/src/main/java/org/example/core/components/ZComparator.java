package org.example.core.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import org.example.core.utils.Mappers;

import java.util.Comparator;

public class ZComparator implements Comparator<Entity> {
    private static final int DIFF_MULTIPLIER = 10;

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<RenderComponent> rm = ComponentMapper.getFor(RenderComponent.class);

    @Override
    public int compare(Entity entityA, Entity entityB) {
        return (int) Math.signum(Mappers.position.get(entityB).zOrder
                    - Mappers.position.get(entityA).zOrder);
    }
}
