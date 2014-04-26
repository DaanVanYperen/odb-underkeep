package net.mostlyoriginal.game.system.agent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.system.physics.CollisionSystem;
import net.mostlyoriginal.game.component.agent.Clickable;
import net.mostlyoriginal.game.component.agent.Focusable;

/**
 * @author Daan van Yperen
 */
@Wire
public class ClickableSystem extends EntityProcessingSystem {

    ComponentMapper<Clickable> cm;
    CollisionSystem collisionSystem;
    TagManager tagManager;
    public Entity mouse;
    public boolean leftButtonDown;

    public ClickableSystem() {
        super(Aspect.getAspectForAll(Clickable.class, Bounds.class, Pos.class));
    }

    @Override
    protected void begin() {
        mouse = tagManager.getEntity("mouse");
        leftButtonDown = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
    }

    @Override
    protected void process(Entity e) {
        Clickable clickable = cm.get(e);
        clickable.clicked = (leftButtonDown && collisionSystem.overlaps(mouse, e));
    }
}
