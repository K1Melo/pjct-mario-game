package com.kmelo.mariobros.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.kmelo.mariobros.MarioBros;

public class Mario extends Sprite {
    public World world;
    public Body b2body;

    public Mario(World world) {
        this.world = world;
        defineMario();
    }

    public void  defineMario() {
        BodyDef bdef = new BodyDef();
        bdef.position.set((float) 32 / MarioBros.PPM,(float) 32 / MarioBros.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius((float) 5 / MarioBros.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }
}