package com.squad22podA.task_mgt.payload.request;

import com.squad22podA.task_mgt.entity.enums.PriorityLevel;
import com.squad22podA.task_mgt.entity.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Deadline is required")
    private LocalDateTime deadline;

    @NotNull(message = "Priority level is required")
    @Enumerated(EnumType.STRING)
    private PriorityLevel priorityLevel;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private Status status;

}
