package org.example.core.components;

import com.badlogic.ashley.core.Component;

public class StateComponent implements Component {
    public static final int ACTION_STANDING = 0;
    public static final int ACTION_WALKING = 1;
    public static final int ACTION_RUNNING = 2;
    public static final int ACTION_SWIMING = 3;
    public static final int ACTION_ATTACKING = 4;

    private int state = 0;
    private int direction = 0;
    private int action = 0;
    public float time = 0.0f;
    public boolean isLooping = false;

    public void set(int newState) {
        state = newState;
        time = 0.0f;
    }

    public void setDirection(int dir) {
        if(direction != dir) {
            time = 0.0f;
        }
        direction = dir;
    }

    public void setAction(int act) {
        action = act;
        time = 0.0f;
    }

    public int get() {
        return state;
    }

    public int getDirection() {
        return direction;
    }

    public int getAction() {
        return action;
    }
}
