package net.mostlyoriginal.game.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.manager.AssetSystem;
import net.mostlyoriginal.game.manager.EntityFactorySystem;
import net.mostlyoriginal.game.manager.FontManager;

/**
 * @author Daan van Yperen
 */
@Wire
public class UITitleSystem extends VoidEntitySystem {

    private SpriteBatch batch = new SpriteBatch();
    private CameraSystem cameraSystem;
    FontManager fontManager;

    private EntityFactorySystem entityFactorySystem;
    private ComponentMapper<Bounds> bm;
    private ComponentMapper<Pos> pm;
    private AssetSystem assetSystem;
    public String title = "underkeep";
    public String sub1 = "the deep threat";
    public String sub2 = "is coming";
    public String sub3 = "";
    public float cooldown = 8;

    public void setMessage( String title, String sub1, String sub2, float cooldown )
    {
        this.title = title;
        this.sub1 = sub1;
        this.sub2 = sub2;
        this.cooldown = cooldown;
    }

    @Override
    protected void processSystem() {

        cooldown -= world.delta;
        float a = MathUtils.clamp(cooldown,0f,1f);

        batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
        batch.begin();
        batch.setColor(1f, 1f, 1f, a);
        fontManager.font.setColor(1f, 1f, 1f, a);
        fontManager.fontMedium.setColor(1f, 1f, 1f, a);
        fontManager.fontLarge.setColor(1f, 1f, 1f, a);

        float offsetX = cameraSystem.getPixelWidth() / 2 + 32;
        fontManager.fontLarge.draw(batch, title, offsetX -
                fontManager.fontLarge.getBounds(title).width / 2, cameraSystem.getPixelHeight() * 0.7f);

        if ( !sub1.equals("")) {
            fontManager.fontMedium.draw(batch, sub1, offsetX -
                    fontManager.fontMedium.getBounds(sub1).width / 2, cameraSystem.getPixelHeight() * 0.7f - 20);
        }

        if ( !sub2.equals("")) {
            fontManager.fontMedium.draw(batch, sub2, offsetX -
                    fontManager.fontMedium.getBounds(sub2).width / 2, cameraSystem.getPixelHeight() * 0.7f - 35);
        }

        if ( !sub3.equals("")) {
            fontManager.font.draw(batch, sub3, offsetX -
                    fontManager.font.getBounds(sub3).width / 2, cameraSystem.getPixelHeight() * 0.7f - 50);
        }

        batch.end();
    }
}
