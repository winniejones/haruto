package org.example.core.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;
import org.example.core.screens.MainGameScreen;
import org.example.core.utils.Dimensions;

public class RenderMapSystem extends EntitySystem implements Disposable {
    public static final String PATH_TO_TILE_MAP = "map/test/test_map.tmx";
    public static final int[] BASE_LAYERS = new int[]{0,1,2,3};
    public static final int[] UPPER_LAYERS = new int[]{4};
    private SpriteBatch batch;

    /* Tile map */
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;

    public RenderMapSystem() {
        this.batch = MainGameScreen.batch;
        this.camera = MainGameScreen.cam;

        this.initializeMap();
    }

    private void initializeMap() {
        Gdx.app.log(this.getClass().getSimpleName(), "Init Map Renderer");
        this.map = new TmxMapLoader().load(PATH_TO_TILE_MAP);

        this.mapRenderer = new OrthogonalTiledMapRenderer(this.map, Dimensions.PIXELS_TO_METRES, this.batch);
    }

    @Override
    public void update(float delta) {
        batch.setProjectionMatrix(camera.combined);
        mapRenderer.setView(camera);
        mapRenderer.render(BASE_LAYERS);
    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
    }
}
