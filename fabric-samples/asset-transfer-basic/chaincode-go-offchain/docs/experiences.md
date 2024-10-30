## Errors 
* This is when  I run the normal `chaincode-go` in the asset-transfer-basic folder
```bash
Using organization 1
Using organization 2
+ infoln 'invoke fcn call:{"function":"InitLedger","Args":[]}'
+ println '\033[0;34minvoke fcn call:{"function":"InitLedger","Args":[]}\033[0m'
+ echo -e '\033[0;34minvoke fcn call:{"function":"InitLedger","Args":[]}\033[0m'
invoke fcn call:{"function":"InitLedger","Args":[]}
+ peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile /home/keo/Documents/blockchain_related/hyperledger-fabric-application/fabric-samples/test-network/organizations/ordererOrganizations/example.com/tlsca/tlsca.example.com-cert.pem -C mychannel -n basic --peerAddresses localhost:7051 --tlsRootCertFiles /home/keo/Documents/blockchain_related/hyperledger-fabric-application/fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/tlsca/tlsca.org1.example.com-cert.pem --peerAddresses localhost:9051 --tlsRootCertFiles /home/keo/Documents/blockchain_related/hyperledger-fabric-application/fabric-samples/test-network/organizations/peerOrganizations/org2.example.com/tlsca/tlsca.org2.example.com-cert.pem --isInit -c '{"function":"InitLedger","Args":[]}'
+ res=0
2024-10-13 00:05:24.957 +07 0001 INFO [chaincodeCmd] chaincodeInvokeOrQuery -> Chaincode invoke successful. result: status:200 
Invoke transaction successful on peer0.org1 peer0.org2 on channel 'mychannel'

```
* This is the output that I got when i run the custom asset-transfer-basic chaincode
  * I just realized that it might be due to  to  the UUID will be genereted on an `InitLedger` called , which might  result the state inconsistency on the other peer as well 
  * Because the we will need the data consistency in order to for the endorsement to be successful, different values from each other will show errors . 
```bash
# example 
const id = uuid.New().String()
const createdDate = time.Now().String() 

# these shall cause the inconsistency in the multiple peers as the values will get generated and its values will be different from each other 

```