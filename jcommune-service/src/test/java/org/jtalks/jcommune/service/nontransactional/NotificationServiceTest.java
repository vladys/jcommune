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


import org.jtalks.jcommune.model.entity.Branch;
import org.jtalks.jcommune.model.entity.JCUser;
import org.jtalks.jcommune.model.entity.Topic;
import org.jtalks.jcommune.service.exceptions.MailingFailedException;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Evgeniy Naumenko
 */
public class NotificationServiceTest {

    @Mock
    private MailService mailService;
    @Mock
    private SecurityService securityService;

    private NotificationService service;

    private JCUser user1 = new JCUser("name1", "email1", "password1");
    private JCUser user2 = new JCUser("name2", "email2", "password2");
    private Topic topic;
    private Branch branch;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        service = new NotificationService(securityService, mailService);
        topic = new Topic(user1, "title");
        branch = new Branch("name");
        branch.addTopic(topic);
    }

    @Test
    public void testTopicChanged() throws MailingFailedException {
        topic.getSubscribers().add(user1);
        topic.getSubscribers().add(user2);

        service.topicChanged(topic);

        verify(mailService).sendTopicUpdatesOnSubscription(user1, topic);
        verify(mailService).sendTopicUpdatesOnSubscription(user2, topic);
    }

    @Test
    public void testBranchChanged() throws MailingFailedException {
        branch.getSubscribers().add(user1);
        branch.getSubscribers().add(user2);

        service.branchChanged(branch);

        verify(mailService).sendBranchUpdatesOnSubscription(user1, branch);
        verify(mailService).sendBranchUpdatesOnSubscription(user2, branch);
    }

    @Test
    public void testTopicChangedSelfSubscribed() throws MailingFailedException {
        when(securityService.getCurrentUser()).thenReturn(user1);
        topic.getSubscribers().add(user1);
        topic.getSubscribers().add(user2);

        service.topicChanged(topic);

        verify(mailService).sendTopicUpdatesOnSubscription(user2, topic);
        verifyNoMoreInteractions(mailService);
    }

    @Test
    public void testBranchChangedSelfSubscribed() throws MailingFailedException {
        when(securityService.getCurrentUser()).thenReturn(user1);
        branch.getSubscribers().add(user1);
        branch.getSubscribers().add(user2);

        service.branchChanged(branch);

        verify(mailService).sendBranchUpdatesOnSubscription(user2, branch);
        verifyNoMoreInteractions(mailService);
    }

    @Test
    public void testTopicChangedNoSubscribers() {
        service.topicChanged(topic);

        verifyZeroInteractions(mailService);
    }

    @Test
    public void testBranchChangedNoSubscribers() {
        service.branchChanged(branch);

        verifyZeroInteractions(mailService);
    }
}
