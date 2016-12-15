package com.jwang123.androidpaginatesample.paginate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.jwang123.androidpaginatesample.R;

/**
 * Created by wangjie on 15/12/2016.
 */

public abstract class PaginatableFragment extends Fragment implements Paginatable {
    private static final String ProgressBarTag = "progress_bar_tag";

    protected ProgressBar bottomRefreshView;
    protected abstract SwipeRefreshLayout getSwipeRefresh();

    @Override
    public void addRefresh() {
        bottomRefreshView = new ProgressBar(getContext());
    }

    @Override
    public void endRefreshing() {
        getSwipeRefresh().setRefreshing(false);
        endFullScreenRefresh();
    }

    @Override
    public void startFullScreenRefresh() {
        FrameLayout parent = new FrameLayout(getContext());
        parent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        parent.setTag(ProgressBarTag);

        ProgressBar bar = new ProgressBar(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        bar.setLayoutParams(layoutParams);

        parent.addView(bar);
        ((ViewGroup)getView()).addView(parent);
    }

    public void endFullScreenRefresh() {
        View bar = getView().findViewWithTag(ProgressBarTag);
        ((ViewGroup)getView()).removeView(bar);
    }

    @Override
    public void startBottomRefresh() {
        bottomRefreshView.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams params = bottomRefreshView.getLayoutParams();
        if (params != null) {
            params.height = (int) getResources().getDimension(R.dimen.bottom_refresh_height);
        }
    }

    @Override
    public void stopBottomRefresh() {
        bottomRefreshView.setVisibility(View.GONE);
        ViewGroup.LayoutParams params = bottomRefreshView.getLayoutParams();
        if (params != null) {
            params.height = 0;
        }
    }

    public abstract void invalidateElements();

    @Override
    public abstract ElementsGetter getElementsGetter();

    @Override
    public abstract void displayNoElementIfNeeded(boolean noElement);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addRefresh();
    }
}
