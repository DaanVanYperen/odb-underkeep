package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.mouse.MouseCursor;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.manager.AssetSystem;
import net.mostlyoriginal.game.manager.EntityFactorySystem;
import net.mostlyoriginal.game.manager.FontManager;

/**
 * @author Daan van Yperen
 */
@Wire
public class UIHintSystem extends EntityProcessingSystem {

    private SpriteBatch batch = new SpriteBatch();
    private CameraSystem cameraSystem;
    FontManager fontManager;


    private EntityFactorySystem entityFactorySystem;
    private ComponentMapper<Bounds> bm;
    private ComponentMapper<Pos> pm;
    private AssetSystem assetSystem;
    public String hint = "";
    public float cooldown = 0.5f;


    public UIHintSystem() {
        super(Aspect.getAspectForAll(MouseCursor.class, Pos.class));
    }

    public void setMessage( String hint, float cooldown )
    {
        this.hint = hint;
        this.cooldown = cooldown;
    }

    @Override
    protected void process(Entity e) {
        cooldown -= world.delta;
        float a = MathUtils.clamp(cooldown*2f,0f,0.9f);

        Pos pos = pm.get(e);

        BitmapFont.TextBounds bounds = fontManager.font.getBounds(hint);

        float x=pos.x - (bounds.width/2);
        float y=pos.y - 10;

        // clamp to screen.
        if ( x + bounds.width >= cameraSystem.getPixelWidth() ) x = cameraSystem.getPixelWidth()-4-bounds.width;
        if ( y + bounds.height >= cameraSystem.getPixelHeight() ) x = cameraSystem.getPixelHeight()-4-bounds.height;
        if ( y < 0 ) y =0;
        if ( x < 0 ) x =0;

        batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
        batch.begin();
        batch.setColor(1f, 1f, 1f, a);
        batch.draw(assetSystem.get("hint-bg").getKeyFrame(0), x-2,y-2, (int)(bounds.width+4), (int)(bounds.height+4) );
        fontManager.font.setColor(1f, 1f, 1f, a);
        fontManager.font.draw(batch, hint, (int)x,(int)y + 5);

        batch.end();

    }
}
