package org.example.core.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.core.components.AnimationComponent;
import org.example.core.components.StateComponent;
import org.example.core.components.TextureComponent;
import org.example.core.utils.Mappers;

public class AnimationSystem extends IteratingSystem {
    public AnimationSystem() {
        super(Family.all(TextureComponent.class, AnimationComponent.class, StateComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TextureComponent tex = Mappers.texture.get(entity);
        AnimationComponent anim = Mappers.animation.get(entity);
        StateComponent state = Mappers.state.get(entity);

        Animation<TextureRegion> animation = anim.animations.get(state.getDirection());

        if (animation != null) {
            if (state.getAction() == StateComponent.ACTION_WALKING) {
                tex.region = animation.getKeyFrame(deltaTime, true);
            } else if (state.getAction() == StateComponent.ACTION_STANDING) {
                tex.region = animation.getKeyFrame(deltaTime,true);
            }

//            tex.region = animation.getKeyFrame(state.time);
        }

        state.time += deltaTime;
    }
}
