package com.squad22podA.task_mgt.service.impl;

import com.squad22podA.task_mgt.entity.enums.Status;
import com.squad22podA.task_mgt.entity.model.Task;
import com.squad22podA.task_mgt.entity.model.UserModel;
import com.squad22podA.task_mgt.payload.request.TaskRequest;
import com.squad22podA.task_mgt.payload.response.TaskResponseDto;
import com.squad22podA.task_mgt.payload.response.TaskResponseInfo;
import com.squad22podA.task_mgt.repository.TaskRepository;
import com.squad22podA.task_mgt.repository.UserModelRepository;
import com.squad22podA.task_mgt.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class TaskServiceImpl implements TaskService {


    private final TaskRepository taskRepository;
    public final UserModelRepository userModelRepository;


    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserModelRepository userModelRepository) {
        this.taskRepository = taskRepository;
        this.userModelRepository = userModelRepository;
    }


    @Override
    @Transactional
    public TaskResponseDto createTask(String email, TaskRequest taskRequest) {
        Optional<UserModel> optionalUser = userModelRepository.findByEmail(email);
        if(optionalUser.isEmpty()) {
            throw  new RuntimeException("User not found");
        }


        // get the actual user
        UserModel user = optionalUser.get();

        // add the task
        Task task = Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .deadline(taskRequest.getDeadline())
                .priorityLevel(taskRequest.getPriorityLevel())
                .status(Status.PENDING)
                .userModel(user)
                .build();

        // save to repo
        taskRepository.save(task);

        // return the api response
        return TaskResponseDto.builder()
                .responseCode("002")
                .responseMessage("Task created")
                .taskResponseInfo(TaskResponseInfo.builder()
                        .title(task.getTitle())
                        .description(task.getDescription())
                        .deadline(task.getDeadline())
                        .priorityLevel(task.getPriorityLevel())
                        .status(task.getStatus())
                        .build())
                .build();
    }

    @Override
    @Transactional
    public TaskResponseDto editTask(String email, Long taskId, TaskRequest taskRequest) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if(optionalTask.isEmpty()) {
            throw  new RuntimeException("Task not found");
        }
        Task task = optionalTask.get();

        if (!task.getUserModel().getEmail().equals(email)) {
            throw new RuntimeException("You do not have permission to edit this task");
        }


        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setDeadline(taskRequest.getDeadline());
        task.setPriorityLevel(taskRequest.getPriorityLevel());
        task.setStatus(taskRequest.getStatus());

        Task updatedTask = taskRepository.save(task);


        return TaskResponseDto.builder()
                .responseCode("003")
                .responseMessage("Task has been update")
                .taskResponseInfo(TaskResponseInfo.builder()
                        .title(updatedTask.getTitle())
                        .description(updatedTask.getDescription())
                        .deadline(updatedTask.getDeadline())
                        .priorityLevel(updatedTask.getPriorityLevel())
                        .status(updatedTask.getStatus())
                        .build())
                .build();
    }


    @Override
    public List<Task> getTasksByStatus(Status status) {
        return taskRepository.findByStatus(status);
    }

    @Override
    public List<Task> getTasksByStatusAndUserId(Status status, Long userId) {
        return taskRepository.findByStatusAndUserModelId(status, userId);
    }

    @Override
    public List<TaskResponseDto> getAllTask(String email) {
        List<Task> tasks = taskRepository.findAll();
//        System.out.println("not filtered" + tasks);

        // Get all task
        return tasks.stream()
                .filter(task -> task.getUserModel().getEmail().equals(email))
                .map(task -> TaskResponseDto.builder()
                        .responseCode("006")
                        .responseMessage("Get all Task was a Success")
                        .taskResponseInfo(TaskResponseInfo.builder()
                                .id(task.getId())
                                .title(task.getTitle())
                                .description(task.getDescription())
                                .deadline(task.getDeadline())
                                .priorityLevel(task.getPriorityLevel())
                                .status(task.getStatus())
                                .createdAt(task.getCreatedAt())
                                .build())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getTasksByStatusAndUserEmail(Status status, String email) {
        // Implement retrieval of tasks by status and user identifier
        List<Task> tasks = taskRepository.findByStatusAndUserModelEmail(status, email);

        // return all tasks by status
        return tasks.stream()
                .filter(task -> task.getUserModel().getEmail().equals(email))
                .map(task -> TaskResponseDto.builder()
                        .responseCode("009")
                        .responseMessage("Get all Filtered Task was a Success")
                        .taskResponseInfo(TaskResponseInfo.builder()
                                .id(task.getId())
                                .title(task.getTitle())
                                .description(task.getDescription())
                                .deadline(task.getDeadline())
                                .priorityLevel(task.getPriorityLevel())
                                .status(task.getStatus())
                                .build())
                        .build())
                .collect(Collectors.toList());
//        return taskRepository.findByStatusAndUserModelEmail(status, email);
    }

    @Override
    public TaskResponseDto getTask(String email, Long taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if(optionalTask.isEmpty()) {
            throw  new RuntimeException("Task not found");
        }
        Task task = optionalTask.get();

        if (!task.getUserModel().getEmail().equals(email)) {
            throw new RuntimeException("You do not have permission to edit this task");
        }


        return TaskResponseDto.builder()
                .responseCode("004")
                .responseMessage("Get Task was a Success")
                .taskResponseInfo(TaskResponseInfo.builder()
                        .id(task.getId())
                        .title(task.getTitle())
                        .description(task.getDescription())
                        .deadline(task.getDeadline())
                        .priorityLevel(task.getPriorityLevel())
                        .status(task.getStatus())
                        .build())
                .build();
    }

    @Override
    public List<TaskResponseDto> getCompletedTask(String email) {
        Status status = Status.COMPLETED;
        List<Task> tasks = taskRepository.findByStatus(status);

        // Filter tasks by email and map to TaskResponseDto
        List<TaskResponseDto> taskResponseDtos = tasks.stream()
                .filter(task -> task.getUserModel().getEmail().equals(email))
                .map(task -> TaskResponseDto.builder()
                        .responseCode("005")
                        .responseMessage("Get Task by Status was a Success")
                        .taskResponseInfo(TaskResponseInfo.builder()
                                .id(task.getId())
                                .title(task.getTitle())
                                .description(task.getDescription())
                                .deadline(task.getDeadline())
                                .priorityLevel(task.getPriorityLevel())
                                .status(task.getStatus())
                                .build())
                        .build())
                .collect(Collectors.toList());

        return taskResponseDtos;
    }

    @Override
    public TaskResponseDto deleteTask(String email, Long taskId) {

        try {
            taskRepository.deleteById(taskId);
        } catch (Exception e) {
            return TaskResponseDto.builder()
                    .responseCode("404")
                    .responseMessage("Task Not found to delete")
                    .build();
        }



        return TaskResponseDto.builder()
                .responseCode("008")
                .responseMessage("Task Delete successful")
                .build();
    }


}
