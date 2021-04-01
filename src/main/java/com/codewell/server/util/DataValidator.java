package com.codewell.server.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

public class DataValidator
{
    public static boolean isValidUsername(final String username)
    {
        return StringUtils.isNotEmpty(username) && StringUtils.isAlphanumeric(username);
    }

    public static boolean isValidEmail(final String email)
    {
        return StringUtils.isNotEmpty(email) && EmailValidator.getInstance().isValid(email);
    }

    public static boolean isValidAge(final Integer age)
    {
        return age == null || age > 0;
    }

    public static boolean isValidAddress(final String address)
    {
        return address == null || StringUtils.isAlphaSpace(address);
    }
}
