package net.mostlyoriginal.game.manager;

import com.artemis.Manager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Manages fonts.
 *
 * @author Daan van Yperen
 */
public class FontManager extends Manager {

    public BitmapFont font;
    public BitmapFont fontLarge;
    public BitmapFont fontMedium;

    @Override
    protected void initialize() {
        font = new BitmapFont(Gdx.files.internal("5x5.fnt"), false);
        font.setColor(0, 0, 0, 1f);

        fontMedium = new BitmapFont(Gdx.files.internal("5x5.fnt"), false);
        fontMedium.setScale(2);
        fontMedium.setColor(0, 0, 0, 0.9f);
        fontLarge = new BitmapFont(Gdx.files.internal("5x5.fnt"), false);
        fontLarge.setScale(3);
        fontLarge.setColor(0, 0, 0, 0.9f);
    }
}
