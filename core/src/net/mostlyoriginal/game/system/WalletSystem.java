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
public class WalletSystem extends VoidEntitySystem {

    public Color DARK_SCORE_COLOR;

    public float treasure = 10000;
    private SpriteBatch batch = new SpriteBatch();
    private CameraSystem cameraSystem;
    FontManager fontManager;

    @Override
    protected void processSystem() {
        batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
        batch.begin();
        batch.setColor(1f, 1f, 1f, 1f);
        fontManager.font.setColor(1f,1f,1f,1f);
        fontManager.font.draw(batch, treasure + "$", 2, cameraSystem.getPixelHeight() - 2);

        batch.end();
    }
}
