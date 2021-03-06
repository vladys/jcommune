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
package org.jtalks.jcommune.web.controller;

import org.joda.time.DateTime;
import org.jtalks.jcommune.model.entity.Branch;
import org.jtalks.jcommune.model.entity.JCUser;
import org.jtalks.jcommune.model.entity.Topic;
import org.jtalks.jcommune.service.BranchService;
import org.jtalks.jcommune.service.LastReadPostService;
import org.jtalks.jcommune.service.PostService;
import org.jtalks.jcommune.service.TopicService;
import org.jtalks.jcommune.service.exceptions.NotFoundException;
import org.jtalks.jcommune.service.nontransactional.LocationService;
import org.jtalks.jcommune.service.nontransactional.SecurityService;
import org.jtalks.jcommune.web.dto.BranchDto;
import org.jtalks.jcommune.web.dto.Breadcrumb;
import org.jtalks.jcommune.web.util.BreadcrumbBuilder;
import org.jtalks.jcommune.web.util.ForumStatisticsProvider;
import org.jtalks.jcommune.web.util.Pagination;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.ModelAndViewAssert.assertAndReturnModelAttributeOfType;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeAvailable;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;
import static org.testng.Assert.assertEquals;


/**
 * @author Kravchenko Vitaliy
 * @author Alexandre Teterin
 * @author Evdeniy Naumenko
 * @author Eugeny Batov
 */
public class BranchControllerTest {
    @Mock
    private BranchService branchService;
    @Mock
    private TopicService topicService;
    @Mock
    private BreadcrumbBuilder breadcrumbBuilder;
    @Mock
    private LocationService locationServiceImpl;
    @Mock
    private ForumStatisticsProvider forumStatisticsProvider;
    @Mock
    private LastReadPostService lastReadPostService;
    @Mock
    private SecurityService securityService;

    private BranchController controller;

    private static final DateTime now = new DateTime();

    @BeforeMethod
    public void init() {
        initMocks(this);
        controller = new BranchController(branchService, topicService, lastReadPostService,
               securityService, breadcrumbBuilder, locationServiceImpl);
    }

    @Test
    public void showPage() throws NotFoundException {
        JCUser user = new JCUser("", "", "");
        Map map = new HashMap<JCUser, String>();
        map.put(user, "");
        long branchId = 1L;
        int page = 2;
        boolean pagingEnabled = true;
        Branch branch = new Branch("name");
        branch.setId(branchId);
        //set expectations
        when(branchService.get(branchId)).thenReturn(branch);
        when(breadcrumbBuilder.getForumBreadcrumb(branchService.get(branchId)))
                .thenReturn(new ArrayList<Breadcrumb>());
        when(forumStatisticsProvider.getOnlineRegisteredUsers()).thenReturn(new ArrayList<Object>());

        //invoke the object under test
        ModelAndView mav = controller.showPage(branchId, page, pagingEnabled);

        //check expectations
        verify(breadcrumbBuilder).getForumBreadcrumb(branchService.get(branchId));

        //check result
        assertViewName(mav, "topicList");
        assertAndReturnModelAttributeOfType(mav, "topics", List.class);

        Branch actualBranch = assertAndReturnModelAttributeOfType(mav, "branch", Branch.class);
        assertEquals(actualBranch.getId(), branchId);

        Pagination pagination = assertAndReturnModelAttributeOfType(mav, "pagination", Pagination.class);
        assertEquals(pagination.getPage().intValue(), page);
        assertModelAttributeAvailable(mav, "breadcrumbList");

    }

    @Test
    public void recentTopicsPage() throws NotFoundException {
        int page = 2;
        //set expectations
        when(topicService.getRecentTopics(now)).thenReturn(new ArrayList<Topic>());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("lastlogin", now);

        //invoke the object under test
        ModelAndView mav = controller.recentTopicsPage(page, session);

        //check expectations
        verify(topicService).getRecentTopics(now);

        //check result
        assertViewName(mav, "recent");
        assertAndReturnModelAttributeOfType(mav, "topics", List.class);

        Pagination pagination = assertAndReturnModelAttributeOfType(mav, "pagination", Pagination.class);
        assertEquals(pagination.getMaxPages(), 1);
        assertEquals(pagination.getPage().intValue(), page);

    }

    @Test
    public void unansweredTopicsPage() {
        int page = 1;
        //set expectations
        when(topicService.getUnansweredTopics()).thenReturn(new ArrayList<Topic>());

        //invoke the object under test
        ModelAndView mav = controller.unansweredTopicsPage(page);

        //check expectations
        verify(topicService).getUnansweredTopics();

        //check result
        assertViewName(mav, "unansweredTopics");
        assertAndReturnModelAttributeOfType(mav, "topics", List.class);

        Pagination pagination = assertAndReturnModelAttributeOfType(mav, "pagination", Pagination.class);
        assertEquals(pagination.getMaxPages(), 1);
        assertEquals(pagination.getPage().intValue(), page);
    }

    @Test
    public void testViewList() throws NotFoundException {
        JCUser user = new JCUser("", "", "");
        Map map = new HashMap<JCUser, String>();
        map.put(user, "");
        long branchId = 1L;
        int page = 2;
        boolean pagingEnabled = true;
        Branch branch = new Branch("name");
        branch.setId(branchId);
        //set expectations
        when(branchService.get(branchId)).thenReturn(branch);
        when(breadcrumbBuilder.getForumBreadcrumb(branchService.get(branchId)))
                .thenReturn(new ArrayList<Breadcrumb>());
        when(forumStatisticsProvider.getOnlineRegisteredUsers()).thenReturn(new ArrayList<Object>());

        ModelAndView mav = controller.showPage(branchId, page, pagingEnabled);

        List<String> actualViewList = assertAndReturnModelAttributeOfType(mav, "viewList", List.class);
        assertEquals(actualViewList, new ArrayList<String>());
    }

    @Test
    public void testGetBranchesFromSection() throws NotFoundException {
        long sectionId = 1L;
        long branchId = 1L;
        List<Branch> branches = new ArrayList<Branch>();
        Branch branch = new Branch("name");
        branch.setId(branchId);
        branches.add(branch);
        when(branchService.getBranchesInSection(sectionId)).thenReturn(branches);

        BranchDto[] branchDtoArray = controller.getBranchesFromSection(sectionId);

        assertEquals(branchDtoArray.length, branches.size());
        assertEquals(branchDtoArray[0].getId(), branch.getId());
        assertEquals(branchDtoArray[0].getName(), branch.getName());
    }

    @Test
    public void testGetAllBranches() throws NotFoundException {
        long branchId = 1L;
        List<Branch> branches = new ArrayList<Branch>();
        Branch branch = new Branch("name");
        branch.setId(branchId);
        branches.add(branch);
        when(branchService.getAllBranches()).thenReturn(branches);

        BranchDto[] branchDtoArray = controller.getAllBranches();

        assertEquals(branchDtoArray.length, branches.size());
        assertEquals(branchDtoArray[0].getId(), branch.getId());
        assertEquals(branchDtoArray[0].getName(), branch.getName());
    }
    
    @Test 
    public void testMarkAllTopicsAsRead() throws NotFoundException {
    	Long id = Long.valueOf(1);
    	String result = controller.markAllTopicsAsRead(id);
    	assertEquals(result, "redirect:/branches/" + String.valueOf(id));
    }
}
