package org.example.core.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static org.example.core.utils.Assets.ITACHI_ASSET;

public class AnimationFactory {
    public static Animation<TextureRegion> getAnimationRegion(float frameDuration, String pathToAsset, int pxSize, int yStart, int[] frameOrder, boolean texRegIsHorizontal) {
        if (texRegIsHorizontal) {
            return getRegionsHorizontally(
                    loadTexture(pathToAsset),
                    frameDuration, frameOrder,
                    pxSize, 0, yStart
            );
        } else {
            return getRegionsVertically(
                    loadTexture(pathToAsset),
                    frameDuration, frameOrder,
                    pxSize, 0, yStart
            );
        }
    }

    public static Animation<TextureRegion> getItachiUp(float frameDuration) {
        return getRegionsHorizontally(
                loadTexture(ITACHI_ASSET),
                frameDuration, new int[]{1, 0, 1, 2},
                32, 0, 96
        );
    }

    public static Animation<TextureRegion> getItachiDown(float frameDuration) {
        return getRegionsHorizontally(
                loadTexture(ITACHI_ASSET),
                frameDuration, new int[]{1, 0, 1, 2},
                32, 0, 0
        );
    }

    public static Animation<TextureRegion> getItachiLeft(float frameDuration) {
        return getRegionsHorizontally(
                loadTexture(ITACHI_ASSET),
                frameDuration, new int[]{1, 0, 1, 2},
                32, 0, 32
        );
    }

    public static Animation<TextureRegion> getItachiRight(float frameDuration) {
        return getRegionsHorizontally(
                loadTexture(ITACHI_ASSET),
                frameDuration, new int[]{1, 0, 1, 2},
                32, 0, 64
        );
    }

    public static Texture loadTexture(String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public static Animation<TextureRegion> getRegionsHorizontally(
            Texture texture, float frameDuration, int[] frameOrder, int pxSize, int xStart, int yStart
    ) {
        TextureRegion[] regions = new TextureRegion[frameOrder.length];
        for (int i = 0; i < frameOrder.length; i++) {
            regions[i] = new TextureRegion(
                    texture,
                    xStart + (pxSize * frameOrder[i]),
                    yStart,
                    pxSize,
                    pxSize
            );
        }
        return new Animation<>(frameDuration, regions);
    }

    public static Animation<TextureRegion> getRegionsVertically(
            Texture texture, float frameDuration, int[] frameOrder, int pxSize, int xStart, int yStart
    ) {
        TextureRegion[] regions = new TextureRegion[frameOrder.length];
        for (int i = 0; i < frameOrder.length; i++) {
            regions[i] = new TextureRegion(
                    texture,
                    xStart,
                    yStart + (pxSize * frameOrder[i]),
                    pxSize,
                    pxSize
            );
        }
        return new Animation<>(frameDuration, regions);
    }
}
