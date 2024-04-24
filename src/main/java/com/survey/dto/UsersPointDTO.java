package com.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersPointDTO {
    private String PointId;
    private String UsersId;
    private Integer PointTotal;
    private Integer PointUsed;
    private Integer PointBalance;

}
