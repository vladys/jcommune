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
package org.jtalks.jcommune.service.transactional;

import org.jtalks.jcommune.model.dao.PrivateMessageDao;
import org.jtalks.jcommune.model.entity.JCUser;
import org.jtalks.jcommune.model.entity.PrivateMessage;
import org.jtalks.jcommune.model.entity.PrivateMessageStatus;
import org.jtalks.jcommune.service.UserService;
import org.jtalks.jcommune.service.exceptions.NotFoundException;
import org.jtalks.jcommune.service.nontransactional.MailService;
import org.jtalks.jcommune.service.nontransactional.SecurityService;
import org.jtalks.jcommune.service.nontransactional.UserDataCacheService;
import org.jtalks.jcommune.service.security.AclBuilder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.jtalks.jcommune.service.TestUtils.mockAclBuilder;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author Pavel Vervenko
 * @author Kirill Afonin
 * @author Max Malakhov
 */
public class TransactionalPrivateMessageServiceTest {

    @Mock
    private PrivateMessageDao pmDao;
    @Mock
    private SecurityService securityService;
    @Mock
    private UserService userService;
    @Mock
    private UserDataCacheService userDataCache;
    @Mock
    private MailService mailService;

    private TransactionalPrivateMessageService pmService;

    private static final long PM_ID = 1L;
    private static final String USERNAME = "username";
    private AclBuilder aclBuilder;

    JCUser user = new JCUser(USERNAME, "email", "password");

    @BeforeMethod
    public void setUp() throws Exception {
        initMocks(this);
        aclBuilder = mockAclBuilder();
        pmService = new TransactionalPrivateMessageService(pmDao, securityService, userService, userDataCache,
                mailService);
    }

    @Test
    public void testGetInboxForCurrentUser() {

        when(pmDao.getAllForUser(user)).thenReturn(new ArrayList<PrivateMessage>());
        when(securityService.getCurrentUser()).thenReturn(user);

        pmService.getInboxForCurrentUser();

        verify(pmDao).getAllForUser(user);
        verify(securityService).getCurrentUser();
    }

    @Test
    public void testGetOutboxForCurrentUser() {
        when(pmDao.getAllFromUser(user)).thenReturn(new ArrayList<PrivateMessage>());
        when(securityService.getCurrentUser()).thenReturn(user);

        pmService.getOutboxForCurrentUser();

        verify(pmDao).getAllFromUser(user);
        verify(securityService).getCurrentUser();
    }

    @Test
    public void testSendMessage() throws NotFoundException {
        when(securityService.grantToCurrentUser()).thenReturn(aclBuilder);

        PrivateMessage pm = pmService.sendMessage("body", "title", USERNAME);

        assertFalse(pm.isRead());
        assertEquals(pm.getStatus(), PrivateMessageStatus.SENT);
        verify(securityService).getCurrentUser();
        verify(userService).getByUsername(USERNAME);
        verify(userDataCache).incrementNewMessageCountFor(USERNAME);
        verify(mailService).sendReceivedPrivateMessageNotification(userService.getByUsername(USERNAME), pm);
        verify(pmDao).saveOrUpdate(pm);
        verify(securityService).grantToCurrentUser();
        verify(aclBuilder).user(USERNAME);
        verify(aclBuilder).read();
        verify(aclBuilder).on(pm);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void testSendMessageToWrongUser() throws NotFoundException {
        when(userService.getByUsername(USERNAME)).thenThrow(new NotFoundException());

        PrivateMessage pm = pmService.sendMessage("body", "title", USERNAME);

        verify(pmDao, never()).saveOrUpdate(pm);
        verify(userService).getByUsername(USERNAME);
    }

    @Test
    public void testGetDraftsFromCurrentUser() {
        when(pmDao.getDraftsFromUser(user)).thenReturn(new ArrayList<PrivateMessage>());
        when(securityService.getCurrentUser()).thenReturn(user);

        pmService.getDraftsFromCurrentUser();

        verify(pmDao).getDraftsFromUser(user);
        verify(securityService).getCurrentUser();
    }

    @Test
    public void testSaveDraft() throws NotFoundException {
        when(securityService.grantToCurrentUser()).thenReturn(aclBuilder);

        PrivateMessage pm = pmService.saveDraft(PM_ID, "body", "title", USERNAME);

        assertEquals(pm.getId(), PM_ID);
        verify(securityService).getCurrentUser();
        verify(userService).getByUsername(USERNAME);
        verify(pmDao).saveOrUpdate(pm);
        verify(aclBuilder).admin();
        verify(aclBuilder).on(pm);
    }

    @Test
    public void testSaveDraftRecipientUsernameNull() throws NotFoundException {
    	when(securityService.grantToCurrentUser()).thenReturn(aclBuilder);
    	
    	String recipientUsername = null;
    	pmService.saveDraft(PM_ID, "body", "title", recipientUsername);
    	
    	verify(userService, Mockito.never()).getByUsername(Mockito.any(String.class));
		// other verifications are covered in main test
    }

    @Test
    public void testCurrentUserNewPmCount() {
        int expectedPmCount = 2;
        when(securityService.getCurrentUserUsername()).thenReturn(USERNAME);
        when(pmDao.getNewMessagesCountFor(USERNAME)).thenReturn(expectedPmCount);
        when(userDataCache.getNewPmCountFor(USERNAME)).thenReturn(null);

        int newPmCount = pmService.currentUserNewPmCount();

        assertEquals(newPmCount, expectedPmCount);
        verify(securityService).getCurrentUserUsername();
        verify(pmDao).getNewMessagesCountFor(USERNAME);
        verify(userDataCache).putNewPmCount(USERNAME, newPmCount);
    }

    @Test
    public void testCurrentUserNewPmCountCached() {
        int expectedPmCount = 2;
        when(securityService.getCurrentUserUsername()).thenReturn(USERNAME);
        when(userDataCache.getNewPmCountFor(USERNAME)).thenReturn(expectedPmCount);

        int newPmCount = pmService.currentUserNewPmCount();

        assertEquals(newPmCount, expectedPmCount);
        verify(pmDao, never()).getNewMessagesCountFor(anyString());
        verify(userDataCache).getNewPmCountFor(USERNAME);
    }

    @Test
    public void testCurrentUserNewPmCountWithoutUser() {
        when(securityService.getCurrentUserUsername()).thenReturn(null);

        int newPmCount = pmService.currentUserNewPmCount();

        assertEquals(newPmCount, 0);
        verify(securityService).getCurrentUserUsername();
    }

    @Test
    public void testSendDraft() throws NotFoundException {
        when(securityService.grantToCurrentUser()).thenReturn(aclBuilder);

        PrivateMessage pm = pmService.sendDraft(1L, "body", "title", USERNAME);

        assertFalse(pm.isRead());
        assertEquals(pm.getStatus(), PrivateMessageStatus.SENT);
        verify(securityService).getCurrentUser();
        verify(userService).getByUsername(USERNAME);
        verify(userDataCache).incrementNewMessageCountFor(USERNAME);
        verify(mailService).sendReceivedPrivateMessageNotification(userService.getByUsername(USERNAME), pm);
        verify(pmDao).saveOrUpdate(pm);
        verify(securityService).deleteFromAcl(pm);
        verify(securityService).grantToCurrentUser();
        verify(aclBuilder).user(USERNAME);
        verify(aclBuilder).read();
        verify(aclBuilder).on(pm);
    }

    @Test
    public void testGetMessageToMe() throws NotFoundException {
        PrivateMessage expected = new PrivateMessage(user, user, "title", "body");
        when(pmDao.get(PM_ID)).thenReturn(expected);
        when(pmDao.isExist(PM_ID)).thenReturn(true);
        when(securityService.getCurrentUser()).thenReturn(user);

        PrivateMessage pm = pmService.get(PM_ID);

        assertEquals(pm, expected);
        assertTrue(pm.isRead());
        verify(pmDao).saveOrUpdate(pm);
        verify(userDataCache).decrementNewMessageCountFor(USERNAME);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void testGetNotFound() throws NotFoundException {
        when(pmDao.isExist(PM_ID)).thenReturn(false);

        PrivateMessage pm = pmService.get(PM_ID);
    }

    @Test
    public void testGetReadAlreadyRead() throws NotFoundException {
        PrivateMessage expected = new PrivateMessage(user, user, "title", "body");
        expected.setRead(true);
        when(pmDao.get(PM_ID)).thenReturn(expected);
        when(pmDao.isExist(PM_ID)).thenReturn(true);
        when(securityService.getCurrentUser()).thenReturn(user);

        PrivateMessage pm = pmService.get(PM_ID);

        verify(pmDao, never()).saveOrUpdate(pm);
        verify(userDataCache, never()).decrementNewMessageCountFor(USERNAME);
    }
    
    @Test
    public void testGetPrivateMessageInDraftStatus() throws NotFoundException {
    	PrivateMessage message = new PrivateMessage(user, user, "title", "body");
    	message.setStatus(PrivateMessageStatus.DRAFT);
    	
    	when(securityService.getCurrentUser()).thenReturn(user);
    	when(pmDao.get(PM_ID)).thenReturn(message);
    	when(pmDao.isExist(PM_ID)).thenReturn(true);
    	
    	PrivateMessage resultMessage = pmService.get(PM_ID);
    	
    	assertEquals(resultMessage.isRead(), false, 
    			"Message status is draft, so message shouldn't be marked as read");
    	verify(pmDao, never()).saveOrUpdate(resultMessage);
    	verify(userDataCache, never()).decrementNewMessageCountFor(USERNAME);
    }
    
    @Test
    public void testGetPrivateMessageUserToNotCurrentUser() throws NotFoundException {
    	PrivateMessage message = new PrivateMessage(user, user, "title", "body");
    	JCUser currentUser = new JCUser(USERNAME, "email", "password");
    	
    	when(securityService.getCurrentUser()).thenReturn(currentUser);
    	when(pmDao.get(PM_ID)).thenReturn(message);
    	when(pmDao.isExist(PM_ID)).thenReturn(true);
    	
    	PrivateMessage resultMessage = pmService.get(PM_ID);
    	
    	assertEquals(resultMessage.isRead(), false, 
    			"The message isn't addressed to the current user, so message shouldn't be marked as read.");
    	verify(pmDao, never()).saveOrUpdate(resultMessage);
    	verify(userDataCache, never()).decrementNewMessageCountFor(USERNAME);
    }
}
