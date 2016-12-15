package com.jwang123.androidpaginatesample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wangjie on 15/12/2016.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    public List<Integer> items;
    private View bottomRefreshView;

    private static final int ITEM_VIEW = 0;
    private static final int BOTTOM_REFRESH_VIEW = 1;
    public ReachBottomListener reachBottomListener;


    public Adapter(List<Integer> items, View bottomRefreshView) {
        this.items = items;
        this.bottomRefreshView = bottomRefreshView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == BOTTOM_REFRESH_VIEW) {
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) parent.getResources().getDimension(R.dimen.bottom_refresh_height));
            bottomRefreshView.setLayoutParams(params);
            return new BottomViewHolder(bottomRefreshView);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder vh = new ItemViewHolder(view);
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < items.size()) {
            return ITEM_VIEW;
        }
        return BOTTOM_REFRESH_VIEW;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_VIEW) {
            ((ItemViewHolder) holder).textView.setText("row" + items.get(position));
            return;
        }
        if (reachBottomListener != null) {
            reachBottomListener.onReachBottom();
        }
    }

    @Override
    public int getItemCount() {
        return items.size()+1;
    }

    public static abstract class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class BottomViewHolder extends ViewHolder {

        public BottomViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class ItemViewHolder extends ViewHolder {

        TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }

    }

    interface ReachBottomListener {
        void onReachBottom();
    }
}
