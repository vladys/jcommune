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
package org.jtalks.jcommune.web.dto;

import org.jtalks.jcommune.model.entity.Section;

/**
 * Dto for transferring sections to client side.
 * Dto does not contains branches in section, just id and name.
 *
 * @author Eugeny Batov
 */
public class SectionDto {

    private long id;
    private String name;

    /**
     * Creates dto for section.
     *
     * @param section section for conversion
     */
    public SectionDto(Section section) {
        this.id = section.getId();
        this.name = section.getName();
    }

    /**
     * @return section id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets section id.
     *
     * @param id id of section
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return section name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets section name.
     *
     * @param name name of section
     */
    public void setName(String name) {
        this.name = name;
    }

}
