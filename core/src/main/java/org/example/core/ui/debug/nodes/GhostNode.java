package org.example.core.ui.debug.nodes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import org.example.core.ui.debug.SystemExplorerWindow;
import org.example.core.utils.SimpleTimer;

import static org.example.core.factories.FontFactory.skinSmallFont;

public class GhostNode extends UpdateNode {
    private SimpleTimer removeTimer;

    public GhostNode(UpdateNode nodeRemoved, boolean includeChildren) {
        super(new Label(nodeRemoved.getActor().getName(), VisUI.getSkin(), skinSmallFont, Color.RED), null);

        Tree.Node parent = nodeRemoved.getParent();
        final Array<Tree.Node> parentsSiblings;
        if (parent == null) {
            parentsSiblings = nodeRemoved.getTree().getRootNodes();
        } else {
            parentsSiblings = parent.getChildren();
        }
        //insert in same position as removed node
        int index = parentsSiblings.indexOf(nodeRemoved, false);
        parent.insert(index, this);


        removeTimer = new SimpleTimer(SystemExplorerWindow.removeTime, true);

        setExpanded(nodeRemoved.isExpanded());
        if (includeChildren && isExpanded()) {
            addChildren(nodeRemoved.getChildren(), this);
        }
    }

    public void addChildren(Array<Tree.Node> children, Tree.Node root) {
        for (Tree.Node child : children) {
            addChildren(child.getChildren(), root);

            add(new MyNode(new Label(child.getActor().getName(), VisUI.getSkin(), skinSmallFont, Color.RED)));
        }
    }

    @Override
    public void update() {
        tryRemove();
    }

    public void tryRemove() {
        /*
        if (isExpanded()) {
            //this should be if has focus/isSelection (including children) instead
            return;
        }*/

        if (removeTimer.canDoEvent())
            remove();
    }
}
