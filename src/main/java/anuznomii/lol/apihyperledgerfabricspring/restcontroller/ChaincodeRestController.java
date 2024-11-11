package anuznomii.lol.apihyperledgerfabricspring.restcontroller;

import anuznomii.lol.apihyperledgerfabricspring.services.ChaincodeService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hyperledger.fabric.gateway.ContractException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/v1/chaincode")
@AllArgsConstructor
public class ChaincodeRestController {

    // inject chaincode service
    private final ChaincodeService chaincodeService;

    // init chaincode
    @PostMapping("/{chaincodeName}/{functionName}/invoke")
    public String invokeChaincodeMethod(
            @PathVariable String chaincodeName,
            @PathVariable String functionName

    ) throws Exception {
        chaincodeService.invokeChaincode(chaincodeName,functionName,"null");
        return  "Invoke chaincode" + functionName + "Successfully";
    }

    // query the chaincode
    @GetMapping("/{chaincodeName}/{functionName}/query")
    public ResponseEntity<String> queryChaincode(
            @PathVariable String chaincodeName,
            @PathVariable String functionName
    ) throws Exception{

        return  ResponseEntity.ok(
                chaincodeService.queryChaincode(chaincodeName,functionName,"null")
        );
    }



}
