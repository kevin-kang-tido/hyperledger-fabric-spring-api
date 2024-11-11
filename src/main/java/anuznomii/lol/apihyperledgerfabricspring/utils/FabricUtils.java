package anuznomii.lol.apihyperledgerfabricspring.utils;

import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class FabricUtils {

    public static void setTlsProps(
            Properties props,
            String org1CertificatePath,
            Boolean tlsEnabled
    ) throws Exception {
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
    }

    // checking identities exist  for not
    public static boolean checkIdentityExistence(
            String label,
            Wallet wallet
    ) throws IOException {
        return wallet.get(label) != null;
    }




}
