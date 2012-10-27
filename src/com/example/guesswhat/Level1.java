package com.example.guesswhat;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.PixelFormat;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andlabs.andengine.extension.physicsloader.PhysicsEditorLoader;

import android.content.Context;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.immersion.uhl.Launcher;

public class Level1 extends SimpleBaseGameActivity implements
		IOnSceneTouchListener {

	private static final float CAMERA_WIDTH = 480;
	private static final float CAMERA_HEIGHT = 800;
	private Camera mCamera;
	private Launcher m_launcher;
	private Rectangle pointer;
	private PhysicsWorld mPhysicsWorld;
	private FixtureDef mFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0,
			true);
	private TextureRegion mStarBlack;
	private TextureRegion mStar;
	private TextureRegion mBubble;
	private TextureRegion mArrow;

	protected void onPause() {
		// TODO Auto-generated method stub

		super.onPause();

		try {
			m_launcher.stop();
		} catch (RuntimeException e) {
			Log.e("haptics", " error " + e.getLocalizedMessage());
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		try {
			m_launcher = new Launcher(this);
		} catch (RuntimeException e) {
			Log.e("Haptics", "error " + e.getMessage());
		}

	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,
				new FillResolutionPolicy(), mCamera);

	}

	@Override
	public Scene onCreateScene() {
		Scene scene = new Scene();
		scene.setOnSceneTouchListener(this);

		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
		scene.registerUpdateHandler(this.mPhysicsWorld);

		scene.registerUpdateHandler(this.mPhysicsWorld);

		this.mPhysicsWorld.setContactListener(new ContactListener() {

			boolean mOnSurface = false;
			
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}

			@Override
			public void endContact(Contact contact) {
//				m_launcher.stop();
//				m_launcher.play(Launcher.TICK_100);
//				mOnSurface = false;
			}

			@Override
			public void beginContact(Contact contact) {
//				if(!mOnSurface) {
					m_launcher.play(Launcher.TICK_100);
//					mOnSurface = true;
//				} 
//				m_launcher.play(Launcher.TEXTURE6);
			}
		});

		Sprite blackstar = new Sprite(CAMERA_WIDTH / 2 - mStarBlack.getWidth()
				/ 2, CAMERA_HEIGHT / 2 - mStarBlack.getHeight() / 2 - 80,
				mStarBlack, getVertexBufferObjectManager());
		scene.attachChild(blackstar);

		Sprite arrow = new Sprite(0, CAMERA_HEIGHT - mArrow.getHeight(),
				mArrow, getVertexBufferObjectManager());
		arrow.setSize(CAMERA_WIDTH / 3 - 10, CAMERA_WIDTH / 3 - 10);
		scene.attachChild(arrow);

		Sprite bubble = new Sprite(CAMERA_WIDTH / 3, CAMERA_HEIGHT
				- mBubble.getHeight(), mBubble, getVertexBufferObjectManager());
		bubble.setSize(CAMERA_WIDTH / 3 - 10, CAMERA_WIDTH / 3 - 10);
		scene.attachChild(bubble);

		Sprite star = new Sprite(CAMERA_WIDTH / 3 * 2, CAMERA_HEIGHT
				- mStar.getHeight(), mStar, getVertexBufferObjectManager());
		star.setSize(CAMERA_WIDTH / 3 - 10, CAMERA_WIDTH / 3 - 10);
		scene.attachChild(star);

		final PhysicsEditorLoader loader = new PhysicsEditorLoader();
		try {
			loader.load(this, mPhysicsWorld, "star_black.xml", blackstar, true,
					true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return scene;
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.isActionUp()) {
			m_launcher.stop();
		} else {
			if (pointer != null) {
				pointer.setPosition(pSceneTouchEvent.getX(),
						pSceneTouchEvent.getY());

				final PhysicsConnector physicsConnector = this.mPhysicsWorld
						.getPhysicsConnectorManager()
						.findPhysicsConnectorByShape((IAreaShape) pointer);

				this.mPhysicsWorld.unregisterPhysicsConnector(physicsConnector);
				if (physicsConnector != null) {
					Body body = physicsConnector.getBody();

					final float angle = body.getAngle();
					final Vector2 v2 = Vector2Pool.obtain(
							(pSceneTouchEvent.getX() - CAMERA_WIDTH / 2) / 32,
							(pSceneTouchEvent.getY() - CAMERA_HEIGHT / 2) / 32);
					body.setTransform(v2, angle);
					Vector2Pool.recycle(v2);
				}
			} else {
				pointer = new Rectangle(pSceneTouchEvent.getX(),
						pSceneTouchEvent.getY(), 1, 1,
						getVertexBufferObjectManager());
				pScene.attachChild(pointer);
			}

			Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, pointer,
					BodyType.DynamicBody, mFixtureDef);
			body.setUserData("pointer");
			this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
					pointer, body, true, true));

		}
		return false;
	}

	@Override
	protected void onCreateResources() {
		try {
			this.mStarBlack = loadResource(this, getTextureManager(),
					PixelFormat.RGBA_8888, TextureOptions.BILINEAR,
					"star_black.png");
			this.mStar = loadResource(this, getTextureManager(),
					PixelFormat.RGBA_8888, TextureOptions.BILINEAR, "star.png");
			this.mBubble = loadResource(this, getTextureManager(),
					PixelFormat.RGBA_8888, TextureOptions.BILINEAR,
					"bubble.png");
			this.mArrow = loadResource(this, getTextureManager(),
					PixelFormat.RGBA_8888, TextureOptions.BILINEAR, "arrow.png");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static TextureRegion loadResource(final Context pContext,
			final TextureManager pTextureManager, final PixelFormat pFormat,
			final TextureOptions pOptions, final String pPath)
			throws IOException {
		final BitmapTexture texture = new BitmapTexture(pTextureManager,
				new IInputStreamOpener() {
					@Override
					public InputStream open() throws IOException {
						return pContext.getAssets().open(pPath);
					}
				}, BitmapTextureFormat.fromPixelFormat(pFormat), pOptions);

		texture.load();

		return TextureRegionFactory.extractFromTexture(texture);
	}
}