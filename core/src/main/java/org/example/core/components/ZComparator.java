package org.example.core.components;

import com.badlogic.ashley.core.Entity;
import org.example.core.utils.Mappers;

import java.util.Comparator;

public class ZComparator implements Comparator<Entity> {
    @Override
    public int compare(Entity entityA, Entity entityB) {
        return (int) Math.signum(Mappers.position.get(entityB).zOrder
                    - Mappers.position.get(entityA).zOrder);
    }
}
