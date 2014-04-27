package net.mostlyoriginal.game.manager;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Manager;
import com.artemis.annotations.Wire;
import net.mostlyoriginal.game.component.CastleBlock;
import net.mostlyoriginal.game.system.CastleSystem;

/**
 * Keeps track of who lives where in the castle.
 *
 * @author Daan van Yperen
 */
@Wire
public class ServantManager extends Manager {

    public boolean castleDirty = true;
    EntityFactorySystem entityFactorySystem;
    ComponentMapper<CastleBlock> cm;
    private Entity[][] actor= new Entity[CastleSystem.H][CastleSystem.W];


    public void servantLostHome(int x, int y) {

        if ( actor[y][x] != null )
        {
            actor[y][x].deleteFromWorld();
            actor[y][x] = null;
        }
    }

    public void createServant(int x, int y, CastleBlock.Type type) {

        Entity entity = null;
        switch ( type ) {
            case EMPTY:
                break;
            case TOWER:
                entity = entityFactorySystem.createEntity("mage", 0, entityFactorySystem.SERVANT_Y);
                break;
            case WALL:
                break;
            case BARRACKS:
                entity = entityFactorySystem.createEntity("knight", 0, entityFactorySystem.SERVANT_Y);
                break;
            case SPELUNKER:
                entity = entityFactorySystem.createEntity("spelunker", 0, entityFactorySystem.SERVANT_Y);
                break;
        }

        if ( entity != null ) {
            actor[y][x] = entity;
            actor[y][x].addToWorld();
        }
    }
}
