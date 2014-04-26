package net.mostlyoriginal.game.system.agent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.Bag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.system.physics.CollisionSystem;
import net.mostlyoriginal.game.component.agent.Selectable;

/**
 * Radiobutton selection of a single entity.
 *
 * @author Daan van Yperen
 */
@Wire
public class SelectableSystem extends EntityProcessingSystem {

    ComponentMapper<Anim> am;
    ComponentMapper<Selectable> sm;
    ComponentMapper<Pos> pm;
    CollisionSystem collisionSystem;
    TagManager tagManager;
    public Entity mouse;
    private Entity selectedEntity;
    private Entity indicator;

    public SelectableSystem() {
        super(Aspect.getAspectForAll(Selectable.class, Bounds.class, Pos.class));
    }

    @Override
    protected void begin() {
        mouse = tagManager.getEntity("mouse");
        indicator = tagManager.getEntity("indicator");
        am.get(indicator).visible = false;
    }

    @Override
    protected void end() {

        Object[] array = ((Bag<Entity>) getActives()).getData();
        for (int i = 0, s = getActives().size(); s > i; i++) {
            Entity e = (Entity) array[i];
            if (sm.has(e)) {
                Selectable selectable = sm.get(e);
                selectable.selected = e == selectedEntity;

                if (selectable.selected) {
                    // move indicator to selected entity and make visible.

                    Pos ePos = pm.get(e);
                    Pos indicatorPos = pm.get(indicator);
                    indicatorPos.x = ePos.x;
                    indicatorPos.y = ePos.y;
                    am.get(indicator).visible = true;
                }
            }
        }
    }

    @Override
    protected void process(Entity e) {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && collisionSystem.overlaps(mouse, e)) {
            selectedEntity = e;
        }
    }
}
