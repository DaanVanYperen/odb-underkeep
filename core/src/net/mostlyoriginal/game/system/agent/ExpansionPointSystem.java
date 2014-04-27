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
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.ExpansionOption;
import net.mostlyoriginal.game.component.ExpansionPoint;
import net.mostlyoriginal.game.component.agent.Clickable;
import net.mostlyoriginal.game.manager.EntityFactorySystem;
import net.mostlyoriginal.game.system.CastleSystem;

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
    ComponentMapper<ExpansionPoint> pm;
    ComponentMapper<ExpansionOption> om;
    TagManager tagManager;
    CastleSystem castleSystem;
    CameraSystem cameraSystem;
    GroupManager groupManager;
    EntityFactorySystem entityFactorySystem;

    public ExpansionPointSystem() {
        super(Aspect.getAspectForAll(Clickable.class, Pos.class, ExpansionPoint.class, Bounds.class));
    }

    @Override
    protected void process(Entity e) {


        // expansion point just clicked? prep the menu!
        if ( cm.get(e).clicked )
        {
            ExpansionPoint point = pm.get(e);
            Pos pos = posm.get(e);

            int count = (point.allowTowers ? 1 : 0) + (point.allowWalls ? 3 : 0);
            int x = (int)pos.x - (int)(((OFFSET_PER_COUNT) * 0.5f * count) - 9f) ;
            int y = (int)pos.y + 20;

            if ( x <= 4 ) x = 4;
            if ( y >= cameraSystem.getPixelHeight() - 20 ) y = (int)(cameraSystem.getPixelHeight() - 20);

            if ( point.allowTowers )
            {
                entityFactorySystem.createEntity("expand-mage", x,y).addToWorld(); x+=OFFSET_PER_COUNT;
            }
            if ( point.allowWalls )
            {
                entityFactorySystem.createEntity("expand-knight", x,y).addToWorld(); x+=OFFSET_PER_COUNT;
                entityFactorySystem.createEntity("expand-spelunker", x,y).addToWorld(); x+=OFFSET_PER_COUNT;
                entityFactorySystem.createEntity("expand-wall", x,y).addToWorld(); x+=OFFSET_PER_COUNT;
            }
        }
    }
}
