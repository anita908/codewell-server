package com.codewell.server.web;

import com.codewell.server.service.UserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest
{
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController target;
}
