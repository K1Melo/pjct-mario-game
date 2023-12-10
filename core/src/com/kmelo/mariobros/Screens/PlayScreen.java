package com.kmelo.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.kmelo.mariobros.Sprites.Brick;
import com.kmelo.mariobros.Sprites.Coin;
import com.kmelo.mariobros.Sprites.Mario;
import com.kmelo.mariobros.Tools.B2WorldCreator;
import com.kmelo.mariobros.Tools.WorldContactListener;

import static com.kmelo.mariobros.Sprites.Mario.State.FALLING;
import static com.kmelo.mariobros.Sprites.Mario.State.JUMPING;

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
    private TextureAtlas atlas;

    public PlayScreen(MarioBros game) {
        atlas = new TextureAtlas("Mario_and_Enimies.pack");

        this.game = game;

        gameView = new OrthographicCamera();
        gamePortable = new FitViewport((float) MarioBros.V_WIDTH / MarioBros.PPM, (float) MarioBros.V_HEIGHT / MarioBros.PPM, gameView);
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, (float) 1 / MarioBros.PPM);

        gameView.position.set(gamePortable.getWorldWidth() / 2, gamePortable.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(world, map);

        player = new Mario(world, this);

        world.setContactListener(new WorldContactListener());
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {
        
    }

    public void handleInput(float dt) {
        if((Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) && !(player.currentState == JUMPING || player.currentState == FALLING)) {
            player.b2body.applyLinearImpulse(new Vector2(0, 3.7f), player.b2body.getWorldCenter(), true);
        }

        if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) && player.b2body.getLinearVelocity().x <= 2) {
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        }

        if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) && player.b2body.getLinearVelocity().x >= -2) {
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }
    }

    public void update(float dt) {
        handleInput(dt);

        world.step(1/60f, 6, 2);

        player.update(dt);

        hud.update(dt);

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

        game.batch.setProjectionMatrix(gameView.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();

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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
