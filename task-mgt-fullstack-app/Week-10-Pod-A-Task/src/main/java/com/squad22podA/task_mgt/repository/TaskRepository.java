package com.squad22podA.task_mgt.repository;

import com.squad22podA.task_mgt.entity.enums.Status;
import com.squad22podA.task_mgt.entity.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    ///Filter All Task By Status Only
    List<Task> findByStatus(Status status);

    //Filter Task by userId And Status
    List<Task> findByStatusAndUserModelId(Status status, Long userId);

    void deleteById(Long taskId);

    List<Task> findByStatusAndUserModelEmail(Status status, String email);
}
