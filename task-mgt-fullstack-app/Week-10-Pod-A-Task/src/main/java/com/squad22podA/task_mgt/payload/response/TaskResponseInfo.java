package com.squad22podA.task_mgt.payload.response;

import com.squad22podA.task_mgt.entity.enums.PriorityLevel;
import com.squad22podA.task_mgt.entity.enums.Status;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponseInfo {

    private Long id;

    private String title;


    private String description;


    private LocalDateTime deadline;


    private PriorityLevel priorityLevel;


    private Status status;

    private LocalDateTime createdAt;
}
