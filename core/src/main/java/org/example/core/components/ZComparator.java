package org.example.core.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

import java.util.Comparator;

public class ZComparator implements Comparator<Entity> {
    private static final int DIFF_MULTIPLIER = 10;

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<RenderComponent> rm = ComponentMapper.getFor(RenderComponent.class);

    @Override
    public int compare(Entity e1, Entity e2) {
        int priorityDiff = getPriorityDifference(e1, e2);

        if (priorityDiff == 0)
            return getVerticalPositionDifferente(e1, e2);

        return priorityDiff;
    }

    private int getVerticalPositionDifferente(Entity e1, Entity e2) {
        PositionComponent p1 = pm.get(e1);
        PositionComponent p2 = pm.get(e2);

        return (int) ((p2.y - p1.y) * DIFF_MULTIPLIER);
    }

    private int getPriorityDifference(Entity e1, Entity e2) {
        RenderComponent r1 = rm.get(e1);
        RenderComponent r2 = rm.get(e2);

        //return r2.getPriority().getValue() - r1.getPriority().getValue();
        return 0;
    }
}
