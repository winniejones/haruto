package org.example.core.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import org.example.core.components.ControlFocusComponent;
import org.example.core.components.ControllableComponent;
import org.example.core.components.StateComponent;
import org.example.core.screens.MainGameScreen;
import org.example.core.utils.Mappers;

public class DesktopInputSystem extends EntitySystem implements InputProcessor {
    private boolean[] keys = new boolean[256];
    private ImmutableArray<Entity> players;

    @Override
    public void addedToEngine(Engine engine) {
        players = engine.getEntitiesFor(Family.all(ControlFocusComponent.class, ControllableComponent.class).get());
        MainGameScreen.getInputMultiplexer().addProcessor(this);
    }

    @Override
    public void update(float deltaTime) {
        ControllableComponent control = Mappers.controllable.get(players.first());
        control.up = keys[Keys.W] || keys[Keys.UP];
        control.down = keys[Keys.S] || keys[Keys.DOWN];
        control.left = keys[Keys.A] || keys[Keys.LEFT];
        control.right = keys[Keys.D] || keys[Keys.RIGHT];
//        debugCameraControls(deltaTime);
    }

    @Override
    public boolean keyDown(int keycode) {
        keys[keycode] = true;
        StateComponent state = Mappers.state.get(players.first());
        state.setAction(StateComponent.ACTION_WALKING);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        keys[keycode] = false;
        StateComponent state = Mappers.state.get(players.first());
        state.setAction(StateComponent.ACTION_STANDING);
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (players.size() == 0) {
            return false;
        }
        ControllableComponent control = Mappers.controllable.get(players.first());
        control.attack = true;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (players.size() == 0) {
            return false;
        }
        ControllableComponent control = Mappers.controllable.get(players.first());
        control.attack = false;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    private boolean handleControls(int keyCode, boolean keyDown) {
        if (players.size() == 0) {
            return false;
        }
        boolean handled = false;

        ControllableComponent control = Mappers.controllable.get(players.first());
        if (keyCode == Keys.W || keyCode == Keys.DOWN) {
            control.down = keyDown;
            handled = true;
        }

        return handled;
    }

    private void debugCameraControls(float deltaTime) {
        if (players.size() == 0) {
            return;
        }

        Entity player = players.first();
    }
}
