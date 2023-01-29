// Generated by view binder compiler. Do not edit!
package com.raredev.stringeditor.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.textfield.TextInputEditText;
import com.raredev.stringeditor.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DialogStringBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextInputEditText textinputName;

  @NonNull
  public final TextInputEditText textinputValue;

  private DialogStringBinding(@NonNull LinearLayout rootView,
      @NonNull TextInputEditText textinputName, @NonNull TextInputEditText textinputValue) {
    this.rootView = rootView;
    this.textinputName = textinputName;
    this.textinputValue = textinputValue;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static DialogStringBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DialogStringBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.dialog_string, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DialogStringBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.textinput_name;
      TextInputEditText textinputName = ViewBindings.findChildViewById(rootView, id);
      if (textinputName == null) {
        break missingId;
      }

      id = R.id.textinput_value;
      TextInputEditText textinputValue = ViewBindings.findChildViewById(rootView, id);
      if (textinputValue == null) {
        break missingId;
      }

      return new DialogStringBinding((LinearLayout) rootView, textinputName, textinputValue);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
