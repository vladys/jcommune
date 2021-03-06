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
package org.jtalks.jcommune.service.nontransactional;

import org.jtalks.jcommune.model.entity.JCUser;
import org.jtalks.jcommune.model.entity.Topic;
import org.mockito.Mockito;
import org.springframework.security.core.session.SessionRegistry;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * @author Andrey Kluev
 */
public class LocationServiceTest {
    private Topic topic;
    private LocationService locationService;
    private SecurityService securityService;
    private SessionRegistry sessionRegistry;
    private JCUser user;
    List<Object> list;
    Map<JCUser, String> map;


    @BeforeMethod
    protected void setUp() {
        securityService = mock(SecurityService.class);
        sessionRegistry = mock(SessionRegistry.class);
        locationService = new LocationService(securityService, sessionRegistry);
        user = new JCUser("", "", "");
        topic = new Topic(user, "");
        topic.setUuid("uuid");
        list = new ArrayList<Object>();
        map = new ConcurrentHashMap<JCUser, String>();
    }

    @Test
    public void testUsersViewing() {
        when(securityService.getCurrentUser()).thenReturn(user);
        list.add(user);
        map.put(user, "");
        when(sessionRegistry.getAllPrincipals()).thenReturn(list);

        topic.setUuid("");

        locationService.getUsersViewing(topic);
    }

    @Test
    public void testUserNotOnline() {
        when(securityService.getCurrentUser()).thenReturn(user);
        JCUser user1 = new JCUser("", "", "");
        list.add(user1);
        when(sessionRegistry.getAllPrincipals()).thenReturn(list);


        locationService.getUsersViewing(topic);
    }

    @Test
    public void testCurrentUserIsAnonymous() {
        when(sessionRegistry.getAllPrincipals()).thenReturn(list);

        locationService.getUsersViewing(topic);
    }

    @Test
    public void testClearUserLocation() {
        when(securityService.getCurrentUser()).thenReturn(user);
        when(securityService.getCurrentUser()).thenReturn(user);

        locationService.clearUserLocation();
    }
    
    @Test
    public void testClearUserLocationForAnonymous() {
    	locationService.clearUserLocation();
    	@SuppressWarnings("unchecked")
		Map<JCUser, String> registerUserMap = mock(Map.class);
    	verify(registerUserMap, Mockito.never()).remove(Mockito.any());
    }
}
