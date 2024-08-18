package com.squad22podA.task_mgt.payload.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileResponseDto {
    private String responseCode;

    private String responseMessage;

    private UserProfileResponseInfo userProfileResponseInfo;

}
