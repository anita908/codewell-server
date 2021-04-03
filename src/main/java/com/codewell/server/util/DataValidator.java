package com.codewell.server.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.Set;

public class DataValidator
{
    private static final Set<String> STATES = Set.of("Alabama", "Alaska", "Arizona", "Arkansas", "California",
        "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Idaho", "Hawaii", "Illinois", "Iowa", "Kansas",
        "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi",
        "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York",
        "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina",
        "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming");

    public static boolean isValidUsername(final String username)
    {
        return StringUtils.isNotEmpty(username) && StringUtils.isAlphanumeric(username);
    }

    public static boolean isValidEmail(final String email)
    {
        return StringUtils.isNotEmpty(email) && EmailValidator.getInstance().isValid(email);
    }

    public static boolean isValidCity(final String city)
    {
        return city == null || StringUtils.isAlphaSpace(city);
    }

    public static boolean isValidState(final String state)
    {
        return state == null || STATES.contains(state);
    }
}
