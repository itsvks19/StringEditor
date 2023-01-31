package com.raredev.stringeditor.adapter;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.raredev.stringeditor.R;
import com.raredev.stringeditor.databinding.StringItemBinding;
import com.raredev.stringeditor.model.Attribute;
import java.util.List;

public class StringsAdapter extends RecyclerView.Adapter<StringsAdapter.VH> {
  private List<Attribute> listStrings;
  
  private ItemTouchHelper touchHelper;
  private StringListener listener;
  
  public StringsAdapter(List<Attribute> listStrings) {
    this.listStrings = listStrings;
  }

  @Override
  public VH onCreateViewHolder(ViewGroup parent, int position) {
    return new VH(StringItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
  }

  @Override
  public void onBindViewHolder(VH holder, int position) {
    Attribute model = listStrings.get(position);

    holder.tvName.setText(model.getName());
    holder.tvValue.setText(model.getValue());

    holder.itemView.setOnClickListener((v) -> listener.onStringClick(v, position));
    holder.itemView.setOnLongClickListener(
        (v) -> {
          listener.onStringLongClick(v, position);
          return true;
        });

    holder.imgDrag.setOnTouchListener(
        (v, event) -> {
          if (event.getActionMasked() == MotionEvent.ACTION_DOWN) touchHelper.startDrag(holder);
          return false;
        });
  }

  public void setTouchHelper(ItemTouchHelper touchHelper) {
    this.touchHelper = touchHelper;
  }

  public void setStringListener(StringListener listener) {
    this.listener = listener;
  }

  public interface StringListener {
    void onStringClick(View v, int pos);
    void onStringLongClick(View v, int pos);
  }

  @Override
  public int getItemCount() {
    return listStrings.size();
  }

  public class VH extends RecyclerView.ViewHolder {
    public StringItemBinding binding;
    public TextView tvName, tvValue;
    public ImageView imgDrag;

    public VH(@NonNull StringItemBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
      tvName = binding.name;
      tvValue = binding.value;
      imgDrag = binding.iconDrag;
    }
  }
}
