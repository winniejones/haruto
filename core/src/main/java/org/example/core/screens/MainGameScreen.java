package org.example.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.core.GameResources;
import org.example.core.managers.CameraManager;
import org.example.core.managers.MapManager;

public class MainGameScreen implements Screen {
    private static final String TAG = MainGameScreen.class.getSimpleName();

    private GameResources gameResources;
    private Stage stage;
    private BitmapFont font;

    private CameraManager cameraManager;
    private MapManager mapManager;

    public MainGameScreen(GameResources gameResources) {
        this.gameResources = gameResources;
        initializeStage();
        initializeManagers();
        initializeSystems();
        font = new BitmapFont();
        font.setColor(Color.BLUE);
    }

    private void initializeStage() {
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    private void initializeManagers() {
        this.cameraManager = new CameraManager();
        this.mapManager = new MapManager(gameResources.getBatch(), cameraManager.getCamera());
    }

    private void initializeSystems() {
    }

    @Override
    public void render(float delta) {
        cameraManager.render(delta);
        mapManager.render();
        gameResources.getBatch().begin();
        gameResources.getEngine().update(delta);
        gameResources.getBatch().end();

//        mapManager.renderUpperLayer();
    }

    @Override
    public void resize(int width, int height) {
        cameraManager.resize(width, height);
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        mapManager.dispose();
    }

    @Override
    public void show() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
