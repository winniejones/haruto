package org.example.core;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameResources {
    private static GameResources instance;

    private Engine engine;
    private SpriteBatch batch;

    public GameResources(Engine engine, SpriteBatch batch) {
        this.engine = engine;
        this.batch = batch;

        if (instance == null) {
            instance = this;
        }
    }

    public Engine getEngine() {
        return engine;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public static GameResources get() {
        return instance;
    }
}
