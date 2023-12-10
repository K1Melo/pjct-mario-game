package com.kmelo.mariobros.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.kmelo.mariobros.MarioBros;
import com.kmelo.mariobros.Scenes.Hud;
import com.kmelo.mariobros.Screens.PlayScreen;

public class Brick extends InteractiveTileObject{
    public Brick(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(MarioBros.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        setCategoryFilter(MarioBros.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(50);
        MarioBros.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
    }
}
