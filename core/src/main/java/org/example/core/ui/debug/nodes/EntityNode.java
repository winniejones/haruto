package org.example.core.ui.debug.nodes;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.example.core.ui.debug.SystemExplorerWindow;
import org.example.core.utils.LogHelper;
import org.example.core.utils.SimpleTimer;

import static org.example.core.factories.FontFactory.skinSmallFont;

public class EntityNode extends UpdateNode {
    boolean isNew = false;
    private SimpleTimer newTimer;

    public EntityNode(Entity entity, Skin skin) {
        super(new Label(LogHelper.objString(entity), skin, skinSmallFont, Color.WHITE), entity);
    }

    public EntityNode(Entity entity, Skin skin, boolean markNew) {
        this(entity, skin);

        isNew = markNew;
        if (isNew) {
            newTimer = new SimpleTimer(SystemExplorerWindow.newTime, true);
            getActor().setColor(Color.GREEN);
        }
    }

    public Entity getEntity() {
        return (Entity) getValue();
    }

    @Override
    public void update() {
        if (getValue() == null) {
            return;
        }

        if (isNew && newTimer.tryEvent()) {
            isNew = false;
            newTimer = null;
            getActor().setColor(Color.WHITE);
        }

        ((Label) getActor()).setText(toString());

        if (!isExpanded())
            return;

        boolean showHistory = SystemExplorerWindow.showHistory;

        //add nodes
        ImmutableArray<Component> components = getEntity().getComponents();
        for (Component comp : components) {
            if (findNode(comp) == null) {
                add(new ReflectionNode(comp, showHistory));
            }
        }

        //update nodes, clean up dead nodes
        for (Object child : getChildren()) {
            UpdateNode node = (UpdateNode)child;
            if (!components.contains((Component) node.getValue(), false)) {
                if (showHistory) {
                    if (!(node instanceof GhostNode)) {
                        node.removeAndCreateGhost(SystemExplorerWindow.includeChildren);
                    }
                } else {
                    node.remove();
                }
            } else {
                node.update();
            }
        }
    }

    @Override
    public String toString() {
        if (getValue() == null)
            return super.toString();

        return LogHelper.objString(getEntity()) + " [" + getEntity().getComponents().size() + "]";
    }
}
