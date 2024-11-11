package anuznomii.lol.apihyperledgerfabricspring.configuration;

import anuznomii.lol.apihyperledgerfabricspring.services.FabricCAService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;


/*
* Configure the Fabric Gateway is Essential for setting up and secure,
* reliable connection between your application and Fabric Network
* */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class FabricGatewayConfig {

    // write a configuration for fabric gateway

    @Value("${fabric.wallet.config-path}")
    private  String walletPath;
    @Value("${fabric.network.config-path}")
    private  String networkConfigPath;
    private Wallet wallet;

    @Value("${fabric.network.discovery}")
    private Boolean isDiscovered;

    // inject ca service
    private  final FabricCAService fabricCAService;

    @Bean
    public Gateway createGateway() throws  Exception{

        //  store file in wallet
        this.wallet = Wallets.newFileSystemWallet(
                Paths.get(walletPath)
        );

        // get the networkConfigPath
        var networkConfig = Paths.get(networkConfigPath);

        // create gateway
        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet,"admin") // make it dynamic role later...
                .networkConfig(networkConfig)
                .discovery(isDiscovered)
                .commitTimeout(60, TimeUnit.SECONDS);

        var gateway= builder.connect();

        log.info("Gateway is Connecting Successfully!");
        return  gateway;
    }
}
