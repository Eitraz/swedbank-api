package com.github.eitraz.swedbank;

import com.github.eitraz.swedbank.exception.SwedbankClientException;
import com.github.eitraz.swedbank.model.Link;

import java.util.*;
import java.util.function.Function;

public class PageIterable<C, T> implements Iterable<T> {
    private final SwedbankClient swedbankClient;
    private final Function<Optional<C>, Optional<Link>> nextLinkProvider;
    private final Class<C> containerResponseType;
    private final Function<C, Iterator<T>> iteratorProvider;

    private final List<C> pageContainers = new ArrayList<>();

    private Iterator<T> buffer = Collections.emptyIterator();

    PageIterable(SwedbankClient swedbankClient,
                 Function<Optional<C>, Optional<Link>> nextLinkProvider,
                 Class<C> containerResponseType,
                 Function<C, Iterator<T>> iteratorProvider) {
        this.swedbankClient = swedbankClient;
        this.nextLinkProvider = nextLinkProvider;
        this.containerResponseType = containerResponseType;
        this.iteratorProvider = iteratorProvider;

        // First data load
        pageContainers.add(getNextPageContainer(null));
    }

    private Optional<Link> getNextLink(C pageContainer) {
        return nextLinkProvider.apply(Optional.ofNullable(pageContainer));
    }

    /**
     * Should only be called when verified that a next link is present
     */
    @SuppressWarnings("ConstantConditions")
    private C getNextPageContainer(C pageContainer) {
        try {
            return swedbankClient.follow(getNextLink(pageContainer).get(), containerResponseType);
        } catch (SwedbankClientException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void fillBuffer() {
        // Load next container page
        if (pageContainers.size() == 1 && getNextLink(pageContainers.get(0)).isPresent()) {
            pageContainers.add(getNextPageContainer(pageContainers.get(0)));
        }

        // Remove first page container and add to buffer
        if (!pageContainers.isEmpty()) {
            buffer = iteratorProvider.apply(pageContainers.remove(0));
        }
        // No more pages
        else {
            buffer = Collections.emptyIterator();
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                // Buffer or next page has more items
                return buffer.hasNext() || (!pageContainers.isEmpty() && iteratorProvider.apply(pageContainers.get(0)).hasNext());
            }

            @Override
            public T next() {
                if (buffer.hasNext()) {
                    return buffer.next();
                } else {
                    fillBuffer();
                    return buffer.next();
                }
            }
        };
    }
}
