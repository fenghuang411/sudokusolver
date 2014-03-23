package iit.fenghuang.sudokusolver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.googlecode.tesseract.android.TessBaseAPI;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MyCameraDemo extends Activity {
	SurfaceHolder holder = null;
	SurfaceHolder holderLine = null;
	SurfaceView surface = null;
	SurfaceView surfaceLine = null;
	Camera cam = null;
	Button but = null;
	int unit_size;
	Canvas myCanvas;
	Paint myPaint;
	boolean previewRunning = true;
	static int image_rotate = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.main_);
		but = (Button) findViewById(R.id.ocr_butten1);
		but.setOnClickListener(new myOnClickListener());
		surface = (SurfaceView) findViewById(R.id.surfaceView1);
		holder = surface.getHolder();
		holder.addCallback(new MySurfaceViewCallback());
		surfaceLine = (SurfaceView) findViewById(R.id.surfaceView2);
		surfaceLine.setZOrderOnTop(true);
		holderLine = surfaceLine.getHolder();
		holderLine.setFormat(PixelFormat.TRANSLUCENT);
		holderLine.addCallback(new MySurfaceViewCallback2());
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		unit_size = dm.widthPixels/11;
		try {
			copyTessData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void drawOnSurface(String puzzle){
		myCanvas = holderLine.lockCanvas();  
        myCanvas.drawColor(Color.TRANSPARENT);  
        myPaint = new Paint();  
        myPaint.setAntiAlias(true);  
        myPaint.setColor(Color.GREEN);  
        myPaint.setStyle(Style.FILL_AND_STROKE);  
        myPaint.setStrokeWidth(5);
        for (int i = 1; i <= 10; i++){
        	myCanvas.drawLine(unit_size, unit_size*i, unit_size*10, unit_size*i, myPaint);
        	myCanvas.drawLine(unit_size*i, unit_size, unit_size*i, unit_size*10, myPaint);
        }
        myPaint.setTextSize(50);
        myPaint.setStyle(Style.FILL);
        for (int i = 1; i <= 81; i++){
        	if (puzzle.charAt(i) != '0')
        		myCanvas.drawText(""+puzzle.charAt(i), ((i-1)%9+1)*unit_size+unit_size/4, ((i-1)/9+1)*unit_size+unit_size, myPaint);
        }
        holderLine.unlockCanvasAndPost(myCanvas);  
	}
	private class MySurfaceViewCallback2 implements SurfaceHolder.Callback{
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			myCanvas = holder.lockCanvas();  
	        myCanvas.drawColor(Color.TRANSPARENT);  
	        myPaint = new Paint();  
	        myPaint.setAntiAlias(true);  
	        myPaint.setColor(Color.GREEN);  
	        myPaint.setStyle(Style.STROKE);  
	        myPaint.setStrokeWidth(5);
	        for (int i = 1; i <= 10; i++){
	        	myCanvas.drawLine(unit_size, unit_size*i, unit_size*10, unit_size*i, myPaint);
	        	myCanvas.drawLine(unit_size*i, unit_size, unit_size*i, unit_size*10, myPaint);
	        }
	        myPaint.setTextSize(40);
	        myPaint.setStrokeWidth(2);
	        myCanvas.drawText("Recognition percentage needs luck", unit_size, unit_size*12, myPaint);
	        myCanvas.drawText("Check and right the wrongs later", unit_size, unit_size*13, myPaint);
	        holder.unlockCanvasAndPost(myCanvas);  
		}
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			
		}
	}
	private class MySurfaceViewCallback implements SurfaceHolder.Callback{
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
		}
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			MyCameraDemo.this.cam = Camera.open(0);
			WindowManager manager = (WindowManager) MyCameraDemo.this.getSystemService(Context.WINDOW_SERVICE);
			Display display = manager.getDefaultDisplay();
			 Point size = new Point();
			    display.getSize(size);
			Camera.Parameters parameters = cam.getParameters();
			parameters.setPictureFormat(ImageFormat.JPEG);
			MyCameraDemo.this.cam.setParameters(parameters);
			setCameraDisplayOrientation(MyCameraDemo.this, 0, MyCameraDemo.this.cam);
			try {
				MyCameraDemo.this.cam.setPreviewDisplay(MyCameraDemo.this.holder);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MyCameraDemo.this.cam.startPreview();
			MyCameraDemo.this.previewRunning = true;
		}
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			if (MyCameraDemo.this.cam != null){
				if (MyCameraDemo.this.previewRunning){
					MyCameraDemo.this.cam.stopPreview();
					MyCameraDemo.this.previewRunning = false;
				}
				MyCameraDemo.this.cam.release();
			}
		}
	}
	private    Camera.PictureCallback jpgcall = new    Camera.PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			
			Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
			Matrix mat = new Matrix();
	        mat.postRotate(image_rotate);
	        Bitmap correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
	        Bitmap[] bmp_units = new Bitmap[82];
	        bmp_units[0] = null;
	        int unit_size = correctBmp.getWidth()/11; // the screen and bitmap may differ from size, dont knwo why
	
	        for (int i = 1; i <= 81; i++){
	        	bmp_units[i] = Bitmap.createBitmap(correctBmp,((i-1)%9+1)*unit_size+(int)(unit_size*0.2), ((i-1)/9+1)*unit_size-(int)(unit_size*0.05*((i-1)/9+1)), (int)(unit_size*0.6), (int)(unit_size*0.8));
	        }
//	        String[] fns = new String[82];
//	        File[] fs = new File[82];
//	        BufferedOutputStream bos = null;
//	        try {
//	        	for (int i = 1; i <= 81; i++){
//	        		fns[i] = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"CameraTest"+File.separator+"piece"+i+".jpg";
//	        		fs[i] = new File(fns[i]);
//	        		if (!fs[i].getParentFile().exists())
//	        			fs[i].getParentFile().mkdirs();
//
//	        		bos = new BufferedOutputStream(new FileOutputStream(fs[i]));
//
//	        		bmp_units[i].compress(Bitmap.CompressFormat.JPEG, 80, bos);
//	        		bos.flush();
//
//	        	}
//	        	bos.close();
//	        } catch (IOException e) {
//	        	// TODO Auto-generated catch block
//	        	e.printStackTrace();
//	        }
			
			TessBaseAPI baseApi = new TessBaseAPI();
			
			baseApi.init(Environment.getExternalStorageDirectory().getPath()+File.separator+"sudokusolver"+File.separator+"tesseract", "ukr");
			baseApi.setVariable("tessedit_char_whitelist", "123456789");
			char[] result = new char[82];
			result[0] = 'C';
			for (int i = 1; i <= 81; i++){
				baseApi.setImage(bmp_units[i]);
				String recognizedText = baseApi.getUTF8Text();
				result[i] = getCharFromOCR(recognizedText);
			}
			baseApi.end();
	        drawOnSurface(String.valueOf(result));
			MyCameraDemo.this.cam.stopPreview();
			
			MyCameraDemo.this.getIntent().putExtra("retmsg",String.valueOf(result) );
			MyCameraDemo.this.setResult(RESULT_OK, MyCameraDemo.this.getIntent());
			MyCameraDemo.this.finish();
			
		}
	};
	private char getCharFromOCR(String line){
		if (line.isEmpty())
			return '0';
		for (int i = 0; i < line.length(); i++)
			if (line.charAt(i)>='1' && line.charAt(i)<= '9')
				return line.charAt(i);
		return '0';
	}
	private class myOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (MyCameraDemo.this.cam != null){
				MyCameraDemo.this.cam.autoFocus(new myAutoFocus());
			}
		}
	}
	private class myAutoFocus implements AutoFocusCallback{

		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			// TODO Auto-generated method stub
			if (success){
				MyCameraDemo.this.cam.takePicture(sc, pc, jpgcall);
			}
			else {
				
			}
		}
		
	}
	private ShutterCallback sc = new ShutterCallback() {
		
		@Override
		public void onShutter() {
			// TODO Auto-generated method stub
			
		}
	};
	private Camera.PictureCallback pc = new Camera.PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
		// this is not a place to implement, should be in jpgcall
		}
	};
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.my_camera_demo, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//
//	/**
//	 * A placeholder fragment containing a simple view.
//	 */
//	public static class PlaceholderFragment extends Fragment {
//
//		public PlaceholderFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_my_camera_demo,
//					container, false);
//			return rootView;
//		}
//	}
	// this is official solution from camera mis-oriented, for image processing, image_rotate should be updated here
	public static void setCameraDisplayOrientation(Activity activity,
	        int cameraId, android.hardware.Camera camera)
	{
	    android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
	    android.hardware.Camera.getCameraInfo(cameraId, info);
	    int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
	    int degrees = 0;
	    switch (rotation)
	    {
	    case Surface.ROTATION_0:
	        degrees = 0;
	        break;
	    case Surface.ROTATION_90:
	        degrees = 90;
	        break;
	    case Surface.ROTATION_180:
	        degrees = 180;
	        break;
	    case Surface.ROTATION_270:
	        degrees = 270;
	        break;
	    }

	    int result;
	    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
	    {
	        result = (info.orientation + degrees) % 360;
	        result = (360 - result) % 360; // compensate the mirror
	    }
	    else
	    { // back-facing
	        result = (info.orientation - degrees + 360) % 360;
	    }
	    camera.setDisplayOrientation(result);
	    image_rotate = result;
	}
	private void copyTessData() throws IOException{
		String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"sudokusolver"+File.separator+"tesseract"+File.separator+"tessdata"+File.separator+"ukr.traineddata";
		File file = new File(filePath);
		InputStream in = null;
		OutputStream out = null;
		if (!file.exists()){
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			in = getAssets().open("ukr.traineddata");
			out = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int read;
			while((read = in.read(buffer)) != -1){
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		}
	}
}
