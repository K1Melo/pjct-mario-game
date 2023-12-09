package com.kmelo.mariobros.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.kmelo.mariobros.MarioBros;
import com.kmelo.mariobros.Screens.PlayScreen;

import java.lang.reflect.Array;

public class Mario extends Sprite {
    public enum State { FALLING, JUMPING, STANDING, RUNNING }
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion marioStand;
    private Animation marioRun;
    private Animation marioJump;

    private float stateTimer;
    private boolean runningRight;

    public Mario(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = world;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        com.badlogic.gdx.utils.Array<TextureRegion> frames = new com.badlogic.gdx.utils.Array<>();
        for (int i = 1; i< 4; i++) {
            frames.add(new TextureRegion(getTexture(), i * 16, 0, 16, 16));
        }

        marioRun = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 4; i < 6; i++) {
            frames.add(new TextureRegion(getTexture(), i * 16, 0, 16, 16));
        }

        marioJump = new Animation(0.1f, frames);

        defineMario();

        marioStand = new TextureRegion(getTexture(), 0, 0, 16, 16);
        setBounds(0, 0, (float) 16 / MarioBros.PPM, (float) 16 / MarioBros.PPM);
        setRegion(marioStand);
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = (TextureRegion) marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioStand;
                break;
        }

        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        }
        else if (b2body.getLinearVelocity().y < 0) {
            return State.FALLING;
        }
        else if (b2body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

    public void  defineMario() {
        BodyDef bdef = new BodyDef();
        bdef.position.set((float) 32 / MarioBros.PPM,(float) 32 / MarioBros.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius((float) 6 / MarioBros.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2((float) -2 / MarioBros.PPM, (float) 7 /  MarioBros.PPM), new Vector2((float) 2 / MarioBros.PPM, (float) 7 /  MarioBros.PPM));
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData("head");
    }
}
