package org.example.core.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;

public class RenderOverlayMapSystem extends EntitySystem implements Disposable {
    public static final String PATH_TO_TILE_MAP = "map/test/test_map.tmx";
    public static final int[] UPPER_LAYERS = new int[]{3,4};

    /* Tile map */
    private OrthogonalTiledMapRenderer mapRenderer;

    public RenderOverlayMapSystem(OrthogonalTiledMapRenderer mapRenderer) {
        this.mapRenderer = mapRenderer;

        this.initializeMap();
    }

    private void initializeMap() {
        Gdx.app.log(this.getClass().getSimpleName(), "Init Overlay Map Renderer");
    }

    @Override
    public void update(float delta) {
        mapRenderer.render(UPPER_LAYERS);
    }

    @Override
    public void dispose() { }
}
