package no.studentmediene.barwebgame.models;

import com.badlogic.gdx.graphics.Texture;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Håkon Meyer Tørnquist <haakon.t@gmail.com>
 *         Date: 05.11.2016 18.31.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Cup extends Texture {
    public Cup(int x, int y) {
        super("cup.png");
        this.x = x;
        this.y = y;
    }

    private int x, y;

    public static final float WIDTH = 20, HEIGHT = 30;
}
