package com.codewell.server.util;

import com.codewell.server.dto.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

public class UserUtil
{
    public static boolean hasValidEmail(final UserDto user)
    {
        return StringUtils.isEmpty(user.getEmail()) || EmailValidator.getInstance().isValid(user.getEmail());
    }

    public static boolean hasValidAge(final UserDto user)
    {
        return user.getAge() == null || user.getAge() > 0;
    }

    public static boolean hasValidAddress(final UserDto user)
    {
        return user.getCity() == null || StringUtils.isAlphaSpace(user.getCity());
    }

    public static boolean hasValidUsername(final UserDto user)
    {
        return StringUtils.isNotEmpty(user.getUsername()) && StringUtils.isAlphanumeric(user.getUsername());
    }
}
