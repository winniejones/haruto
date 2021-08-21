package org.example.core.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import org.example.core.components.*;
import org.example.core.utils.Direction;
import org.example.core.utils.Mappers;

public class CharacterMovementSystem extends IteratingSystem {

    public CharacterMovementSystem() {
        super(Family.all(ControllableComponent.class, PositionComponent.class, CharacterComponent.class).get());
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        controlCharacter(entity, deltaTime);
    }

    private void controlCharacter(Entity entity, float delta) {
        ControllableComponent control = Mappers.controllable.get(entity);
        PositionComponent pos = Mappers.position.get(entity);
        CharacterComponent character = Mappers.character.get(entity);
        StateComponent state = Mappers.state.get(entity);
        PhysicsComponent physicsComp = Mappers.physics.get(entity);
        float walkSpeed = character.walkSpeed;
        float xa = 0, ya = 0;
//        LogHelper.printObjectFields(control);

//        moveStyleLinear(delta, control, state, physicsComp, walkSpeed);
        moveStyleold(delta, control, pos, state, physicsComp, walkSpeed);
    }

    private void moveStyleLinear(float delta, ControllableComponent control, StateComponent state, PhysicsComponent physicsComp, float walkSpeed) {
        int toggleX = 0;
        int toggleY = 0;
        if (control.up) {
            state.setDirection(Direction.UP);
            toggleY = 1;
        } else if (control.down) {
            state.setDirection(Direction.DOWN);
            toggleY = -1;
        } else if (control.left) {
            state.setDirection(Direction.LEFT);
            toggleX = -1;
        } else if (control.right) {
            state.setDirection(Direction.RIGHT);
            toggleX = 1;
        }
        physicsComp.body.setLinearVelocity(walkSpeed * toggleX * (delta + 1), walkSpeed * toggleY * (delta + 1));
    }

    private void moveStyleold(float delta, ControllableComponent control, PositionComponent pos, StateComponent state, PhysicsComponent physicsComp, float walkSpeed) {

        if (control.up) {
            state.setDirection(Direction.UP);
            pos.setPos(pos.pos.x, pos.pos.y + (walkSpeed* delta));
        } else if (control.down) {
            state.setDirection(Direction.DOWN);
            pos.setPos(pos.pos.x, pos.pos.y - (walkSpeed* delta));
        } else if (control.left) {
            state.setDirection(Direction.LEFT);
            pos.setPos(pos.pos.x - (walkSpeed* delta), pos.pos.y);
        } else if (control.right) {
            state.setDirection(Direction.RIGHT);
            pos.setPos(pos.pos.x + (walkSpeed* delta), pos.pos.y);
        }
    }
}
