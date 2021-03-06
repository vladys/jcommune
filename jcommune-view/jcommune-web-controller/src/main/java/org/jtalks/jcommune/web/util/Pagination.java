/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.jcommune.web.util;

import org.jtalks.jcommune.model.entity.JCUser;

import java.util.List;

/**
 * Class for pagination.
 *
 * @author Kirill Afonin
 * @author Andrey Kluev
 */
public class Pagination {
    private int page;
    private int pageSize = JCUser.DEFAULT_PAGE_SIZE;
    private int itemsCount;
    private boolean pagingEnabled;

    /**
     * Create instance.
     *
     * @param page          page (default 1)
     * @param currentUser   current user
     * @param itemsCount    total number of items
     * @param pagingEnabled paging status
     */
    public Pagination(Integer page, JCUser currentUser, int itemsCount, boolean pagingEnabled) {
        this.page = page;
        this.pageSize = Pagination.getPageSizeFor(currentUser);
        this.itemsCount = itemsCount;
        this.pagingEnabled = pagingEnabled;
    }


    /**
     * @return page number of current page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * todo: looks odd, do we necessary need it?
     *
     * @param pageSize number of items on the page
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return number of items on the page
     */
    public int getPageSize() {
        return pageSize;
    }


    /**
     * Returns page count.
     *
     * @return page count
     */
    private int calculatePageCount() {
        return itemsCount / pageSize;
    }

    /**
     * @return total number of pages
     */
    public int getMaxPages() {
        int maxPages = isRounded() ? calculatePageCount() : calculatePageCount() + 1;
        return Math.max(maxPages, 1);

    }

    /**
     * @return {@code true} if number of pages rounded else {@code false}
     */
    public boolean isRounded() {
        return (itemsCount % pageSize) == 0;
    }

    /**
     * @return true if current page is last
     */
    public boolean isLastPage() {
        return page == getMaxPages();
    }

    /**
     * @return pagingEnabled is flag for the button Show all/Show pages
     */
    public boolean isPagingEnabled() {
        return pagingEnabled;
    }



    /**
     * used if the total number of items
     * divided by the number of elements on a page without a trace
     *
     * @param list list of items
     * @return list new list of items
     */
    public List integerNumberOfPages(List list) {
        return list.subList((getPage() - 1) * pageSize, getPage() * pageSize);
    }

    /**
     * used if the total number of items
     * divided by the number of elements on the page with the remainder
     *
     * @param list list of items
     * @return list new list of items
     */
    public List notIntegerNumberOfPages(List list) {
        return list.subList((getPage() - 1) * pageSize,
                (getPage() - 1) * pageSize + list.size() % pageSize);
    }

    /**
     * Returns page size applicable for the current user. If for some reasons
     * this implementation is unable to determaine this parameter the default
     * value will be used.
     *
     * @param user current user representation, may be null
     * @return page size for the current user or default if there is no user
     */
    public static int getPageSizeFor(JCUser user) {
        return (user == null) ? JCUser.DEFAULT_PAGE_SIZE : user.getPageSize();
    }
}
