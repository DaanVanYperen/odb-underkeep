package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import net.mostlyoriginal.game.component.CastleBlock;
import net.mostlyoriginal.game.manager.EntityFactorySystem;

/**
 * Keeps track of castle model, responsible for assembling and updating the castle.
 *
 * @author Daan van Yperen
 */
@Wire
public class CastleSystem extends EntityProcessingSystem {

    public static final int H = 10;
    public static final int W = 10;
    public CastleBlock.Type castle[][] = new CastleBlock.Type[H][W];

    public boolean castleDirty = true;

    EntityFactorySystem entityFactorySystem;

    public CastleSystem() {
        super(Aspect.getAspectForAll(CastleBlock.class));
        for ( int x=0;x<W;x++) {
            for (int y = 0; y < H; y++) {
                castle[y][x] = CastleBlock.Type.EMPTY;
            }
        }

        castle[0][4] = CastleBlock.Type.WALL;
        castle[0][3] = CastleBlock.Type.BARRACKS;
        castle[0][2] = CastleBlock.Type.WALL;
        castle[1][3] = CastleBlock.Type.WALL;
        castle[2][3] = CastleBlock.Type.TOWER;
        castle[3][3] = CastleBlock.Type.TOWER;
        castle[4][3] = CastleBlock.Type.TOWER;
        castle[0][5] = CastleBlock.Type.TOWER;
        castle[1][5] = CastleBlock.Type.TOWER;
    }

    @Override
    protected void end() {
        if ( castleDirty ) {
            castleDirty=false;
            assembleCastle();
        }
    }

    @Override
    protected void process(Entity e) {

    }

    protected void demolishCastle()
    {
        ImmutableBag<Entity> actives = getActives();
        for ( Entity active : actives )
        {
            if ( active != null && active.isActive() )
                active.deleteFromWorld();
        }
    }

    protected void assembleCastle()
    {
        demolishCastle();
        spawnCoreBlocks();
        spawnTrimming();
    }

    private void spawnTrimming() {

        // spawn trimming
        for ( int x=0;x<W;x++)
        {
            int px = 13 + x * 13;

            for (int y=0;y<H;y++) {
                int py = 30 + y * 17;

                // only spawn trimming on empty blocks.
                if ( castle[y][x] == CastleBlock.Type.EMPTY || castle[y][x] == CastleBlock.Type.TOWER)
                {
                    // top wall trimming.
                    if ( isWallAt(x, y-1) ) entityFactorySystem.createEntity("building-trimming-on-wall", px, py).addToWorld();
                    else if ( isWallAt(x+1, y-1) ) entityFactorySystem.createEntity("building-trimming-on-wall-left", px, py).addToWorld();
                    else if ( isWallAt(x-1, y-1) ) entityFactorySystem.createEntity("building-trimming-on-wall-right", px, py).addToWorld();

                    // side wall trimming left side
                    if ( isWallAt(x+1, y) && isWallAt(x+1, y+1) ) entityFactorySystem.createEntity("building-trimming-top-wall-left", px, py).addToWorld();
                    else if ( isWallAt(x+1, y) ) entityFactorySystem.createEntity("building-trimming-bottom-wall-left", px, py).addToWorld();

                    // side wall trimming right side.
                    if ( isWallAt(x-1, y) && isWallAt(x-1, y+1) ) entityFactorySystem.createEntity("building-trimming-top-wall-right", px, py).addToWorld();
                    else if ( isWallAt(x-1, y) ) entityFactorySystem.createEntity("building-trimming-bottom-wall-right", px, py).addToWorld();

                    // tower top.
                    if ( isTowerAt(x, y-1) && !isTowerAt(x,y) ) entityFactorySystem.createEntity("building-trimming-on-tower", px, py).addToWorld();

                    if ( (isWallAt(x, y-1) || isTowerAt(x,y-2)) && !isTowerAt(x,y-1))
                    {
                        entityFactorySystem.createEntity("building-flag", px, py).addToWorld();
                    }
                }
            }
        }
    }

    private boolean isWallAt(int x, int y) {
        if ( x<0 || y<0 || x>=W || y>=H ) return false;
        return (castle[y][x].subType == CastleBlock.SubType.WALL);
    }

    private boolean isTowerAt(int x, int y) {
        if ( x<0 || y<0 || x>=W || y>=H ) return false;
        return (castle[y][x].subType == CastleBlock.SubType.TOWER);
    }

    private void spawnCoreBlocks() {
        // spawn core blocks.
        for ( int x=0;x<W;x++)
        {
            int px = 13 + x * 13;

            for (int y=0;y<H;y++) {
                int py = 30 + y * 17;

                switch (castle[y][x]) {
                    case EMPTY:
                        break;
                    case WALL:
                        entityFactorySystem.createEntity("building-wall", px, py).addToWorld();
                        break;
                    case BARRACKS:
                        entityFactorySystem.createEntity("building-barracks", px, py).addToWorld();
                        break;
                    case TOWER:
                        entityFactorySystem.createEntity("building-tower", px, py).addToWorld();
                        break;
                }
            }
        }
    }
}
