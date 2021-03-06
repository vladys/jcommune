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

import org.jtalks.jcommune.service.nontransactional.SecurityService;
import org.jtalks.jcommune.web.dto.EditUserProfileDto;
import org.jtalks.jcommune.web.validation.annotations.ChangedPassword;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates if password set matches the current user's password.
 * This check is to be performed only if new passwword has been set.
 *
 * @author Evgeniy Naumenko
 */
public class ChangedPasswordValidator implements ConstraintValidator<ChangedPassword, EditUserProfileDto> {

    private String message;

    private SecurityService service;

    /**
     * @param service to obtain current user logged in
     */
    @Autowired
    public ChangedPasswordValidator(SecurityService service) {
        this.service = service;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(ChangedPassword constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(EditUserProfileDto dto, ConstraintValidatorContext context) {
        boolean result = dto.getNewUserPassword() == null;
        result |= service.getCurrentUser().getPassword().equals(dto.getCurrentUserPassword());
        if (!result) {
            // add validation error to the field
            context.buildConstraintViolationWithTemplate(message)
                    .addNode("currentUserPassword")
                    .addConstraintViolation();
        }
        return result;
    }
}
