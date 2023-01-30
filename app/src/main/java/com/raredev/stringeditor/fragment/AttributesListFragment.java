package com.raredev.stringeditor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.blankj.utilcode.util.ToastUtils;
import com.raredev.stringeditor.adapter.StringsAdapter;
import com.raredev.stringeditor.callback.DialogCallback;
import com.raredev.stringeditor.callback.ItemMoveCallBack;
import com.raredev.stringeditor.databinding.FragmentAttributesListBinding;
import com.raredev.stringeditor.dialog.AttributeDialog;
import com.raredev.stringeditor.model.Attribute;
import com.raredev.stringeditor.utils.XmlGeneratorUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class AttributesListFragment extends Fragment implements ItemMoveCallBack.ItemMoveListener {
  private FragmentAttributesListBinding binding;

  private List<Attribute> listString = new ArrayList<>();
  private StringsAdapter adapter;

  private DataObserver observer;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentAttributesListBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    init();
  }

  @Override
  public void onStart() {
    super.onStart();
    String code = XmlGeneratorUtils.getInstance().getGeneratedCode();
    if (code != null) {
      XmlGeneratorUtils.getInstance().xmlToList(code, listString);
      adapter.notifyDataSetChanged();
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    adapter.unregisterAdapterDataObserver(observer);
    binding = null;
  }
  
  @Override
  public boolean onItemMove(RecyclerView.ViewHolder holder, int fromPosition, int toPosition) {
    Collections.swap(listString, fromPosition, toPosition);
    adapter.notifyItemMoved(fromPosition, toPosition);
    return true;
  }

  @Override
  public void onDragFinish() {
    adapter.notifyDataSetChanged();
  }

  private void init() {
    adapter = new StringsAdapter(listString);
    observer = new DataObserver();

    ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemMoveCallBack(this));
    touchHelper.attachToRecyclerView(binding.listString);
    
    adapter.registerAdapterDataObserver(observer);
    adapter.setTouchHelper(touchHelper);

    binding.listString.setLayoutManager(new LinearLayoutManager(getContext()));
    binding.listString.setAdapter(adapter);

    binding.fab.setOnClickListener((v) -> dialogNewString());

    adapter.setStringListener(
        new StringsAdapter.StringListener() {
          @Override
          public void onStringClick(View v, int pos) {}

          @Override
          public void onStringLongClick(View v, int pos) {
            showPopupMenu(v, pos);
          }
        });
  }

  private void showPopupMenu(View v, int pos) {
    PopupMenu menu = new PopupMenu(requireActivity(), v);
    menu.getMenu().add(0, 0, 0, "Edit");
    menu.getMenu().add(0, 1, 1, "Remove");
    menu.setOnMenuItemClickListener(
        (item) -> {
          final var title = item.getTitle();
          if (title == "Edit") {
            dialogEditString(pos);
          } else if (title == "Remove") {
            listString.remove(pos);
            ToastUtils.showShort("Removed");
            adapter.notifyDataSetChanged();
          }
          return true;
        });
    menu.show();
  }

  private void dialogEditString(int pos) {
    AttributeDialog dialog = new AttributeDialog(requireActivity(), listString, pos,
      new DialogCallback() {
        @Override
        public void onPositiveButtonClicked(String name, String value) {
          listString.set(pos, new Attribute("string", name, value));
          adapter.notifyDataSetChanged();
        }
    });
    dialog.setTitle("Edit String");
    dialog.show();
  }

  private void dialogNewString() {
    AttributeDialog dialog = new AttributeDialog(requireActivity(), listString, -1,
      new DialogCallback() {
        @Override
        public void onPositiveButtonClicked(String name, String value) {
          listString.add(new Attribute("string", name, value));
          adapter.notifyDataSetChanged();
        }
    });
    dialog.setTitle("Create String");
    dialog.show();
  }

  class DataObserver extends RecyclerView.AdapterDataObserver {
    @Override
    public void onChanged() {
      super.onChanged();
      XmlGeneratorUtils.getInstance().generateXml(listString);
    }
  }

  public static AttributesListFragment newInstance() {
    AttributesListFragment fragment = new AttributesListFragment();
    fragment.setRetainInstance(true);
    return fragment;
  }
}