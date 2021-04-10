package org.example.core.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import org.example.core.factories.EntityFactory;
import org.example.core.factories.FontFactory;
import org.example.core.systems.*;
import org.example.core.utils.LogHelper;

import java.util.concurrent.TimeUnit;

public class MainGameScreen implements Screen {
    private static final String TAG = MainGameScreen.class.getSimpleName();

    private static InputMultiplexer inputMultiplexer;

    public static ExtendViewport viewport;
    public static World world;
    public static OrthographicCamera cam;
    public static SpriteBatch batch;
    private static Stage stage;
    private static long gameTimeCurrent, gameTimeStart, timePaused;

    private static Engine engine;
    public static ShapeRenderer shape;

    public MainGameScreen() {
        Gdx.app.log(this.getClass().getSimpleName(), "Screen Reset.");
        cam = new OrthographicCamera();
        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        float invScale = 1.0f / 30f;
        float viewportWidth = 1280 * invScale;
        float viewportHeight = 720 * invScale;
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
//        viewport = new ExtendViewport(viewportWidth, viewportHeight, cam);
//        viewport.apply();

        cam.setToOrtho(false, viewportWidth, viewportWidth * (viewportHeight / viewportWidth));

         //set this as input processor for mouse wheel scroll events
        inputMultiplexer = new InputMultiplexer();

        initializeUI();
        initializeCore();
        initializeGame();
    }

    public static long getGameTimeCurrent() {
        return gameTimeCurrent;
    }

    private void initializeCore() {
        engine = new Engine();

        world = new World(new Vector2(), true);
    }

    private void initializeGame() {
        Entity player = EntityFactory.createPlayer(400, 400);
        gameTimeStart = System.nanoTime();
        initializeGameWorld(player);
    }

    private void initializeUI() {
        if (VisUI.isLoaded())
            VisUI.dispose(true);
        VisUI.load(VisUI.SkinScale.X1);
        BitmapFont font = FontFactory.createFont(FontFactory.fontBitstreamVM, 12);
        VisUI.getSkin().add(FontFactory.skinSmallFont, font);

        stage = new Stage(new ScreenViewport());
        inputMultiplexer.addProcessor(0, stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void initializeSystems() {
        Gdx.app.log(this.getClass().getSimpleName(), "Init Systems");
        long time = System.currentTimeMillis();

        engine.addSystem(new ClearScreenSystem());
        engine.addSystem(new DesktopInputSystem());

        engine.addSystem(new CharacterControlSystem());

        engine.addSystem(new RenderMapSystem());
        engine.addSystem(new CameraSystem());
        engine.addSystem(new RenderEntitySystem());
        engine.addSystem(new DebugSystem());
        long now = System.currentTimeMillis();
        Gdx.app.log(this.getClass().getSimpleName(), "systems loaded in " + (now - time) + "ms");
    }

    private void initializeGameWorld(Entity player) {
        Gdx.app.log(this.getClass().getSimpleName(), "Init " + LogHelper.objString(player));

        initializeSystems();

        engine.addEntity(player);
    }

    @Override
    public void render(float delta) {
        gameTimeCurrent = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - gameTimeStart);
        engine.update(delta);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
//        cameraManager.render(delta);
//        mapManager.render();
//        gameResources.getBatch().begin();
//        gameResources.getEngine().update(delta);
//        gameResources.getBatch().end();

//        mapManager.renderUpperLayer();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        Gdx.app.log(this.getClass().getSimpleName(), "Disposing: " + this.getClass().getSimpleName());
        shape.dispose();
        batch.dispose();
        // clean up after self
        for (EntitySystem sys : engine.getSystems()) {
            if (sys instanceof Disposable)
                ((Disposable) sys).dispose();
        }

        engine.removeAllEntities();
        engine = null;
        world.dispose();
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

    public static void adjustGameTime(long deltaMillis) {
        gameTimeStart += deltaMillis * 1000000;
    }

    public static Stage getStage() {
        return stage;
    }

    public static InputMultiplexer getInputMultiplexer() {
        return inputMultiplexer;
    }
}
