package net.mostlyoriginal.game.system.agent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.system.camera.CameraShakeSystem;
import net.mostlyoriginal.game.component.Erupt;
import net.mostlyoriginal.game.manager.EntityFactorySystem;
import net.mostlyoriginal.game.system.CastleSystem;

/**
 * @author Daan van Yperen
 */

@Wire
public class EruptSystem extends EntityProcessingSystem {

    ComponentMapper<Erupt> em;
    ComponentMapper<Pos> pm;
    ComponentMapper<Bounds> bm;

    CastleSystem castleSystem;
    CameraShakeSystem cameraShakeSystem;
    EntityFactorySystem entityFactorySystem;

    public EruptSystem() {
        super(Aspect.getAspectForAll(Erupt.class, Pos.class, Bounds.class));
    }

    @Override
    protected void process(Entity e) {
        Erupt erupt = em.get(e);
        Pos pos = pm.get(e);
        Bounds bounds = bm.get(e);

        if ( pos.y + bounds.maxy > erupt.y)
        {
            eruptAndDamageCastle(e);
        }
    }

    private void eruptAndDamageCastle(Entity e) {

         e.deleteFromWorld();
        cameraShakeSystem.shake(1);

        entityFactorySystem.createEntity("jumping-imp" ,castleSystem.getRandomUsedX(), 30).addToWorld();
    }
}
