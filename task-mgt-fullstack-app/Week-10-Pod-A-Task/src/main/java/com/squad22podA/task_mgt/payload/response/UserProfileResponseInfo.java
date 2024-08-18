package com.squad22podA.task_mgt.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileResponseInfo {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
