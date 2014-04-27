package net.mostlyoriginal.game.system;

import com.artemis.annotations.Wire;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.manager.FontManager;

/**
 * @author Daan van Yperen
 */
@Wire
public class UIWalletSystem extends VoidEntitySystem {

    public Color DARK_SCORE_COLOR;

    public int treasure = 30;
    private SpriteBatch batch = new SpriteBatch();
    private CameraSystem cameraSystem;
    FontManager fontManager;

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
}
