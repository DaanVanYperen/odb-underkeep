package net.mostlyoriginal.game.system.agent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.CastleBlock;
import net.mostlyoriginal.game.component.Cost;
import net.mostlyoriginal.game.component.ExpansionOption;
import net.mostlyoriginal.game.component.ExpansionPoint;
import net.mostlyoriginal.game.component.agent.Clickable;
import net.mostlyoriginal.game.manager.EntityFactorySystem;
import net.mostlyoriginal.game.system.CastleSystem;
import net.mostlyoriginal.game.system.UIWalletSystem;

/**
 * Sends selected Qeestee on quest after the quest is clicked.
 *
 * @author Daan van Yperen
 */
@Wire
public class ExpansionPointSystem extends EntityProcessingSystem {


    private static final float OFFSET_PER_COUNT = 17;
    ComponentMapper<Clickable> cm;
    ComponentMapper<Pos> posm;
    ComponentMapper<Anim> am;
    ComponentMapper<ExpansionPoint> pm;
    ComponentMapper<ExpansionOption> om;
    ComponentMapper<Cost> com;
    TagManager tagManager;
    CastleSystem castleSystem;
    CameraSystem cameraSystem;
    GroupManager groupManager;
    EntityFactorySystem entityFactorySystem;

    private ExpansionOption option;
    private Cost cost;
    private UIWalletSystem uiWalletSystem;

    public ExpansionPointSystem() {
        super(Aspect.getAspectForAll(Clickable.class, Pos.class, ExpansionPoint.class, Bounds.class));
    }

    @Override
    protected void begin() {
        final Entity focusEntity = tagManager.getEntity("focus");
        if (focusEntity != null && om.has(focusEntity)) {
            option = om.get(focusEntity);
            cost = com.get(focusEntity);
        } else { option = null; cost = null; }
    }

    @Override
    protected void process(Entity e) {

        ExpansionPoint point = pm.get(e);

        Anim anim = am.get(e);

        // only display valid locations for placement.
        anim.visible = option != null &&
                ((option.type.subType == CastleBlock.SubType.WALL && point.allowWalls)
                        || (option.type.subType == CastleBlock.SubType.TOWER && point.allowTowers));

        // expansion point just clicked? prep the menu!
        if (anim.visible && cm.get(e).clicked) {
            attemptBuyAt(option, point);
        }
    }

    private void attemptBuyAt(ExpansionOption buy, ExpansionPoint point) {
        if ( cost != null && uiWalletSystem.pay(cost.cost) ) {
            tagManager.unregister("focus");
            castleSystem.setBlock(point.x, point.y, option.type);
        }
    }
}
