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
package org.jtalks.jcommune.service;

import org.jtalks.jcommune.model.entity.PrivateMessage;
import org.jtalks.jcommune.service.exceptions.NotFoundException;

import java.util.List;

/**
 * The interface to manipulate with private messages.
 *
 * @author Pavel Vervenko
 * @author Kirill Afonin
 * @author Guram Savinov
 */
public interface PrivateMessageService extends EntityService<PrivateMessage> {

    /**
     * Get all inbox messages for the current user.
     *
     * @return the list of messages
     */
    List<PrivateMessage> getInboxForCurrentUser();

    /**
     * Get all outgoing messages from the current user.
     *
     * @return the list of messages
     */
    List<PrivateMessage> getOutboxForCurrentUser();

    /**
     * Send the private message to the user.
     *
     * @param title     the title of the message
     * @param body      the body of the message
     * @param recipient username of receiver
     * @return sent message
     * @throws NotFoundException if the receiver not exists
     */
    PrivateMessage sendMessage(String title, String body, String recipient) throws NotFoundException;

    /**
     * Get current user's drafts
     *
     * @return list of draft messages
     */
    List<PrivateMessage> getDraftsFromCurrentUser();

    /**
     * Save message as draft. If message exist it will be updated.
     *
     * @param id        message id
     * @param title     the title of the message
     * @param body      the body of the message
     * @param recipient username of receiver
     * @return saved message
     * @throws NotFoundException if the receiver not exists
     */
    PrivateMessage saveDraft(long id, String title, String body, String recipient) throws NotFoundException;

    /**
     * Get count of new messages for current user.
     *
     * @return count of new messages
     */
    int currentUserNewPmCount();

    /**
     * Send draft message.
     * After sending message will given "unread" status.
     *
     * @param id        message id
     * @param title     the title of the message
     * @param body      the body of the message
     * @param recipient username of receiver
     * @return saved message
     * @throws NotFoundException if the receiver not exists
     */
    PrivateMessage sendDraft(long id, String title, String body, String recipient) throws NotFoundException;

    /**
     * Delete or change status of messages by id.
     * For messages with SENT status this method change status to
     * DELETED_FROM_INBOX or DELETED_FROM_OUTBOX.
     * Messages with status DELETED_FROM_INBOX, DELETED_FROM_OUTBOX
     * or DRAFT will be removed.
     * 
     * @param ids Identifiers of messages for deletion
     * @return URL for redirection
     */
    String delete(List<Long> ids) throws NotFoundException;
}
