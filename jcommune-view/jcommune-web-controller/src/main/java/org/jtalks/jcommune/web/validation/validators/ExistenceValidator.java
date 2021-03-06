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
package org.jtalks.jcommune.web.validation.validators;

import org.jtalks.common.model.entity.Entity;
import org.jtalks.jcommune.model.dao.ValidatorDao;
import org.jtalks.jcommune.web.validation.annotations.Exists;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Checks string value for existence in a database.
 *
 * @author Evgeniy Naumenko
 * @see Exists
 */
public class ExistenceValidator implements ConstraintValidator<Exists, String> {

    private Class<? extends Entity> entity;
    private String field;

    private ValidatorDao<String> dao;

    /**
     * @param dao session factory fro database requests
     */
    @Autowired
    public ExistenceValidator(ValidatorDao<String> dao) {
        this.dao = dao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(Exists annotation) {
        this.entity = annotation.entity();
        this.field = annotation.field();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return (value != null) && !dao.isResultSetEmpty(entity, field, value);
    }
}
