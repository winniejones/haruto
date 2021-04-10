package org.example.core.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import org.example.core.components.PositionComponent;
import org.example.core.factories.FontFactory;
import org.example.core.screens.MainGameScreen;
import org.example.core.ui.debug.DebugText;
import org.example.core.ui.debug.SystemExplorerWindow;
import org.example.core.utils.DebugUtil;
import org.example.core.utils.LogHelper;
import org.example.core.utils.Mappers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Set;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Line;
import static org.example.core.utils.CommonMath.angleTo;
import static org.example.core.utils.CommonMath.round;

public class DebugSystem extends IteratingSystem implements Disposable {

    private DebugConfig debugCFG;
    private SystemExplorerWindow engineView;
    private Box2DDebugRenderer debugRenderer;
    private BitmapFont fontSmall, fontLarge;

    //rendering
    private static OrthographicCamera cam;
    private SpriteBatch batch;
    private ShapeRenderer shape;

    //entity storage
    private Array<Entity> objects;

    //textures
    private Texture texCompBack = createTile(Color.GRAY);
    private Texture texCompSeparator = createTile(Color.RED);

    private Matrix4 projectionMatrix = new Matrix4();

    private static ArrayList<DebugText> debugTexts = new ArrayList<DebugText>();

    public DebugSystem() {
        super(Family.all(PositionComponent.class).get());
        debugCFG = new DebugConfig();
        fontSmall = FontFactory.createFont(FontFactory.fontBitstreamVM, 10);
        fontLarge = FontFactory.createFont(FontFactory.fontBitstreamVMBold, 20);
        batch = MainGameScreen.batch;
        cam = MainGameScreen.cam;
        shape = MainGameScreen.shape;
        objects = new Array<>();
        debugRenderer = new Box2DDebugRenderer(
                true,
                true,
                true,
                true,
                true,
                true
        );

        MainGameScreen.getStage().addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                super.keyDown(event, keycode);
                engineView.keyDown(event, keycode);
                return false;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                super.keyUp(event, keycode);
                return false;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                return false;
            }
        });
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engineView = new SystemExplorerWindow(getEngine());
    }

    @Override
    public void update(float delta) {
        engineView.update();

        updateKeyToggles();

        //don't update if we aren't drawing
        if (!debugCFG.drawDebugUI) return;
        super.update(delta);

        //set projection matrix so things render using correct coordinates
        projectionMatrix.setToOrtho2D(-8, -8, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setProjectionMatrix(projectionMatrix);
        shape.setProjectionMatrix(cam.combined);
        shape.begin(Line);
        shape.end();

        //draw diagnostic info
        int diagnosticX = 15;
        int diagnosticY = Gdx.graphics.getHeight() - 15;

        if (debugCFG.drawFPS) drawFPS(diagnosticX, diagnosticY);

        if (debugCFG.drawDiagnosticInfo) drawDiagnosticInfo(diagnosticX, diagnosticY);

        if (debugCFG.drawPos) drawPos();

        if (debugCFG.drawMousePos) drawMousePos();

        if (debugCFG.drawEntityList) drawEntityList();

        batch.begin();
        {

            //draw components on entity
            if (debugCFG.drawComponentList)
                drawComponentList();


            drawDebugTexts(batch);
        }
        batch.end();

        if (debugCFG.box2DDebugRender)
            debugRenderer.render(MainGameScreen.world, cam.combined);

        objects.clear();
    }

    private void updateKeyToggles() {
        if (Gdx.input.isKeyPressed(Keys.NUM_8)) {
            MainGameScreen.adjustGameTime(1000);
        }
        if (Gdx.input.isKeyPressed(Keys.NUM_9)) {
            MainGameScreen.adjustGameTime(-1000);
        }


        if (Gdx.input.isKeyJustPressed(Keys.F9)) {
            engineView.toggle();
        }

        if (Gdx.input.isKeyJustPressed(Keys.F2)) {
            MainGameScreen.getStage().setDebugAll(!MainGameScreen.getStage().isDebugAll());
        }

        //toggle debug
        if (Gdx.input.isKeyJustPressed(Keys.F3)) {
            debugCFG.drawDebugUI = !debugCFG.drawDebugUI;
            Gdx.app.log(this.getClass().getSimpleName(), "DEBUG UI: " + debugCFG.drawDebugUI);
        }

        //toggle pos
        if (Gdx.input.isKeyJustPressed(Keys.NUMPAD_0)) {
            debugCFG.drawPos = !debugCFG.drawPos;
            if (debugCFG.drawComponentList) {
                debugCFG.drawComponentList = false;
            }
            Gdx.app.log(this.getClass().getSimpleName(), "[debug] draw pos: " + debugCFG.drawPos);
        }

        //toggle components
        if (Gdx.input.isKeyJustPressed(Keys.NUMPAD_1)) {
            debugCFG.drawComponentList = !debugCFG.drawComponentList;
            if (debugCFG.drawPos) {
                debugCFG.drawPos = false;
            }
            Gdx.app.log(this.getClass().getSimpleName(), "[debug] draw component list: " + debugCFG.drawComponentList);
        }

        //toggle bounds
        if (Gdx.input.isKeyJustPressed(Keys.NUMPAD_2)) {
            debugCFG.box2DDebugRender = !debugCFG.box2DDebugRender;
            Gdx.app.log(this.getClass().getSimpleName(), "[debug] draw bounds: " + debugCFG.box2DDebugRender);
        }

        //toggle fps
        if (Gdx.input.isKeyJustPressed(Keys.NUMPAD_3)) {
            debugCFG.drawFPS = !debugCFG.drawFPS;
            Gdx.app.log(this.getClass().getSimpleName(), "[debug] draw FPS: " + debugCFG.drawFPS);
        }


        debugRenderer.setDrawBodies(debugCFG.drawBodies);
        debugRenderer.setDrawJoints(debugCFG.drawJoints);
        debugRenderer.setDrawAABBs(debugCFG.drawAABBs);
        debugRenderer.setDrawInactiveBodies(debugCFG.drawInactiveBodies);
        debugRenderer.setDrawVelocities(debugCFG.drawVelocities);
        debugRenderer.setDrawContacts(debugCFG.drawContacts);
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        objects.add(entity);
    }

    @Override
    public void dispose() {
        texCompBack.dispose();
        texCompSeparator.dispose();
        fontSmall.dispose();
        fontLarge.dispose();

        getEngine().removeEntityListener(engineView);
    }

    private static Texture createTile(Color c) {
        Pixmap pixmap;
        pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
        pixmap.setColor(c);
        pixmap.fill();

        Texture tex = new Texture(pixmap);
        pixmap.dispose();
        return tex;
    }

    /**
     * Draw position and speed of entity.
     */
    private void drawPos() {
        fontSmall.setColor(1, 1, 1, 1);
        for (Entity entity : objects) {
            PositionComponent t = Mappers.position.get(entity);

            //String vel = " ~ " + MyMath.round(t.velocity.len(), 1);
            String info = round(t.pos.x, 1) + "," + round(t.pos.y, 1);

            Vector3 screenPos = cam.project(new Vector3(t.pos.cpy(), 0));
            debugTexts.add(new DebugText(Integer.toHexString(entity.hashCode()), screenPos.x, screenPos.y));
            debugTexts.add(new DebugText(info, screenPos.x, screenPos.y - 10));
        }
    }

    private void drawMousePos() {
        int x = Gdx.input.getX();
        int y = Gdx.graphics.getHeight() - Gdx.input.getY();

        String localPos = x + "," + y;
        debugTexts.add(new DebugText(localPos, x, y));

//        Vector3 worldPos = cam.unproject(new Vector3(x, y, 0));
//        long seed = getSeed(worldPos.x, worldPos.y);
//        debugTexts.add(new DebugText((int) worldPos.x + "," + (int) worldPos.y + " (" + seed + ")", x, y + fontSmall.getLineHeight()));

        float angle = angleTo(Gdx.input.getX(), Gdx.input.getY(), Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        debugTexts.add(new DebugText(round(angle, 3) + " / " + round(angle * MathUtils.radDeg, 3), x, y + (int) fontSmall.getLineHeight() * 2));
    }

    public static void addDebugText(String text, float x, float y, boolean project) {
        addDebugText(text, (int) x, (int) y, project);
    }

    public static void addDebugText(String text, int x, int y, boolean project) {
        if (project) {
            Vector3 screenPos = cam.project(new Vector3(x, y, 0));
            x = (int) screenPos.x;
            y = (int) screenPos.y;
        }
        debugTexts.add(new DebugText(text, x, y));
    }

    private void drawDebugTexts(SpriteBatch batch) {
        for (DebugText t : debugTexts) {
            if (t.font == null) {
                fontSmall.setColor(t.color);
                fontSmall.draw(batch, t.text, t.x, t.y);
            } else {
                t.font.setColor(t.color);
                t.font.draw(batch, t.text, t.x, t.y);
            }
        }
        debugTexts.clear();
    }

    private void drawEntityList() {
        float fontHeight = fontSmall.getLineHeight();
        int x = 30;
        int y = 30;
        int i = 0;
        for (Entity entity : getEngine().getEntities()) {
            debugTexts.add(new DebugText(Integer.toHexString(entity.hashCode()), x, y + (fontHeight * i++)));
        }
    }

    private void drawFPS(int x, int y) {
        int fps = Gdx.graphics.getFramesPerSecond();
        Color fpsColor = fps >= 120 ? Color.SKY : fps > 45 ? Color.WHITE : fps > 30 ? Color.YELLOW : Color.RED;
        debugTexts.add(new DebugText(Integer.toString(fps), x, y, fpsColor, fontLarge));
    }

    private void drawDiagnosticInfo(int x, int y) {
        //camera position
        String camera = String.format("Pos: %s %s  Zoom:%3$.2f", (int) cam.position.x, (int) cam.position.y, cam.zoom);

        //memory
        String memory = DebugUtil.getMemory();

        //entity/component count
        String count = DebugUtil.getECSString(getEngine());

        //threads
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        String threads = "  Threads: " + threadSet.size();

        int sbCalls = MainGameScreen.batch.renderCalls;

        int linePos = 1;
        float lineHeight = fontLarge.getLineHeight();
        debugTexts.add(new DebugText(count, x, y - (lineHeight * linePos++), fontLarge));
        debugTexts.add(new DebugText(memory + threads, x, y - (lineHeight * linePos++), fontLarge));
        debugTexts.add(new DebugText(camera, x, y - (lineHeight * linePos++), fontLarge));
        debugTexts.add(new DebugText(
                "time: " + LogHelper.formatDuration(MainGameScreen.getGameTimeCurrent()) + " (" + MainGameScreen.getGameTimeCurrent() + ")",
                Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 10, fontLarge));

        for (Thread t : threadSet) {
            debugTexts.add(new DebugText(t.toString(), x, y - (lineHeight * linePos++)));
        }
    }

    /**
     * Draw all Entity components and fields.
     */
    private void drawComponentList() {
        float fontHeight = fontSmall.getLineHeight();
        int backWidth = 600;//width of background

        //fontSmall.setColor(1, 1, 1, 1);

        for (Entity entity : objects) {
            //get entities position and list of components
            PositionComponent position = Mappers.position.get(entity);
            ImmutableArray<Component> components = entity.getComponents();

            //use Vector3.cpy() to project only the position and avoid modifying projection matrix for all coordinates
            Vector3 screenPos = cam.project(new Vector3(position.pos.cpy(), 0));

            //calculate spacing and offset for rendering
            int fields = 0;
            for (Component c : components) {
                fields += c.getClass().getFields().length;
            }
            float yOffset = fontHeight * fields / 2;
            int curLine = 0;

            //draw all components/fields
            for (Component c : components) {
                //save component line to draw name
                float compLine = curLine;

                //draw all fields
                for (Field f : c.getClass().getFields()) {
                    float yOffField = screenPos.y - (fontHeight * curLine) + yOffset;
                    batch.draw(texCompBack, screenPos.x, yOffField, backWidth, -fontHeight);
                    try {
                        fontSmall.draw(batch, String.format("%-14s %s", f.getName(), f.get(c)), screenPos.x + 130, yOffField);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    curLine++;
                }

                //draw backing on empty components
                if (c.getClass().getFields().length == 0) {
                    batch.draw(texCompBack, screenPos.x, screenPos.y - (fontHeight * curLine) + yOffset, backWidth, -fontHeight);
                    curLine++;
                }

                //draw separating line
                batch.draw(texCompSeparator, screenPos.x, screenPos.y - (fontHeight * curLine) + yOffset, backWidth, 1);

                //draw component name
                float yOffComp = screenPos.y - (fontHeight * compLine) + yOffset;
                fontSmall.draw(batch, "[" + c.getClass().getSimpleName() + "]", screenPos.x, yOffComp);
            }
        }

    }
}
