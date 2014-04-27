package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.component.Bobbing;

/**
 * Bob entity slowly up and down.
 *
 * @author Daan van Yperen
 */
@Wire
public class BobbingSystem extends EntityProcessingSystem {

    ComponentMapper<Pos> pm;
    ComponentMapper<Bobbing> bm;


    public BobbingSystem() {
        super(Aspect.getAspectForAll(Bobbing.class, Pos.class));
    }

    @Override
    protected void process(Entity e) {
        Pos pos = pm.get(e);
        Bobbing bobbing = bm.get(e);

        bobbing.age += world.delta * bobbing.speed;

        pos.x = bobbing.originX + MathUtils.cosDeg(bobbing.age * 360) * bobbing.maxDistanceX;
        pos.y = bobbing.originY + MathUtils.sinDeg(bobbing.age * 360) * bobbing.maxDistanceY;
    }
}
