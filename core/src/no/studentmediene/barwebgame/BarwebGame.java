package no.studentmediene.barwebgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.java.Log;
import no.studentmediene.barwebgame.models.Cup;
import no.studentmediene.barwebgame.util.BeerPongInputProcessor;
import no.studentmediene.barwebgame.util.ProjectileEquation;
import no.studentmediene.barwebgame.util.SpriteSheetAnimation;

import java.util.Iterator;

@Data
@Log
@EqualsAndHashCode(callSuper = true)
public class BarwebGame extends ApplicationAdapter {
    private Screen currentScreen;

    private static final Color BACKGROUND_COLOR = new Color(
            0.39f, 0.58f,
            0.92f, 1.0f);
    private static final float WORLD_TO_SCREEN = 1.0f/100.0f;
    private static final float SCENE_WIDTH = 12.80f;
    private static final float SCENE_HEIGHT = 7.20f;

    private SpriteBatch batch;

    private TextureRegion currentThrowFrame, currentPowerBarFrame;

    SpriteSheetAnimation throwAnimation, powerBarAnimation;
    float throwingStateTime = 0f;

    Texture ball, mug;
    Sprite background;

    BeerPongInputProcessor inputProcessor;

    @Override
    public void create () {
        batch = new SpriteBatch();
        throwAnimation = new SpriteSheetAnimation(
                0.1f, 5, 1, new Texture("animation/throw.png"));
        throwAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        powerBarAnimation = new SpriteSheetAnimation(
                1, 8, 8, new Texture("animation/power-bar.png"));
        ball = new Texture("ball.png");
        mug = new Texture("mug.png");
        Texture background = new Texture("background.png");
        this.background = new Sprite(background);
        createCups();
        inputProcessor = new BeerPongInputProcessor();
        Gdx.input.setInputProcessor(inputProcessor);
    }

    private void createCups() {
        int x = 420;
        Array<Cup> cups = new Array<Cup>();
        for (int i = 0; i < 10; i++) {
            cups.add(new Cup(x, 50));
            x += 20;
        }
        this.cups = cups;
    }

    double ballAirTime = 0;

    float ballStartX = 160,
            ballStartY = 120,
            currentAngle = 45;

    @Override
    public void render () {
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g,
                BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (throwAnimation.isAnimationFinished(throwingStateTime)) {
            inputProcessor.throwing = false;
            throwingStateTime = 0;
            releaseBall();
        }

        if(inputProcessor.spacePressed) {
            changePowerLevel();
        }

        if (inputProcessor.throwing) {
            throwingStateTime += Gdx.graphics.getDeltaTime();
        }

        currentPowerBarFrame = powerBarAnimation.getKeyFrame((float) (currentPowerLevel - 0.5));
        currentThrowFrame = throwAnimation.getKeyFrame(throwingStateTime);

        batch.begin();
        background.draw(batch);
        batch.draw(new Texture("table.png"), 375, -90);
        batch.draw(currentPowerBarFrame, 45, 385);
        batch.draw(mug, 40, 375, 110, 65);
        if (inputProcessor.ballMoving) {
            updateBall();
        }
        for (Cup cup : cups) {
            double ratio = cup.getWidth()/20;
            batch.draw(cup, cup.getX(), cup.getY(), 20,
                    (float) (cup.getHeight()/ratio));
        }
        batch.draw(currentThrowFrame, 50, 50);
        batch.end();
    }

    Array<Cup> cups;

    private void updateBall() {
        ballAirTime += (10*Gdx.graphics.getDeltaTime());
        if (getCurrentBallY() > 50) {
            batch.draw(ball, getCurrentBallX(), getCurrentBallY(), 15, 15);
        }
        else {
            detectCupCollision();
            currentPowerLevel = 1;
            ballAirTime = 0;
            inputProcessor.ballMoving = false;
        }
    }

    int pointCounter = 0;

    private void detectCupCollision() {
        for (Iterator<Cup> iterator = cups.iterator(); iterator.hasNext();) {
            Cup cup = iterator.next();
            if (getCurrentBallX() > cup.getX()
                    && getCurrentBallX() < cup.getX() + 20) {
                iterator.remove();
            }
        }
    }

    private float getCurrentBallY() {
        return ballTrajectory.getY((float) ballAirTime);
    }

    private float getCurrentBallX() {
        return ballTrajectory.getX((float) ballAirTime);
    }

    ProjectileEquation ballTrajectory;

    private void releaseBall() {
        float totalSpeed = (float) (currentPowerLevel + 16);
        ballTrajectory = new ProjectileEquation(ballStartX, ballStartY,
                (float) (totalSpeed*Math.cos(currentAngle)),
                (float) (totalSpeed*Math.sin(currentAngle)));
        inputProcessor.ballMoving = true;
    }

    double currentPowerLevel = 1;

    private void changePowerLevel() {
        if (inputProcessor.powerRising) {
            currentPowerLevel++;
            if (currentPowerLevel >= 64)
                inputProcessor.powerRising = false;
        }
        else {
            currentPowerLevel--;
            if (currentPowerLevel <= 1)
                inputProcessor.powerRising = true;
        }
    }

    @Override
    public void dispose () {
        batch.dispose();
        throwAnimation.dispose();
        powerBarAnimation.dispose();
        ball.dispose();
    }
}
