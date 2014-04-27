package net.mostlyoriginal.game.system.agent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.system.physics.CollisionSystem;
import net.mostlyoriginal.game.component.agent.Clickable;
import net.mostlyoriginal.game.component.agent.Focusable;

/**
 * Radiobutton selection of a single entity.
 *
 * @author Daan van Yperen
 */
@Wire
public class FocusableSystem extends EntityProcessingSystem {

    ComponentMapper<Anim> am;
    ComponentMapper<Clickable> cm;
    ComponentMapper<Pos> pm;
    CollisionSystem collisionSystem;
    TagManager tagManager;
    private Entity indicator;

    public FocusableSystem() {
        super(Aspect.getAspectForAll(Focusable.class, Clickable.class, Bounds.class, Pos.class));
    }

    @Override
    protected void begin() {
        indicator = tagManager.getEntity("indicator");
        if ( !tagManager.isRegistered("focus")) {
             am.get(indicator).visible = false;
        }
    }

    @Override
    protected void process(Entity e) {
        Clickable clickable = cm.get(e);
        if (clickable.clicked) {

            // only render when main anim is visible.
            if ( am.has(e) && !am.get(e).visible ) return;

            tagManager.register("focus", e);
            // move indicator to selected entity and make visible.
            Pos ePos = pm.get(e);
            Pos indicatorPos = pm.get(indicator);
            indicatorPos.x = ePos.x;
            indicatorPos.y = ePos.y;
            am.get(indicator).visible = true;
        }
    }
}
