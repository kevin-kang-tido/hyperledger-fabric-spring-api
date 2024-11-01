package anuznomii.lol.apihyperledgerfabricspring.services;

import anuznomii.lol.apihyperledgerfabricspring.dto.AssetRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.client.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
		String jsonResult = new String(result);
		return jsonResult;

	}

	// handle to crate a new asset to leder
	public  String createAsset(AssetRequest assetRequest) throws  EndorseException, SubmitException,CommitStatusException,CommitException{

		String assetID = UUID.randomUUID().toString();

		log.info("Submitting the transaction : CreateAsset to create a new asset to the ledger...");

		contract.submitTransaction("CreateAsset",
				assetID,
				assetRequest.Color(),
				assetRequest.Size(),
				assetRequest.Owner(),
				String.valueOf(assetRequest.AppraisedValue())
				);
		log.info("Asset created successfully! with the assetID {}", assetID);
		return  "Asset  ID with ID : " + assetID;

	}

	// Read asset with the specific assetID
	public String readAsset(String assetId) throws GatewayException {
		log.info("Evaluating transaction: ReadAsset for assetId {}", assetId);
		byte[] result = contract.evaluateTransaction("ReadAsset", assetId);
		return new String(result);
	}

	// update the asset by id
	public  String updateAsset(String assetId,AssetRequest assetRequest) throws  EndorseException ,SubmitException,CommitStatusException,CommitException {

		log.info("Submitting the transaction : Update By ID {}",assetId);

		contract.submitTransaction("UpdateAsset",
				assetId,
				assetRequest.Color(),
				assetRequest.Size(),
				assetRequest.Owner(),
				String.valueOf(assetRequest.AppraisedValue())
				);
		log.info("Asset is update successfully !");

		return "Asset update with ID "+ assetId;

	}

	// delete asset id
	public String deleteAsset(String assetId) throws EndorseException,SubmitException,CommitStatusException,CommitException{

		log.info("Submitting the transacting : Delete the delete the asset by Id {} ",assetId);

		contract.submitTransaction("deleteAsset",assetId);

		log.info("Delete the Asset has been Successfully");
		return "Asset Delete with ID: "+ assetId;

	}











}
