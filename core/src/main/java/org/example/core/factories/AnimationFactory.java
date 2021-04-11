package org.example.core.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static org.example.core.utils.Assets.ITACHI_ASSET;

public class AnimationFactory {
    public static Animation<TextureRegion> getItachiUp(float frameDuration) {
        return new Animation<>(
                frameDuration,
                new TextureRegion(
                        loadTexture(ITACHI_ASSET),
                        0,
                        96,
                        32,
                        32
                ),
                new TextureRegion(
                        loadTexture(ITACHI_ASSET),
                        32,
                        96,
                        32,
                        32
                ),
                new TextureRegion(
                        loadTexture(ITACHI_ASSET),
                        64,
                        96,
                        32,
                        32
                )
        );
    }

    public static Animation<TextureRegion> getItachiDown(float frameDuration) {
        return easyGetRegions(
                loadTexture(ITACHI_ASSET),
                frameDuration, 3,
                32, 0, 0, false
        );
    }

    public static Animation<TextureRegion> getItachiLeft(float frameDuration) {
        return easyGetRegions(
                loadTexture(ITACHI_ASSET),
                frameDuration, 3,
                32, 0, 32, true
        );
    }

    public static Animation<TextureRegion> getItachiRight(float frameDuration) {
        return easyGetRegions(
                loadTexture(ITACHI_ASSET),
                frameDuration, 3,
                32, 0, 64, true
        );
    }

    public static Texture loadTexture(String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public static Animation<TextureRegion> easyGetRegions(
            Texture texture, float frameDuration, int amountOfFrames, int pxSize, int xStart, int yStart, boolean regionIsHorizontallyAligned
    ) {
        TextureRegion[] regions = new TextureRegion[amountOfFrames];
        for(int i = 0; i < amountOfFrames; i++) {
            if(regionIsHorizontallyAligned){
                regions[i] = new TextureRegion(
                        texture,
                        xStart+ (pxSize * i),
                        yStart ,
                        pxSize,
                        pxSize
                );
            } else {
                regions[i] = new TextureRegion(
                        texture,
                        xStart,
                        yStart + (pxSize * i),
                        pxSize,
                        pxSize
                );
            }
        }
        return new Animation<>(frameDuration, regions);
    }
}
