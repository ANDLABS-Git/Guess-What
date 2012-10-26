package com.example.guesswhat;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.physics.PhysicsHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.util.Log;

import com.immersion.uhl.Launcher;

public class Level1 extends BaseGameActivity implements IOnSceneTouchListener {

	private static final float CAMERA_WIDTH = 480;
	private static final float CAMERA_HEIGHT = 800;
	private Camera mCamera;
	private Launcher m_launcher;
	private Rectangle pointer;

	protected void onPause() {
		// TODO Auto-generated method stub
		
		super.onPause();
		
		try{ 
			m_launcher.stop();
		}catch(RuntimeException e){
			Log.e("haptics"," error "+e.getLocalizedMessage());
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		try{
			m_launcher= new Launcher(this);
		}catch(RuntimeException e){
			Log.e("Haptics", "error "+e.getMessage());
		}
		
	}		
	
	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				this.mCamera)); 
	}

	@Override
	public void onLoadResources() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Scene onLoadScene() {
		Scene scene = new Scene(1);
		scene.setOnSceneTouchListener(this);

		Rectangle background = new Rectangle(100, 100, 100, 100) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
			m_launcher.play(Launcher.TICK_33);
						return true;
				

			};
		};
		
		pointer = new Rectangle(200, 200, 1, 1);
		background.setColor(100, 100, 100);
		scene.getLastChild().attachChild(background);
		scene.registerTouchArea(background);
		return scene;
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return false;
	}

}
