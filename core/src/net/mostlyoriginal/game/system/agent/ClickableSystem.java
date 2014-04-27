package net.mostlyoriginal.game.system.agent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.system.physics.CollisionSystem;
import net.mostlyoriginal.game.component.agent.Clickable;
import net.mostlyoriginal.game.manager.AssetSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Todo Probably want to separate depth from anim, since they're used for other things as well.
 * @author Daan van Yperen
 */
@Wire
public class ClickableSystem extends EntitySystem {

    ComponentMapper<Clickable> cm;
    private ComponentMapper<Anim> sm;
    CollisionSystem collisionSystem;
    TagManager tagManager;
    private Entity mouse;
    private boolean leftButtonDown;
    private boolean found;

    private final List<Entity> sortedEntities = new ArrayList<Entity>();
    public boolean sortedDirty = false;
    private AssetSystem assetSystem;

    public ClickableSystem() {
        super(Aspect.getAspectForAll(Clickable.class, Bounds.class, Pos.class, Anim.class));
    }

    @Override
    protected void begin() {
        mouse = tagManager.getEntity("mouse");
        leftButtonDown = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        found=false;
    }

    // flipped compared to rendering, for clickables we want to hit the entity in front first in the list.
    public Comparator<Entity> layerSortComperator = new Comparator<Entity>() {
        @Override
        public int compare(Entity e1, Entity e2) {
            return sm.get(e2).layer - sm.get(e1).layer;
        }
    };

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {

        if (sortedDirty) {
            sortedDirty = false;
            Collections.sort(sortedEntities, layerSortComperator);
        }

        for (Entity entity : sortedEntities) {
            process(entity);
        }
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }


    protected void process(Entity e) {
        Clickable clickable = cm.get(e);
        if ( !found && (leftButtonDown && collisionSystem.overlaps(mouse, e)) ) {
            found = clickable.clicked = true;
            assetSystem.playSfx("sfx_click");
        } else clickable.clicked=false;
    }

    @Override
    protected void inserted(Entity e) {
        sortedEntities.add(e);
        sortedDirty = true;
    }

    @Override
    protected void removed(Entity e) {
        sortedEntities.remove(e);
    }
}
