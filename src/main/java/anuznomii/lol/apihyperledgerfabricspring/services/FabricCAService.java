package anuznomii.lol.apihyperledgerfabricspring.services;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;

/*
* this service we write logic to get ca (-register , enrollment) for user or client certificate
* Fabric CA client
*
* */

@Service
@Slf4j
public class FabricCAService {

    // we get the values form the application.yaml
   @Value("${fabric.ca.org1.caUrl}")
    private String org1CaUrl;
   @Value("${fabric.ca.org1.certificatePath}")
    private String org1CertificatePath;

    private Wallet wallet;
    @Value("${fabric.wallet.config-path}")
    private  String walletPath;

    @Value("${fabric.ca.tls.enabled}")
    private Boolean tlsEnabled;

   // for testing only
   // get the default user and password for our yaml file
   @Value("${fabric.ca.admin.username}")
   private  String adminUsername;
   @Value("${fabric.ca.admin.password}")
   private  String adminPassword;

    @PostConstruct
    public  void  init() throws Exception{

        // create wallet instance
        this.wallet = Wallets.newFileSystemWallet(
                Paths.get(walletPath)
        );

        // create user admin
      createAdminUserOrg1();
    }

    // create  admin first before the make client register and enroll user
    private  void createAdminUserOrg1() throws  Exception{

     // 1. create a enroll request
     var enrollmentRequest = new EnrollmentRequest();
     enrollmentRequest.setProfile("tls");
     enrollmentRequest.addHost("localhost");
//     enrollmentRequest.addAttrReq();

     // 2. set up the Hyperledger fabric ca Client(HFCA client)
     var props = new Properties();
     if (tlsEnabled){
      // configure the tls properties for the Hyperledger Fabric CA Client
      // 2.1 configure the tls properties
      File pemFile = new File(org1CertificatePath);
      if (!pemFile.exists()){
       throw new Exception(" Certificate for Org1 CA file doesn't exist");
      }
      // verify type of the certificate and also validate the certificate .....
      props.setProperty("pemFile", org1CertificatePath);
      props.setProperty("allowAllHostName","true");

      // configure the timeout for each request
      // 2.2 configure the timeout
      // 30000 ms === 30 ms
      props.setProperty("connectTimeout","3000");
      props.setProperty("readTimeout","30000");
     }
     // create new client ro instance
     var caClient = HFCAClient.createNewInstance(
             org1CaUrl,
             props
     );
     caClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());

     // Already for the enrollment
     var enrollment = caClient.enroll(
             adminUsername,
             adminPassword,
             enrollmentRequest
     );
     // get certificate
     var certificate = Identities.readX509Certificate(
             enrollment.getCert()
     );
     // covert the certificate to Identities
     var adminIdentity = Identities.newX509Identity(
             "Org1SMP",certificate,enrollment.getKey()
     );
     // take Identity to the wallet
     wallet.put("admin",adminIdentity);
     log.info("Successfully store the identity to the wallet !  ");
    }







}
