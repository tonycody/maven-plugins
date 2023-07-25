package io.github.tonycody.maven.plugin.sorter.parameter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

/**
 * @author bjorn
 * @since 2016-07-29
 */
class DependencySortOrderTest {

    @Test
    void emptySortOrderShouldWork() {
        assertThat(new DependencySortOrder(null).toString(), is("DependencySortOrder{childElementNames=[]}"));
        assertThat(new DependencySortOrder("").toString(), is("DependencySortOrder{childElementNames=[]}"));
    }

    @Test
    void emptySortOrderShouldNotSort() {
        assertThat(new DependencySortOrder(null).isNoSorting(), is(true));
        assertThat(new DependencySortOrder("").isNoSorting(), is(true));
    }

    @Test
    void singleSortOrderShouldWork() {
        assertThat(new DependencySortOrder("Gurka").toString(), is("DependencySortOrder{childElementNames=[Gurka]}"));
        assertThat(new DependencySortOrder("Gurka").isNoSorting(), is(false));
    }

    @Test
    void multipleSortOrderShouldWork() {
        assertThat(
                new DependencySortOrder("Gurka,Tomat,Melon").toString(),
                is("DependencySortOrder{childElementNames=[Gurka, Tomat, Melon]}"));
    }
}
