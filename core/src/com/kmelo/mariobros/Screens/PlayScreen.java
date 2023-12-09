package com.kmelo.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kmelo.mariobros.MarioBros;
import com.kmelo.mariobros.Scenes.Hud;
import com.kmelo.mariobros.Sprites.Mario;

public class PlayScreen implements Screen {

    private MarioBros game;
    Texture texture;
    private OrthographicCamera gameView;
    private Viewport gamePortable;
    private Hud hud;

    // Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    private Mario player;

    public PlayScreen(MarioBros game) {
        this.game = game;

        gameView = new OrthographicCamera();
        gamePortable = new FitViewport((float) MarioBros.V_WIDTH / MarioBros.PPM, (float) MarioBros.V_HEIGHT / MarioBros.PPM, gameView);
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, (float) 1 / MarioBros.PPM);

        gameView.position.set(gamePortable.getWorldWidth() / 2, gamePortable.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -5), true);
        b2dr = new Box2DDebugRenderer();

        player = new Mario(this.world);

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
    @Override
    public void show() {
        
    }

    public void handleInput(float dt) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.b2body.applyLinearImpulse(new Vector2(0, 2.7f), player.b2body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) {
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2) {
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }
    }

    public void update(float dt) {
        handleInput(dt);

        world.step(1/60f, 6, 2);

        gameView.position.x = player.b2body.getPosition().x;

        gameView.update();
        renderer.setView(gameView);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        b2dr.render(world, gameView.combined);

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePortable.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
