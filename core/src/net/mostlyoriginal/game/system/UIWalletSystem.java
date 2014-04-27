package net.mostlyoriginal.game.system;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.manager.EntityFactorySystem;
import net.mostlyoriginal.game.manager.FontManager;

/**
 * @author Daan van Yperen
 */
@Wire
public class UIWalletSystem extends VoidEntitySystem {

    public Color DARK_SCORE_COLOR;

    public int treasure = 100;
    private SpriteBatch batch = new SpriteBatch();
    private CameraSystem cameraSystem;
    FontManager fontManager;

    private EntityFactorySystem entityFactorySystem;
    private ComponentMapper<Bounds> bm;
    private ComponentMapper<Pos> pm;


    @Override
    protected void processSystem() {
        batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
        batch.begin();
        batch.setColor(1f, 1f, 1f, 1f);
        fontManager.font.setColor(1f,1f,1f,1f);
        String strTreasure = treasure + "$";
        fontManager.font.draw(batch, strTreasure, cameraSystem.getPixelWidth() - 2 -
                fontManager.font.getBounds(strTreasure).width, cameraSystem.getPixelHeight() - 2);

        batch.end();
    }

    public boolean pay(int cost) {
        boolean canAfford = canAfford(cost);
        if ( canAfford )
        {
            treasure -= cost;
        }
        return canAfford;
    }

    public boolean canAfford(int cost) {
        return cost <= treasure;
    }

    public void add(int gold) {
        treasure += gold;
    }

    public void add(int gold, Entity source) {
        add(gold);

        // spawn dancing coins! :D
        if ( pm.has(source) && bm.has(source) ) {

            int x = (int)(pm.get(source).x + bm.get(source).cx());
            int y = (int)(pm.get(source).y + bm.get(source).cy());

            for ( int i=0,s=Math.min(10,gold);i<s;i++) {
                entityFactorySystem.createEntity("particle-coin", x, y).addToWorld();
            }
        }
    }
}
