package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import net.mostlyoriginal.api.component.graphics.ColorAnimation;
import net.mostlyoriginal.api.component.script.Schedule;
import net.mostlyoriginal.game.component.Bobbing;
import net.mostlyoriginal.game.component.CastleBlock;
import net.mostlyoriginal.game.component.ExpansionPoint;
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
    private static final int BIRD_SPAWN_LEVEL = 3;
    public CastleBlock.Type castle[][] = new CastleBlock.Type[H][W];

    public boolean castleDirty = true;

    EntityFactorySystem entityFactorySystem;
    private int randomUsedX;

    ComponentMapper<CastleBlock> cm;

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
        if ( castleDirty) {
            if ( cm.get(e).fadeoutOnReplace )
            {
                e.addComponent(new Schedule().wait(1f).deleteFromWorld())
                        .addComponent(new ColorAnimation(new Color(1, 1, 1, 1), new Color(1, 1, 1, 0), Interpolation.linear, 1f, 1f))
                        .changedInWorld();
            } else {
                e.deleteFromWorld();
            }
        }
    }

    protected void assembleCastle()
    {
        spawnCoreBlocks();
        spawnTrimming();
        spawnExpansionPoints();
    }

    private void spawnTrimming() {

        // spawn trimming
        for ( int x=-1;x<=W;x++)
        {
            int px = 13 + x * 13;

            for (int y=-1;y<=H;y++) {
                int py = 30 + y * 17;

                // only spawn trimming on empty blocks.
                if ( isEmptyAt(x,y) || isTowerAt(x,y))
                {
                    // top wall trimming.
                    if ( isWallAt(x, y-1) ) entityFactorySystem.createEntity("building-trimming-on-wall", px, py).addToWorld();
                    else if ( isWallAt(x+1, y-1) && !isWallAt(x+1, y) ) entityFactorySystem.createEntity("building-trimming-on-wall-left", px, py).addToWorld();
                    else if ( isWallAt(x-1, y-1) && !isWallAt(x-1, y) ) entityFactorySystem.createEntity("building-trimming-on-wall-right", px, py).addToWorld();

                    // side wall trimming left side
                    if ( isWallAt(x+1, y) && isWallAt(x+1, y+1) ) entityFactorySystem.createEntity("building-trimming-bottom-wall-left", px, py).addToWorld();
                    else if ( isWallAt(x+1, y) ) entityFactorySystem.createEntity("building-trimming-top-wall-left", px, py).addToWorld();

                    // side wall trimming right side.
                    if ( isWallAt(x-1, y) && isWallAt(x-1, y+1) ) entityFactorySystem.createEntity("building-trimming-bottom-wall-right", px, py).addToWorld();
                    else if ( isWallAt(x-1, y) ) entityFactorySystem.createEntity("building-trimming-top-wall-right", px, py).addToWorld();

                    // tower spire.
                    if ( isTowerAt(x, y-1) && !isTowerAt(x,y) ) {
                        entityFactorySystem.createEntity("building-trimming-on-tower", px, py).addToWorld();
                        if ( y >= BIRD_SPAWN_LEVEL ) spawnBirdsOfDoom(px, py);
                    }

                    if ( (isWallAt(x, y-1) && !isWallAt(x, y)) || (isTowerAt(x,y-2) && !isTowerAt(x,y-1)))
                    {
                        entityFactorySystem.createEntity("building-flag", px, py).addToWorld();
                    }
                }
            }
        }
    }

    private void spawnBirdsOfDoom(int px, int py) {
        // spires need birds! provided they're high up enough
        for ( int i=0,s= MathUtils.random(1, 3);i<s;i++)
        {
            Bobbing bobbing = new Bobbing(10, 2, px + 6, py + MathUtils.random(15, 20), MathUtils.random(0.1f, 0.15f));
            bobbing.age = MathUtils.random(0f,1f);
            entityFactorySystem
                    .createEntity("bird")
                    .addComponent(bobbing)
                    .addToWorld();
        }
    }

    private void spawnExpansionPoints() {

        // spawn trimming
        for ( int x=0;x<W;x++)
        {
            int px = 13 + x * 13;

            for (int y=0;y<H;y++) {
                int py = 30 + y * 17;

                // only spawn trimming on empty blocks.
                if ( castle[y][x] == CastleBlock.Type.EMPTY || castle[y][x] == CastleBlock.Type.TOWER)
                {
                    if ( castle[y][x] == CastleBlock.Type.EMPTY )
                    {
                        boolean supportForWall  = y == 0 || isWallAt(x, y-1);
                        boolean supportForTower = y == 0 || isWallAt(x, y-1) || isTowerAt(x, y-1);

                        if ( supportForTower || supportForWall )
                        {
                            Entity e = entityFactorySystem.createEntity("building-hammer", px, py);
                            ExpansionPoint point = e.getComponent(ExpansionPoint.class);
                            point.x = x;
                            point.y = y;
                            point.allowWalls = supportForWall;
                            point.allowTowers = supportForTower;

                            e.addToWorld();
                        }
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

    private boolean isEmptyAt(int x, int y) {
        if ( x<0 || y<0 || x>=W || y>=H ) return true;
        return (castle[y][x].subType == CastleBlock.SubType.NONE);
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
                    case SPELUNKER:
                        entityFactorySystem.createEntity("building-spelunker", px, py).addToWorld();
                        break;
                    case TOWER:
                        entityFactorySystem.createEntity("building-tower", px, py).addToWorld();
                        break;
                }
            }
        }
    }

    public void setBlock(int x, int y, CastleBlock.Type type) {
        if ( x<0 || y<0 || x>=W || y>=H ) return;
        castleDirty=true;
        castle[y][x] = type;
    }

    /**
     * Not very efficient, but also not called often. ;)
     *
     * @return random X coordinate that touches the castle at floor level.
     */
    public int getRandomUsedX()
    {
        Array<Integer> usedX = new Array<Integer>();

        for ( int x=0;x<W;x++)
        {
            if ( castle[0][x] != CastleBlock.Type.EMPTY )
            {
                usedX.add(13 + x * 13 + MathUtils.random(0,13));
            }
        }
        Integer result = usedX.random();
        return result != null ? result : 50;
    }
}
