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
import org.andengine.util.color.Color;
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

public class Level2 extends SimpleBaseGameActivity implements
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
		
		Log.i("contact","start ----");
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
					
//					Rectangle a =  (Rectangle) contact.getFixtureA().getBody().getUserData();
//					Rectangle b =  (Rectangle) contact.getFixtureB().getBody().getUserData();
					
					String aa =  (String) contact.getFixtureA().getBody().getUserData();
					String bb =  (String) contact.getFixtureB().getBody().getUserData();
					
					
//					Log.i("contact",a.getUserData()+":"+a.getX()+":"+a.getY()+"   -   "+b.getUserData()+":"+b.getX()+":"+b.getY()); // + "  ## "+aa + " == " + bb );
					
					if(!aa.equalsIgnoreCase("pointer")){
						if(aa.equalsIgnoreCase("A")){
							m_launcher.play(Launcher.TRIPLE_STRONG_CLICK_100);
						}else if(aa.equalsIgnoreCase("B")){
							m_launcher.play(Launcher.FAST_PULSE_33);
						}if(aa.equalsIgnoreCase("C")){
							m_launcher.play(Launcher.DOUBLE_SHARP_CLICK_66);
						}
					}
//					
//					
//				} 
//				m_launcher.play(Launcher.TEXTURE6);
			}
		});
		
		
	
		
		Rectangle colorA= new Rectangle(0, 0, CAMERA_WIDTH/3, 100, getVertexBufferObjectManager());
		colorA.setColor(Color.GREEN);
		Rectangle colorB= new Rectangle(CAMERA_WIDTH/3,0 , CAMERA_WIDTH/3, 100, getVertexBufferObjectManager());
		colorB.setColor(Color.BLUE);
		Rectangle colorC= new Rectangle(CAMERA_WIDTH/3*2,0 , CAMERA_WIDTH/3, 100, getVertexBufferObjectManager());
		colorC.setColor(Color.RED);
		
		colorA.setUserData("L");
		colorB.setUserData("B");
		colorC.setUserData("C");

		
		
		scene.attachChild(colorA);
		scene.attachChild(colorB);
		scene.attachChild(colorC);
		
		Body a= 	PhysicsFactory.createBoxBody(mPhysicsWorld, colorA, BodyType.StaticBody, mFixtureDef);
		Body c= 	PhysicsFactory.createBoxBody(mPhysicsWorld, colorB, BodyType.StaticBody, mFixtureDef);
		Body b= 	PhysicsFactory.createBoxBody(mPhysicsWorld, colorC, BodyType.StaticBody, mFixtureDef);
		
//		a.setUserData(colorA);
//		b.setUserData(colorB);
//		c.setUserData(colorC);
		
		a.setUserData("A");
		b.setUserData("B");
		c.setUserData("C");

		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				colorA, a, true, true));
		
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				colorB, b, true, true));	
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
						colorC, c, true, true));
		
		
		Rectangle shape1a= new Rectangle(50, 650,50,40, getVertexBufferObjectManager());
		shape1a.setColor(Color.GREEN);
		scene.attachChild(shape1a);
		shape1a.setColor(Color.RED);
		
		Rectangle shape2a= new Rectangle(200, 650, 50,40, getVertexBufferObjectManager());
		shape2a.setColor(Color.GREEN);
		scene.attachChild(shape2a);
		
		Rectangle shape3a= new Rectangle(350, 650, 50,50, getVertexBufferObjectManager());
		shape3a.setColor(Color.GREEN);
		scene.attachChild(shape3a);
		

		Sprite arrowTarget = new Sprite(270, 280,
				mArrow, getVertexBufferObjectManager());
		scene.attachChild(arrowTarget);
		arrowTarget.setScale(2);
		arrowTarget.setColor(Color.BLUE);
		

		Rectangle arrowTargetB= new Rectangle(50, 275, 175,170, getVertexBufferObjectManager());
		arrowTargetB.setColor(Color.RED);
		scene.attachChild(arrowTargetB);
		
		Body bodyarrowTarget= 	PhysicsFactory.createBoxBody(mPhysicsWorld, arrowTarget, BodyType.StaticBody, mFixtureDef);
		Body bodyarrowTargetB= 	PhysicsFactory.createBoxBody(mPhysicsWorld, arrowTargetB, BodyType.StaticBody, mFixtureDef);
		
		bodyarrowTarget.setUserData("A");
		bodyarrowTargetB.setUserData("C");

		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				arrowTarget, bodyarrowTarget, true, true));
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
				arrowTarget, bodyarrowTargetB, true, true));
		
		
		Sprite arrow = new Sprite(72, 595,
				mArrow, getVertexBufferObjectManager());
		scene.attachChild(arrow);
		arrow.setScale(0.5f);
		arrow.setColor(Color.BLUE);
		
		Sprite arrow2 = new Sprite(222, 595,
				mArrow, getVertexBufferObjectManager());
		scene.attachChild(arrow2);
		arrow2.setScale(0.5f);
		arrow2.setColor(Color.RED);
		
		Sprite arrow3 = new Sprite(327, 555,
				mArrow, getVertexBufferObjectManager());
		scene.attachChild(arrow3);
		arrow3.setScale(0.5f);
		arrow3.setRotation(-90);
		arrow3.setColor(Color.BLUE);
		
		
		
		
//
//		Sprite bubble = new Sprite(CAMERA_WIDTH / 3, CAMERA_HEIGHT
//				- mBubble.getHeight(), mBubble, getVertexBufferObjectManager());
//		bubble.setSize(CAMERA_WIDTH / 3 - 10, CAMERA_WIDTH / 3 - 10);
//		scene.attachChild(bubble);
//
//		Sprite star = new Sprite(CAMERA_WIDTH / 3 * 2, CAMERA_HEIGHT
//				- mStar.getHeight()+80, mStar, getVertexBufferObjectManager());
//		star.setSize(CAMERA_WIDTH / 3 - 10, CAMERA_WIDTH / 3 - 10);
//		scene.attachChild(star);

//		final PhysicsEditorLoader loader = new PhysicsEditorLoader();
//		try {
//			loader.load(this, mPhysicsWorld, "star_black.xml", blackstar, true,
//					true);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

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
					pointer.setUserData("pointer");
				}
			} else {
				pointer = new Rectangle(pSceneTouchEvent.getX(),
						pSceneTouchEvent.getY(), 1, 1,
						getVertexBufferObjectManager());
				pScene.attachChild(pointer);
				pointer.setUserData("pointer");
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
					PixelFormat.RGBA_8888, TextureOptions.BILINEAR, "arrowhead.png");

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