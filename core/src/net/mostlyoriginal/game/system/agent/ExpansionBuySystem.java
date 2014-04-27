package net.mostlyoriginal.game.system.agent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.component.Cost;
import net.mostlyoriginal.game.component.ExpansionOption;
import net.mostlyoriginal.game.component.ExpansionPoint;
import net.mostlyoriginal.game.component.agent.Clickable;
import net.mostlyoriginal.game.system.CastleSystem;
import net.mostlyoriginal.game.system.UIWalletSystem;

/**
 * Allows building an expansion.
 *
 * @author Daan van Yperen
 */
@Wire
@Deprecated
public class ExpansionBuySystem extends EntityProcessingSystem {

    ComponentMapper<Clickable> cm;
    ComponentMapper<ExpansionPoint> pm;
    ComponentMapper<ExpansionOption> om;
    TagManager tagManager;
    CastleSystem castleSystem;
    UIWalletSystem uiWalletSystem;
    private Entity lastFocus;
    public Entity focus;


    public ExpansionBuySystem() {
        super(Aspect.getAspectForAll(Clickable.class, Pos.class, ExpansionOption.class, Bounds.class));
    }

    @Override
    protected void begin() {
        super.begin();
    }

    @Override
    protected void end() {

    }

    @Override
    protected void process(Entity e) {
    }

}
