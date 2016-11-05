package no.studentmediene.barwebgame.util;

import com.badlogic.gdx.math.Vector2;

/**
 * @author Håkon Meyer Tørnquist <haakon.t@gmail.com>
 *         Date: 05.11.2016 15.54.
 */

public class ProjectileEquation {

    private final Vector2 startVelocity;
    private final Vector2 startPoint;

    public ProjectileEquation(float startX, float startY,
                              float startVelocityX, float startVelocityY) {
        this.startPoint = new Vector2(startX, startY);
        this.startVelocity = new Vector2(startVelocityX, startVelocityY);
    }

    public float getX(float t) {
        return startVelocity.x * t + startPoint.x;
    }

    public float getY(float t) {
        return 0.5f * -9.81f * t * t + startVelocity.y * t + startPoint.y;
    }
}
