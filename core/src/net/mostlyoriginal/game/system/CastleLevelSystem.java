package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.game.component.Level;

/**
 * Applies castle level to servants.
 *
 * @author Daan van Yperen
 */
@Wire
public class CastleLevelSystem extends EntityProcessingSystem {

    ComponentMapper<Level> lm;
    ComponentMapper<Anim> am;
    CastleSystem castleSystem;

    public CastleLevelSystem() {
        super(Aspect.getAspectForAll(Level.class));
    }

    @Override
    protected void process(Entity e) {
        Level level = lm.get(e);
        switch ( level.type )
        {
            case "queen": updateLevel(e, castleSystem.queenLevel); break;
            case "knight":  updateLevel(e, castleSystem.knightLevel); break;
            case "mage":  updateLevel(e, castleSystem.mageLevel); break;
            case "spelunker":  updateLevel(e, castleSystem.spelunkerLevel); break;
            default: throw new RuntimeException("No idea how to establish level for " + level.type);

        }
    }

    private void updateLevel(Entity e, int newLevel) {

        Level level = lm.get(e);
        if ( level.level != newLevel )
        {
            level.level = newLevel;
        }

        // hide or unhide actor depending on level.
        if ( am.has(e))
        {
            Anim anim = am.get(e);
            anim.visible = level.level > 0;
        }
    }
}
