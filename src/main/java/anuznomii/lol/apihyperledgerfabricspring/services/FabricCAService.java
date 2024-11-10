package anuznomii.lol.apihyperledgerfabricspring.services;

import anuznomii.lol.apihyperledgerfabricspring.models.CAEnrollmentRequest;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.gateway.X509Identity;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

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
    public  void  init() throws Exception {

        // create wallet instance
        this.wallet = Wallets.newFileSystemWallet(
                Paths.get(walletPath)
        );

        // create user admin
        createAdminUserOrg1();

        // create a new user
        CAEnrollmentRequest request = CAEnrollmentRequest.builder()
                .username("client2")
                .affliation("org1.department1")
                .type("client")
                .secret("user1pw")
                .registrarUsername("admin")
                .build();

        registerAndEnrollUser(request);

    }
    // register and enrollment for client
    public  void registerAndEnrollUser(CAEnrollmentRequest request) throws  Exception{

        // ca client
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

        // 1 . register
        var registrationRequest = new RegistrationRequest(
                request.getUsername()) ;
        registrationRequest.setAffiliation(request.getAffliation());
        registrationRequest.setType("client");
        registrationRequest.setMaxEnrollments(-1); // unlimited can enrollment depend on user
        registrationRequest.setSecret(request.getSecret()); /// we also can generate the secret if user didn't provide
        // 1,1 getting the register
        var adminIdentity = wallet.get("admin");
        var adminUser =  new User(){

            X509Identity x509Identities =(X509Identity) adminIdentity;

            @Override
            public String getName() {
                return "admin";
            }

            @Override
            public Set<String> getRoles() {
                return null;
            }

            @Override
            public String getAccount() {
                return null;
            }

            @Override
            public String getAffiliation() {
                return request.getAffliation();
            }

            @Override
            public Enrollment getEnrollment() {
                return new Enrollment() {
                    @Override
                    public PrivateKey getKey() {
                        return x509Identities.getPrivateKey();
                    }

                    @Override
                    public String getCert() {
                        return Identities.toPemString(x509Identities.getCertificate());
                    }
                };
            }

            @Override
            public String getMspId() {
                return "Org1MSP";
            }
        };
        // register the user
        caClient.register(registrationRequest,adminUser);
        log.info("Successfully Register a new user :{}",request.getUsername());


        // 2.  enrollment
        var enrollmentRequest = new EnrollmentRequest();
        enrollmentRequest.setProfile("tls");
        enrollmentRequest.addHost("localHost");

        var enrollment = caClient.enroll(
                request.getUsername(),
                request.getSecret(),
                enrollmentRequest
        );
        //3 . add to the wallet
        var certificate = Identities.readX509Certificate(
                enrollment.getCert()
        );
        // cover certificate to identities
        var userIdentities = Identities.newX509Identity(
                "Org1MSP", certificate,enrollment.getKey()
        );
        // store user identities to wallet
        wallet.put("client1",userIdentities);

        log.info("Add User Identities to Wallet Successfully!  :{}",userIdentities);

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
