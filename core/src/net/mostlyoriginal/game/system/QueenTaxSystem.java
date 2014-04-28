package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.game.component.Incappable;
import net.mostlyoriginal.game.component.Taxing;
import net.mostlyoriginal.game.component.agent.Clickable;
import net.mostlyoriginal.game.manager.AssetSystem;

/**
 * When the queen is taxin, she's taxin'!
 *
 * @author Daan van Yperen
 */
@Wire
public class QueenTaxSystem extends EntityProcessingSystem {

    ComponentMapper<Incappable> dm;
    ComponentMapper<Taxing> tm;
    ComponentMapper<Clickable> cm;
    UIWalletSystem uiWalletSystem;
    private AssetSystem assetSystem;

    public float clickCooldown  =0;

    public QueenTaxSystem() {
        super(Aspect.getAspectForAll(Incappable.class, Taxing.class, Anim.class));
    }

    @Override
    protected void process(Entity e) {

        Incappable incappable = dm.get(e);

        clickCooldown -= world.delta;
        if ( cm.has(e) && clickCooldown <= 0)
        {
            if ( cm.get(e).clicked )
            {
                clickCooldown = 0.5f;
                uiWalletSystem.add(1, e);
                assetSystem.playSfx("sfx_squeekytoy");
            }
        }

        if ( !incappable.incapacitated )
        {
            Taxing taxing = tm.get(e);
            taxing.cooldown -= world.delta;
            if ( taxing.cooldown <= 0)
            {
                taxing.cooldown = taxing.interval;
                uiWalletSystem.add(taxing.tax, e);
                assetSystem.playSfx("sfx_squeekytoy");
            }
        }
    }
}
