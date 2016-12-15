package com.jwang123.androidpaginatesample.paginate;

import java.util.List;

/**
 * Created by wangjie on 15/12/2016.
 */

public interface Paginatable {
    /**
     * add the refresh views for full screen refresh and bottom refresh.
     */
    void addRefresh();

    void endRefreshing();

    void startFullScreenRefresh();

    void endFullScreenRefresh();

    void startBottomRefresh();

    void stopBottomRefresh();

    void invalidateElements();

    ElementsGetter getElementsGetter();

    void displayNoElementIfNeeded(boolean noElement);

    interface ElementsGetter {

        void getElements(int page, SuccessListener successListener, ErrorListener errorListener);

        interface SuccessListener<T> {
            void onGetElements(List<T> elements, boolean hasMoreElements);
        }

        interface ErrorListener {
            void onError(PaginateError error);
        }
    }

    // TODO: 15/12/2016
    interface PaginateError {

    }
}


