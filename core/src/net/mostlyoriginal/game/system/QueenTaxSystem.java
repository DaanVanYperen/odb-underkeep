package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.game.component.Incappable;
import net.mostlyoriginal.game.component.Taxing;

/**
 * When the queen is taxin, she's taxin'!
 *
 * @author Daan van Yperen
 */
@Wire
public class QueenTaxSystem extends EntityProcessingSystem {

    ComponentMapper<Incappable> dm;
    ComponentMapper<Taxing> tm;
    UIWalletSystem uiWalletSystem;

    public QueenTaxSystem() {
        super(Aspect.getAspectForAll(Incappable.class, Taxing.class, Anim.class));
    }

    @Override
    protected void process(Entity e) {

        Incappable incappable = dm.get(e);

        if ( !incappable.incapacitated )
        {
            Taxing taxing = tm.get(e);
            taxing.cooldown -= world.delta;
            if ( taxing.cooldown <= 0)
            {
                taxing.cooldown = taxing.interval;
                uiWalletSystem.add(taxing.tax, e);
            }
        }
    }
}
