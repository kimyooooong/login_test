package com.kimyooong.login_test.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPassword {
    private Long rpId;
    private Long userId;
    private String randomNumber;
    private Boolean confirm;
    private String expireDate;
    private String createDate;
}
