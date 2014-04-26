package net.mostlyoriginal.game.system.agent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.component.ExpansionOption;
import net.mostlyoriginal.game.component.ExpansionPoint;
import net.mostlyoriginal.game.component.agent.Clickable;
import net.mostlyoriginal.game.system.CastleSystem;

/**
 * Sends selected Qeestee on quest after the quest is clicked.
 *
 * @author Daan van Yperen
 */
@Wire
public class ExpansionBuySystem extends EntityProcessingSystem {

    ComponentMapper<Clickable> cm;
    ComponentMapper<ExpansionPoint> pm;
    ComponentMapper<ExpansionOption> om;
    TagManager tagManager;
    CastleSystem castleSystem;
    private Entity lastFocus;
    public Entity focus;


    public ExpansionBuySystem() {
        super(Aspect.getAspectForAll(Clickable.class, Pos.class, ExpansionOption.class, Bounds.class));
    }

    @Override
    protected void begin() {
        super.begin();

        focus = tagManager.getEntity("focus");

        if (lastFocus == null)
            lastFocus = focus;
    }

    @Override
    protected void end() {
        lastFocus = focus;
    }

    @Override
    protected void process(Entity e) {

        if ( cm.get(e).clicked && tagManager.isRegistered("focus") )
        {
            attemptBuyAt(e, focus);
        }

        // kill expansion options if focus changed, or not selecting an expansionpoint.
        if ( focus == null || lastFocus != focus || !pm.has(focus) )
        {
            e.deleteFromWorld();
        }
    }

    private void attemptBuyAt(Entity buy, Entity at) {
        if ( pm.has(at) )
        {
            tagManager.unregister("focus");
            ExpansionOption option = om.get(buy);
            ExpansionPoint point = pm.get(at);
            castleSystem.setBlock(point.x, point.y, option.type);
        }


    }
}
