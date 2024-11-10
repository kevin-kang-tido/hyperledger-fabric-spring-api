package anuznomii.lol.apihyperledgerfabricspring.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CAEnrollmentResponse {
    private  String username;
    private String orgName;

    // response more data here
}
