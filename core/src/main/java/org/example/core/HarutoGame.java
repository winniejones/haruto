package org.example.core;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.example.core.managers.ScreenManager;
import org.example.core.systems.RenderSystem;

public class HarutoGame extends Game {
    private Engine engine;
    private SpriteBatch batch;
    private ScreenManager screenManager;
    private BitmapFont font;

    @Override
    public void create() {
        engine = new Engine();
        batch = new SpriteBatch();

        initializeScreen();
		initializeRenderSystem();
    }

    @Override
    public void dispose() {
        if(screenManager.getCurrentScreen() != null)
			screenManager.getCurrentScreen().dispose();

		super.dispose();
    }

    @Override
    public void render() {
        clearScreen();
		screenManager.render();
    }

    private void initializeScreen() {
		screenManager = new ScreenManager(new GameResources(engine, batch));
		screenManager.loadWorldScreen();
	}

	private void initializeRenderSystem() {
        RenderSystem renderSystem = new RenderSystem(batch);
		engine.addSystem(renderSystem);
    }

	private void clearScreen() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}
}
