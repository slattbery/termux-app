package com.termux.app.utils;

import java.io.File;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import androidx.preference.PreferenceManager;

public class BgRender {

  private static Bitmap cached_bmp = null;
  private static long last_modified = 0;

  public static void apply(ImageView imgView, View termView) {
    if (imgView == null)
      return;

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(imgView.getContext());
    boolean enabled = prefs.getBoolean(BgManager.KEY_EN, false);

    if (!enabled) {
      imgView.setVisibility(View.GONE);
      clear_cache();
      return;
    }

    File file = BgManager.get_file(imgView.getContext());
    if (!file.exists()) {
      imgView.setVisibility(View.GONE);
      clear_cache();
      return;
    }

    long modified = file.lastModified();
    if (cached_bmp == null || modified != last_modified) {
      clear_cache();
      BitmapFactory.Options opts = new BitmapFactory.Options();
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        opts.inPreferredConfig = Bitmap.Config.HARDWARE;
      }
      cached_bmp = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
      last_modified = modified;
    }

    if (cached_bmp == null)
      return;

    imgView.setImageBitmap(cached_bmp);
    imgView.setVisibility(View.VISIBLE);

    int op = prefs.getInt(BgManager.KEY_OP, 50);
    imgView.setAlpha(op / 100f);

    int bl = prefs.getInt(BgManager.KEY_BL, 0);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      if (bl > 0) {
        imgView.setRenderEffect(RenderEffect.createBlurEffect(bl, bl, Shader.TileMode.CLAMP));
      } else {
        imgView.setRenderEffect(null);
      }
    }

    if (termView != null) {
      termView.setBackgroundColor(0x00000000);
    }
  }

  public static void clear_cache() {
    cached_bmp = null;
    last_modified = 0;
  }
}
