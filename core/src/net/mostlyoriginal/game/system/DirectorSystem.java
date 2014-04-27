package net.mostlyoriginal.game.system;

import com.artemis.annotations.Wire;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.manager.EntityFactorySystem;

/**
 * Spawns in monsters and treasures. Provides a scaling challenge.
 *
 *
 *
 *
 * @author Daan van Yperen
 */
@Wire
public class DirectorSystem  extends VoidEntitySystem {


    public float dungeonCooldown = 2;
    public float portalCooldown = 30;

    public float dungeonInterval = 25;
    public float portalInterval = 60;

    public boolean gameOver =false;

    private EntityFactorySystem entityFactory;

    @Override
    protected void processSystem() {

        if ( gameOver ) return;

        dungeonCooldown -= world.delta;
        if (dungeonCooldown <= 0)
        {
            dungeonCooldown = dungeonInterval;
            // slowly increase difficulty.

            if ( MathUtils.random(100) < 25)
            {
                entityFactory.createEntity("marker-gem", randomRadarX(), randomRadarY()).addToWorld();
            } else {
                entityFactory.createEntity("marker-dungeon", randomRadarX(), randomRadarY()).addToWorld();
            }
        }

        portalCooldown -= world.delta;
        if (portalCooldown <= 0)
        {
            portalCooldown = portalInterval;
            // slowly increase difficulty.
            portalInterval = MathUtils.clamp(portalInterval * 0.95f, 10, 9999);
            entityFactory.createEntity("marker-portal", randomRadarX(), randomRadarY()).addToWorld();
        }
    }

    private int randomRadarY() {
        return entityFactory.RADAR_Y + 26 + MathUtils.random(100);
    }

    private int randomRadarX() {
        return entityFactory.RADAR_X + 4 + MathUtils.random(23);
    }

    public void gameOver() {
        gameOver =true;
    }
}
