package anuznomii.lol.apihyperledgerfabricspring.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Builder
public class CAEnrollmentRequest {

    private  String username;
    private  String secret ;
    private  String affliation; // org1 == live which department
    private  String orgName;
    private  String registrarUsername;
    private  String type; // type : orderer | client | admin
    @Builder .Default
    private  Boolean genSecret = true;





}
