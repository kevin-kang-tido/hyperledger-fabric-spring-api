package anuznomii.lol.apihyperledgerfabricspring.restcontrollers;

import org.checkerframework.common.reflection.qual.GetClass;
import org.hyperledger.fabric.client.GatewayException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import anuznomii.lol.apihyperledgerfabricspring.services.ChaincodeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/assets")
@RequiredArgsConstructor
public class AssetRestController {

    // inject the chaincode service in order to invoke the chaincode 
    private final ChaincodeService chaincodeService; 
    @PostMapping("init")
    public ResponseEntity<String> initLedger()throws Exception{
        chaincodeService.initLedger();
        return ResponseEntity.ok("Successfully init the ledger form the chaincode "); 

    }

    @GetMapping("all")
    public ResponseEntity<String> getAllAssets() throws GatewayException {
        return ResponseEntity.ok(chaincodeService.getAllAssets());
    }
}
