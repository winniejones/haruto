package org.example.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.World;
import org.example.core.components.PhysicsComponent;
import org.example.core.components.PositionComponent;
import org.example.core.utils.Mappers;

public class PhysicsSystem extends IteratingSystem {
    public static final float UPDATE_TIME_STEP = 1 / 45f;
    private World world;

    public PhysicsSystem(World world) {
        super(Family.all(PhysicsComponent.class, PositionComponent.class).get());

        this.world = world;
//        world.setContactListener(new PhysicsContactListener());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        world.step(UPDATE_TIME_STEP, 6, 2);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = Mappers.position.get(entity);
        PhysicsComponent physics = Mappers.physics.get(entity);

//        Vector2 vector = physics.body.getPosition();
//        position.setPos(vector);

//        physics.body.setTransform(position.pos.x, position.pos.y, physics.body.getAngle());

    }
}
