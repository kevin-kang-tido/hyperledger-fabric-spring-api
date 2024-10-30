package anuznomii.lol.apihyperledgerfabricspring.configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.Gateway;
import org.hyperledger.fabric.client.Hash;
import org.hyperledger.fabric.client.identity.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.InvalidKeyException;
import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.TlsChannelCredentials;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

@Configuration
public class FabricConfig {

    @Value("${fabric.mspId}")
    private  String mspID;
    @Value("${fabric.channel-name}")
    private  String channelName;
    @Value("${fabric.chaincode-name}")
    private  String chaincodeName;
    @Value("${fabric.peer-endpoint}")
    private  String peerEndpoint;
    @Value("${fabric.override-auth}")
    private  String overrideAuth;

    // Path to crypto materials.
    private static final Path CRYPTO_PATH = Paths
            .get("./fabric-samples/test-network/organizations/peerOrganizations/org1.example.com");
    // Path to user certificate.
    private static final Path CERT_DIR_PATH = CRYPTO_PATH
            .resolve(Paths.get("users/User1@org1.example.com/msp/signcerts"));
    // Path to user private key directory.
    private static final Path KEY_DIR_PATH = CRYPTO_PATH
            .resolve(Paths.get("users/User1@org1.example.com/msp/keystore"));
    // Path to peer tls certificate.
    private static final Path TLS_CERT_PATH = CRYPTO_PATH.resolve(Paths.get("peers/peer0.org1.example.com/tls/ca.crt"));


    @Bean
    public Contract contract(Gateway gateway) {
        return gateway.getNetwork(channelName).getContract(chaincodeName);
    }

    // create gateway bean
    @Bean
    public Gateway gateway() throws Exception{
        var channel = newGrpcConnection();
        return Gateway.newInstance()
                .identity(newIdentity())
                .signer(newSigner())
                .hash(Hash.SHA256)
                .connection(channel)
                // Default timeouts for different gRPC calls
                .evaluateOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                .endorseOptions(options -> options.withDeadlineAfter(15, TimeUnit.SECONDS))
                .submitOptions(options -> options.withDeadlineAfter(5, TimeUnit.SECONDS))
                .commitStatusOptions(options -> options.withDeadlineAfter(1, TimeUnit.MINUTES))
                .connect();
    }

    private  Identity newIdentity() throws IOException, CertificateException {
        try (var certReader = Files.newBufferedReader(getFirstFilePath(CERT_DIR_PATH))) {
            var certificate = Identities.readX509Certificate(certReader);
            return new X509Identity(mspID, certificate);
        }
    }


    private  Signer newSigner() throws IOException, InvalidKeyException {
        try (var keyReader = Files.newBufferedReader(getFirstFilePath(KEY_DIR_PATH))) {
            var privateKey = Identities.readPrivateKey(keyReader);
            return Signers.newPrivateKeySigner(privateKey);
        }
    }
    private  Path getFirstFilePath(Path dirPath) throws IOException {
        try (var keyFiles = Files.list(dirPath)) {
            return keyFiles.findFirst().orElseThrow();
        }
    }
    private  ManagedChannel newGrpcConnection() throws Exception {
        var credentials = TlsChannelCredentials.newBuilder()
                .trustManager(TLS_CERT_PATH.toFile())
                .build();
        return Grpc.newChannelBuilder(peerEndpoint, credentials)
                .overrideAuthority(overrideAuth)
                .build();
    }

}
