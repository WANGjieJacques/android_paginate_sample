package com.jwang123.androidpaginatesample.paginate;

import java.util.List;

/**
 * Created by wangjie on 15/12/2016.
 */

public class PaginatePresenter<T> {
    private Paginatable paginatable;
    public List<T> elements;
    private boolean hasMoreElements = true;
    private int currentPage = 0;
    private boolean isLoadingNextPage = false;
    private boolean isRefreshing = false;

    public PaginatePresenter(Paginatable paginatable) {
        this.paginatable = paginatable;
    }

    public void start() {
        paginatable.addRefresh();
        paginatable.stopBottomRefresh();
    }

    public void pullDownRefresh() {
        if (isRefreshing) {
            return;
        }
        isRefreshing = true;
        paginatable.getElementsGetter().getElements(0, new Paginatable.ElementsGetter.SuccessListener<T>() {
            @Override
            public void onGetElements(List<T> elements, boolean hasMoreElements) {
                currentPage = 0;
                PaginatePresenter.this.hasMoreElements = hasMoreElements;
                PaginatePresenter.this.elements = elements;
                paginatable.endRefreshing();
                paginatable.invalidateElements();
                isRefreshing = false;
                paginatable.displayNoElementIfNeeded(elements.size() == 0);
            }
        }, new Paginatable.ElementsGetter.ErrorListener() {
            @Override
            public void onError(Paginatable.PaginateError error) {
                paginatable.endRefreshing();
                isRefreshing = false;
            }
        });
    }

    public void fullScreenRefresh() {
        paginatable.startFullScreenRefresh();
        pullDownRefresh();
    }

    public void cancelRefresh() {
        paginatable.endRefreshing();
        isRefreshing = false;
    }

    public void loadNextPage() {
        if (isRefreshing) {
            return;
        }
        if (isLoadingNextPage) {
            return;
        }
        if (!hasMoreElements) {
            return;
        }
        isLoadingNextPage = true;
        paginatable.startBottomRefresh();
        paginatable.getElementsGetter().getElements(currentPage + 1, new Paginatable.ElementsGetter.SuccessListener() {
            @Override
            public void onGetElements(List elements, boolean hasMoreElements) {
                PaginatePresenter.this.elements.addAll(elements);
                PaginatePresenter.this.hasMoreElements = hasMoreElements;
                paginatable.stopBottomRefresh();
                paginatable.invalidateElements();
                currentPage += 1;
                isLoadingNextPage = false;
            }
        }, new Paginatable.ElementsGetter.ErrorListener() {
            @Override
            public void onError(Paginatable.PaginateError error) {
                isLoadingNextPage = false;
                paginatable.stopBottomRefresh();
            }
        });
    }
}
