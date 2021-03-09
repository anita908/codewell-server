package com.codewell.server.service;

import com.codewell.server.dto.UserLearningModel;

public interface UserLearningService
{
    UserLearningModel getUserLearningModel(final String userId) throws Exception;
}
