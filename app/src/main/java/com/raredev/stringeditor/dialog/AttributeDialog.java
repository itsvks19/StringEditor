package com.raredev.stringeditor.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.raredev.stringeditor.callback.DialogCallback;
import com.raredev.stringeditor.databinding.DialogAttributeBinding;
import com.raredev.stringeditor.model.Attribute;
import com.raredev.stringeditor.utils.Validator;
import java.util.List;

public class AttributeDialog {
  private DialogAttributeBinding dialogBinding;
  private DialogCallback callback;
  private AlertDialog alertDialog;

  public AttributeDialog(Context context, List<Attribute> listString, int currentPosition, DialogCallback callback) {
    this.callback = callback;
    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
    dialogBinding = DialogAttributeBinding.inflate(((Activity)context).getLayoutInflater());
    builder.setView(dialogBinding.getRoot());

    final var currentName = currentPosition != -1 ? listString.get(currentPosition).getName() : "";
    final var positiveButtonText = currentPosition == -1 ? "Create" : "Save";

    builder.setPositiveButton(
        positiveButtonText,
        (dlg, i) ->
            callback.onPositiveButtonClicked(
                dialogBinding.textinputName.getText().toString(),
                dialogBinding.textinputValue.getText().toString()));
    builder.setNegativeButton("Cancel", (d, w) -> d.dismiss());

    alertDialog = builder.create();

    if (currentPosition != -1) {
      dialogBinding.textinputName.setText(listString.get(currentPosition).getName());
      dialogBinding.textinputValue.setText(listString.get(currentPosition).getValue());
    }
    TextWatcher textWatcher =
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}

          @Override
          public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
            checkNameAndValueErrors(
                listString,
                dialogBinding.textInputLayoutName,
                dialogBinding.textInputLayoutValue,
                currentName,
                dialogBinding.textinputName.getText().toString(),
                dialogBinding.textinputValue.getText().toString(),
                alertDialog);
          }

          @Override
          public void afterTextChanged(Editable p1) {}
        };

    dialogBinding.textinputName.addTextChangedListener(textWatcher);
    dialogBinding.textinputValue.addTextChangedListener(textWatcher);
  }
  
  public void setTitle(String title) {
    alertDialog.setTitle(title);
  }

  public void show() {
    alertDialog.show();
  }
  
  private void checkNameAndValueErrors(List<Attribute> listString, TextInputLayout nameLayout, TextInputLayout valueLayout, String currentName, String name, String value, AlertDialog dialog) {
    if (checkNameErrors(listString, nameLayout, currentName, name, dialog) && checkValueErrors(valueLayout, value, dialog)) {
      dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
      return;
    }
    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
  }

  private boolean checkNameErrors(List<Attribute> listString, TextInputLayout nameLayout, String currentName, String name, AlertDialog dialog) {
    if (!Validator.isValidStringName(name, currentName, listString)) {
      nameLayout.setErrorEnabled(true);
      nameLayout.setError("Invalid name!");
      return false;
    }
    nameLayout.setErrorEnabled(false);
    nameLayout.setError("");
    return true;
  }

  private boolean checkValueErrors(TextInputLayout valueLayout, String value, AlertDialog dialog) {
    if (!Validator.isValidStringValue(value)) {
      valueLayout.setErrorEnabled(true);
      valueLayout.setError("Invalid value!");
      return false;
    }
    valueLayout.setErrorEnabled(false);
    valueLayout.setError("");
    return true;
  }
}