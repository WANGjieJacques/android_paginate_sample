package com.jwang123.androidpaginatesample;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jwang123.androidpaginatesample.R;
import com.jwang123.androidpaginatesample.paginate.Paginatable;
import com.jwang123.androidpaginatesample.paginate.PaginatableFragment;
import com.jwang123.androidpaginatesample.paginate.PaginatePresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends PaginatableFragment {

    private PaginatePresenter<Integer> presenter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Adapter adapter;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PaginatePresenter<>(this);
        presenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.pullDownRefresh();
            }
        });
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter(Collections.<Integer>emptyList(), bottomRefreshView);
        adapter.reachBottomListener = new Adapter.ReachBottomListener() {
            @Override
            public void onReachBottom() {
                presenter.loadNextPage();
            }
        };
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.fullScreenRefresh();
    }

    @Override
    protected SwipeRefreshLayout getSwipeRefresh() {
        return swipeRefreshLayout;
    }

    @Override
    public void invalidateElements() {
        adapter.items = presenter.elements;
        adapter.notifyDataSetChanged();
    }

    @Override
    public ElementsGetter getElementsGetter() {
        return new ElementsGetter() {
            @Override
            public void getElements(final int page, final SuccessListener successListener, ErrorListener errorListener) {
                final List<Integer> elements = new ArrayList<>();
                for (int i = 0; i<20; i++) {
                    elements.add(page*1000+i);
                }
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        successListener.onGetElements(elements, page<3);
                    }
                }, 2000);
            }
        };
    }

    @Override
    public void displayNoElementIfNeeded(boolean noElement) {

    }
}
