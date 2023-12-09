package com.kmelo.mariobros.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.kmelo.mariobros.MarioBros;

public class B2WorldCreator {
    public B2WorldCreator(World world, TiledMap map) {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for(int i = 2; i < 6; i++) {
            for(MapObject object : map.getLayers().get(i). getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / MarioBros.PPM, (rect.getY() + rect.getHeight() / 2) / MarioBros.PPM);

                body = world.createBody(bdef);

                shape.setAsBox((rect.getWidth() / 2) / MarioBros.PPM, (rect.getHeight() / 2) / MarioBros.PPM);
                fdef.shape = shape;
                body.createFixture(fdef);
            }
        }
    }
}
