package com.raredev.stringeditor.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.raredev.stringeditor.R;
import com.raredev.stringeditor.StringModel;
import java.util.List;

public class StringsAdapter extends RecyclerView.Adapter<StringsAdapter.VH> {
  private List<StringModel> listStrings;
  private StringListener listener;

  public StringsAdapter(List<StringModel> listStrings) {
    this.listStrings = listStrings;
  }

  @Override
  public VH onCreateViewHolder(ViewGroup parent, int position) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.string_item, parent, false);
    return new VH(v);
  }

  @Override
  public void onBindViewHolder(VH holder, int position) {
    StringModel model = listStrings.get(position);

    holder.tvName.setText(model.getStringName());
    holder.tvValue.setText(model.getStringValue());

    holder.itemView.setOnClickListener(
        (v) -> {
          listener.onStringClick(v, position);
        });
    holder.itemView.setOnLongClickListener(
        (v) -> {
          listener.onStringLongClick(v, position);
          return true;
        });
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

  class VH extends RecyclerView.ViewHolder {
    TextView tvName, tvValue;

    public VH(View v) {
      super(v);
      tvName = v.findViewById(R.id.name);
      tvValue = v.findViewById(R.id.value);
    }
  }
}
