package xmicha65.bp_app.View;

import java.io.File;

import android.os.Environment;

/**
 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
 * 		mAlbumStorageDirFactory = new FroyoAlbumDirFactory(); // FIND THIS
 * } else {
 * 		mAlbumStorageDirFactory = new BaseAlbumDirFactory();
 * }
 */

public final class BaseAlbumDirFactory extends AlbumStorageDirFactory {

	// Standard storage location for digital camera files
	private static final String CAMERA_DIR = "/dcim/";

	@Override
	public File getAlbumStorageDir(String albumName) {
		return new File (
				Environment.getExternalStorageDirectory()
				+ CAMERA_DIR
				+ albumName
		);
	}
}
