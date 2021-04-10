package org.example.core;

import com.badlogic.gdx.Game;
import org.example.core.managers.ScreenManager;

public class HarutoGame extends Game {
    private ScreenManager screenManager;

    @Override
    public void create() {
        initializeScreen();
    }

    @Override
    public void dispose() {
        if(screenManager.getCurrentScreen() != null)
			screenManager.getCurrentScreen().dispose();

		super.dispose();
    }

    @Override
    public void render() {
		screenManager.render();
    }

    private void initializeScreen() {
		screenManager = new ScreenManager();
		screenManager.loadWorldScreen();
	}
}
