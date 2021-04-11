package org.example.core.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import org.example.core.components.CharacterComponent;
import org.example.core.components.ControllableComponent;
import org.example.core.components.PositionComponent;
import org.example.core.components.StateComponent;
import org.example.core.utils.Direction;
import org.example.core.utils.Mappers;

public class CharacterControlSystem extends IteratingSystem {

    public CharacterControlSystem() {
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
        float walkSpeed = character.walkSpeed * delta;

//        LogHelper.printObjectFields(control);

        if (control.up) {
            state.setDirection(Direction.UP);
            pos.setPos(pos.pos.x, pos.pos.y + walkSpeed);
//            physicsComp.body.applyForceToCenter(MyMath.vector(physicsComp.body.getAngle(), walkSpeed), true);
        } else if (control.down) {
            state.setDirection(Direction.DOWN);
            pos.setPos(pos.pos.x, pos.pos.y - walkSpeed);
        } else if (control.left) {
            state.setDirection(Direction.LEFT);
            pos.setPos(pos.pos.x - walkSpeed, pos.pos.y);
        } else if (control.right) {
            state.setDirection(Direction.RIGHT);
            pos.setPos(pos.pos.x + walkSpeed, pos.pos.y);
        }
    }
}
