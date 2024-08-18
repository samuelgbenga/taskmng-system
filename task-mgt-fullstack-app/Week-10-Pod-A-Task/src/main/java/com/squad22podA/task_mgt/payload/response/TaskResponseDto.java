package com.squad22podA.task_mgt.payload.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponseDto {

    private String responseCode;

    private String responseMessage;

    private TaskResponseInfo taskResponseInfo;
}
