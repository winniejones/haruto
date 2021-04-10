package org.example.core.components;

import com.badlogic.ashley.core.Component;

public class ControllableComponent implements Component {
    //movement
    public boolean up, down, left, right;

    //attack
    public boolean attack;
}
