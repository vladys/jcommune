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
package org.jtalks.jcommune.model.dao;

import org.jtalks.common.model.dao.ChildRepository;
import org.jtalks.jcommune.model.entity.JCUser;
import org.jtalks.jcommune.model.entity.LastReadPost;
import org.jtalks.jcommune.model.entity.Post;
import org.jtalks.jcommune.model.entity.Topic;

import java.util.List;

/**
 * Interface allows to make basic CRUD operations with the
 * {@link Post} objects.
 * At the current moment it doesn't provides any additional methods over the basic {@link ChildRepository} interface
 * but some specific methods will be added soon.
 *
 * @author Pavel Vervenko
 * @author Kirill Afonin
 * @see org.jtalks.jcommune.model.dao.hibernate.PostHibernateDao
 */
public interface PostDao extends ChildRepository<Post> {

    /**
     * @param author user to select posts for
     * @return post list of user
     */
    List<Post> getUserPosts(JCUser author);

    /**
     * Fetches last read post information for particular user and topic.
     *
     * @param forWho user to find last read post for
     * @param topic topic we're interesting in
     * @return last read post for the particular topic or null if user had never opened this topic
     */
    LastReadPost getLastReadPost(JCUser forWho, Topic topic);

    /**
     * Saves last read post for the particular topic and user.
     * May update the existing value or create a new one.
     *
     * @param post last read post representation
     */
    void saveLastReadPost(LastReadPost post);
}
