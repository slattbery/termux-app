package com.termux.app.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.net.Uri;

public class BgManager {

	public static final String KEY_EN = "bg_en";
	public static final String KEY_OP = "bg_op";
	public static final String KEY_BL = "bg_bl";
	public static final String FILE_NM = "bg.png";

	public static void save_img(Context ctx, Uri uri) {
		try (InputStream in = ctx.getContentResolver().openInputStream(uri);
			OutputStream out = new FileOutputStream(get_file(ctx))) {
			byte[] buf = new byte[8192];
			int len;
			while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
		} catch (Exception ignored) {}
	}

	public static File get_file(Context ctx) {
		return new File(ctx.getFilesDir(), FILE_NM);
	}
}
