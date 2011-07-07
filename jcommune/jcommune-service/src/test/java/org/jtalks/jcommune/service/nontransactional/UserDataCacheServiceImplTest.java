/**
 * Copyright (C) 2011  jtalks.org Team
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
 * Also add information on how to contact you by electronic and paper mail.
 * Creation date: Apr 12, 2011 / 8:05:19 PM
 * The jtalks.org Project
 */
package org.jtalks.jcommune.service.nontransactional;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.jtalks.jcommune.service.UserDataCacheService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * @author Kirill Afonin
 */
public class UserDataCacheServiceImplTest {
    private Ehcache cache;
    private UserDataCacheService userDataCacheService;
    private static final String USERNAME = "usernaME";

    @BeforeMethod
    public void setUp() throws Exception {
        cache = mock(Ehcache.class);
        userDataCacheService = new UserDataCacheServiceImpl(cache);
    }


    @Test
    public void testGetNewPmCountFor() throws Exception {
        when(cache.isKeyInCache(USERNAME)).thenReturn(true);
        when(cache.get(USERNAME)).thenReturn(new Element(USERNAME, 2));

        Integer count = userDataCacheService.getNewPmCountFor(USERNAME);

        assertEquals(count.intValue(), 2);
        verify(cache).isKeyInCache(USERNAME);
        verify(cache).get(USERNAME);
    }

    @Test
    public void testGetNewPmCountForNonExistentUserInCache() throws Exception {
        when(cache.isKeyInCache(USERNAME)).thenReturn(false);

        Integer count = userDataCacheService.getNewPmCountFor(USERNAME);

        assertNull(count);
        verify(cache).isKeyInCache(USERNAME);
        verify(cache, never()).get(anyString());
    }

    @Test
    public void testPutNewPmCount() throws Exception {
        userDataCacheService.putNewPmCount(USERNAME, 2);

        verify(cache).put(new Element(USERNAME, 2));
    }

    @Test
    public void testIncrementNewMessageCountFor() throws Exception {
        Element cacheElement = new Element(USERNAME, 1);
        when(cache.isKeyInCache(USERNAME)).thenReturn(true);
        when(cache.get(USERNAME)).thenReturn(cacheElement);

        userDataCacheService.incrementNewMessageCountFor(USERNAME);

        verify(cache).isKeyInCache(USERNAME);
        verify(cache).get(USERNAME);
        verify(cache).put(new Element(USERNAME, 2));
    }

    @Test
    public void testDecrementNewMessageCountFor() throws Exception {
        Element cacheElement = new Element(USERNAME, 2);
        when(cache.isKeyInCache(USERNAME)).thenReturn(true);
        when(cache.get(USERNAME)).thenReturn(cacheElement);

        userDataCacheService.decrementNewMessageCountFor(USERNAME);

        verify(cache).isKeyInCache(USERNAME);
        verify(cache).get(USERNAME);
        verify(cache).put(new Element(USERNAME, 1));
    }
}