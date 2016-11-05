package no.studentmediene.barwebgame.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Håkon Meyer Tørnquist <haakon.t@gmail.com>
 *         Date: 05.11.2016 13.02.
 */
public class SpriteSheetAnimation extends Animation {

    private final Texture spriteSheet;

    public SpriteSheetAnimation(float frameDuration,
                                int frameColumns, int frameRows, Texture spriteSheet) {
        super(frameDuration, generateKeyFrames(spriteSheet, frameColumns, frameRows));

        this.spriteSheet = spriteSheet;
    }

    private static TextureRegion[] generateKeyFrames(Texture spriteSheet, int frameColumns, int frameRows) {
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet,
                spriteSheet.getWidth()/frameColumns,
                spriteSheet.getHeight()/frameRows);
        TextureRegion[] frames;
        if (frameRows == 1) frames = tmp[0];
        else {
            frames = new TextureRegion[frameColumns * frameRows];
            int index = 0;
            for (int i = 0; i < frameRows; i++) {
                for (int j = 0; j < frameColumns; j++) {
                    frames[index++] = tmp[i][j];
                }
            }
        }
        return frames;
    }

    public void dispose() {
        spriteSheet.dispose();
    }
}
