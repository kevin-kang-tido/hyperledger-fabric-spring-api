package anuznomii.lol.apihyperledgerfabricspring.restcontrollers;

import anuznomii.lol.apihyperledgerfabricspring.dto.AssetRequest;
import org.checkerframework.common.reflection.qual.GetClass;
import org.hyperledger.fabric.client.GatewayException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    //   // get assetBYID

    @GetMapping("/{assetId}")
    public String readAsset(@PathVariable String assetId) throws Exception {
        return chaincodeService.readAsset(assetId);
    }


    // create  a new asset to ledger
    @PostMapping("/create")
    String createAsset(@RequestBody AssetRequest assetRequest) throws Exception {
        return chaincodeService.createAsset(assetRequest);
    }
    // update
    @PutMapping("/update/{assetId}")
    public String updateAsset(@PathVariable String assetId, @RequestBody AssetRequest assetRequest) throws  Exception{
        return chaincodeService.updateAsset( assetId,assetRequest);
    }

    // delete
    @DeleteMapping("{assetId}")
    public String deleteAsset(@PathVariable String assetId) throws  Exception{
        return chaincodeService.deleteAsset(assetId);
    }




}
