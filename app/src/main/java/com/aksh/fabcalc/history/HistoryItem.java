package com.aksh.fabcalc.history;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;

import com.aksh.fabcalc.R;
import com.aksh.fabcalc.databinding.HistoryItemBinding;
import com.aksh.fabcalc.utils.ColorUtils;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aakarshit on 22-07-2017.
 */

public class HistoryItem extends AbstractItem<HistoryItem, HistoryItem.ViewHolder> {

    private String expression;
    private String result;

    public static List<HistoryItem> getHistoryList(Context context){
        List<HistoryItem> historyItemList = new ArrayList<>();

        CursorLoader cursorLoader = new CursorLoader(
                context,
                HistoryProvider.History.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        Cursor cursor = cursorLoader.loadInBackground();
        if (!cursor.moveToFirst()) {
            return historyItemList;
        }
        do {
          String expression = cursor.getString(cursor.getColumnIndex(HistoryContract.COLUMN_EXPRESSION));
          String result = cursor.getString(cursor.getColumnIndex(HistoryContract.COLUMN_RESULT));
          historyItemList.add(new HistoryItem(expression, result));
        } while (cursor.moveToNext());
        cursor.close();

        return historyItemList;
    }

    public HistoryItem(String expression, String result) {
        this.expression = expression;
        this.result = result;
    }

    public String getExpression() {
        return expression;
    }

    public String getResult() {
        return result;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(v.getContext());
        HistoryItemBinding itemBinding =
                HistoryItemBinding.inflate(layoutInflater, (ViewGroup) v.getParent(), false);
        return new ViewHolder(itemBinding);
//        return new ViewHolder(v);
    }

    @Override
    public void bindView(ViewHolder holder, List payloads) {
        super.bindView(holder, payloads);
        holder.bind(this);
        setZoomInAnimation(holder.itemView);
    }

    @Override
    public int getType() {
        return R.id.history_item_relative_layout;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.history_item;
    }

    private void setZoomInAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        HistoryItemBinding mHistoryItemBinding;

        public  ViewHolder(HistoryItemBinding binding) {
            super(binding.getRoot());
            mHistoryItemBinding = binding;
        }

        public void bind(HistoryItem item) {
            mHistoryItemBinding.setItem(item);
            mHistoryItemBinding.setCardColor(ColorUtils.currentColors.getSecondaryColor());
            mHistoryItemBinding.setTextColor(ColorUtils.currentColors.getTextColor());
            mHistoryItemBinding.executePendingBindings();
        }
    }
}
