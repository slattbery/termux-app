package com.termux.app.fragments.settings;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.termux.R;
import com.termux.app.utils.BgManager;

public class CustomizationPreferencesFragment extends PreferenceFragmentCompat {

  private ActivityResultLauncher<Intent> picker;

  @Override
  public void onCreatePreferences(Bundle saved, String root) {
    setPreferencesFromResource(R.xml.customization_preferences, root);

    File img = BgManager.get_file(requireContext());
    update_picked_bg_path(img.exists() ? img.getAbsolutePath() : "None");

    Preference pick = findPreference("bg_pick");
    if (pick != null) {
      pick.setOnPreferenceClickListener(p -> {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        picker.launch(i);

        return true;
      });
    }
  }

  @Override
  public void onAttach(Context ctx) {
    super.onAttach(ctx);
    picker = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        res -> {
          if (res.getResultCode() == Activity.RESULT_OK && res.getData() != null) {
            Uri uri = res.getData().getData();
            BgManager.save_img(ctx, uri);
            update_picked_bg_path(uri.toString());
          }
        });
  }

  private void update_picked_bg_path(String path) {
    Preference pick_path = findPreference("bg_pick_path");
    if (pick_path != null) {
      pick_path.setSummary(path);
    }
  }
}
