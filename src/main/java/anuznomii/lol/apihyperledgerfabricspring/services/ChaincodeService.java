package anuznomii.lol.apihyperledgerfabricspring.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChaincodeService {
    private final Gateway gateway;

    // 1 . create and invoke method
    public  void  invokeChaincode (
            String chaincodeName,
            String methodName, String...arg) throws ContractException, InterruptedException, TimeoutException {

        // try catch network
        var network = gateway.getNetwork("mychannel");
        // take network to get the chaincode
        var contract = network.getContract(chaincodeName); // we can name chainCodeName directly (Default)

        var result  = contract.submitTransaction(methodName,arg);

        log.info("Here is the Result: {}",result);
    }

    // 2 . create a query method
    public String queryChaincode(
            String chaincodeName,
            String methodName,
            String ...arg

    ) throws ContractException, InterruptedException, TimeoutException {

        var network = gateway.getNetwork("mychannel");
        var contract = network.getContract(chaincodeName);

        var result = contract.submitTransaction(methodName,arg);
        log.info("Method query is Successfully ");

        return  new String(result);
    }



}
