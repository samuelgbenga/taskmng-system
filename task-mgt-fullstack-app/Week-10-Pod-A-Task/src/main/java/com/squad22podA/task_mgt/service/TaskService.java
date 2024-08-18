package com.squad22podA.task_mgt.service;

import com.squad22podA.task_mgt.entity.enums.Status;
import com.squad22podA.task_mgt.entity.model.Task;
import com.squad22podA.task_mgt.payload.request.TaskRequest;
import com.squad22podA.task_mgt.payload.response.TaskResponseDto;

import java.util.List;

public interface TaskService {

    TaskResponseDto createTask(String email, TaskRequest taskRequest);
    TaskResponseDto editTask(String email, Long taskId, TaskRequest taskRequest);

    //Filter All Task By Status Only
    List<Task> getTasksByStatus(Status status);

    //Filter Task by userId And Status
    List<Task> getTasksByStatusAndUserId(Status status, Long userId);

    List<TaskResponseDto> getAllTask(String email);

    TaskResponseDto getTask(String email, Long taskId);

    List<TaskResponseDto> getCompletedTask(String email);

    TaskResponseDto deleteTask(String email, Long taskId);

    List<TaskResponseDto> getTasksByStatusAndUserEmail(Status status, String email) ;

}
