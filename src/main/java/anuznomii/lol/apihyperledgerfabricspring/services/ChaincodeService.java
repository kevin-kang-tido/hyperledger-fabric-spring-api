package anuznomii.lol.apihyperledgerfabricspring.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.client.*;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChaincodeService {
	private final Contract contract;


    public void initLedger() throws EndorseException, SubmitException, CommitStatusException, CommitException {
		System.out.println("\n--> Submit Transaction: InitLedger, function creates the initial set of assets on the ledger");

		contract.submitTransaction("InitLedger");

		System.out.println("*** Transaction committed successfully");
	}
	public String getAllAssets() throws GatewayException {
		log.info("\n--> Evaluate Transaction: GetAllAssets, function returns all the current assets on the ledger");
		byte[] result = contract.evaluateTransaction("GetAllAssets");
//		String jsonResult = prettyJson(result);
//		log.info("*** Result: {}", FabricUtils.prettyJson(result));
		String jsonResult = new String(result);
		return jsonResult;

	}
}
