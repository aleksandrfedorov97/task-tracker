package com.example.tasktracker.web.mapper;

import com.example.tasktracker.model.User;
import com.example.tasktracker.web.model.UserRequest;
import com.example.tasktracker.web.model.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User userRequestToUser(UserRequest request);
    @Mapping(source = "userId", target = "id")
    User userRequestToUser(String userId, UserRequest request);
    UserResponse userToUserResponse(User user);
}
