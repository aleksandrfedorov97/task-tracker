package com.example.tasktracker.web.mapper;

import com.example.tasktracker.model.Task;
import com.example.tasktracker.web.model.TaskCreateRequest;
import com.example.tasktracker.web.model.TaskUpdateRequest;
import com.example.tasktracker.web.model.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class})
public interface TaskMapper {

    Task taskCreateRequestToTask(TaskCreateRequest request);

    @Mapping(source = "taskId", target = "id")
    Task taskUpdateRequestToTask(String taskId, TaskUpdateRequest request);
    TaskResponse taskToTaskResponse(Task task);

}
