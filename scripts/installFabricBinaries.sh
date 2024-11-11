#!/bin/bash

BINARY_DIR=./fabric-samples
# Define versions (change as needed)
FABRIC_VERSION=2.5.0
FABRIC_CA_VERSION=1.5.5

# Set download URLs
BINARY_URL="https://github.com/hyperledger/fabric/releases/download/v$FABRIC_VERSION/hyperledger-fabric-linux-amd64-$FABRIC_VERSION.tar.gz"
CA_URL="https://github.com/hyperledger/fabric-ca/releases/download/v$FABRIC_CA_VERSION/hyperledger-fabric-ca-linux-amd64-$FABRIC_CA_VERSION.tar.gz"

# Create the bin directory in the current project directory

# echo "Creating bin directory in the current project directory..."
# mkdir -p "${BINARY_DIR}"

# Download and extract Hyperledger Fabric binaries to the bin directory
echo "Downloading Hyperledger Fabric binaries..."
curl -L $BINARY_URL | tar -xz -C "${BINARY_DIR}"
if [ $? -ne 0 ]; then
    echo "Error downloading Hyperledger Fabric binaries."
    exit 1
fi
echo "Hyperledger Fabric binaries installed in "${BINARY_DIR}"."

# Download and extract Fabric CA binaries to the bin directory
echo "Downloading Hyperledger Fabric CA binaries..."
curl -L $CA_URL | tar -xz -C "${BINARY_DIR}"
if [ $? -ne 0 ]; then
    echo "Error downloading Hyperledger Fabric CA binaries."
    exit 1
fi
echo "Hyperledger Fabric CA binaries installed in "${BINARY_DIR}"."

# Verify the installation
# echo "Checking installation..."
# if [ -f ./bin/peer ] && [ -f ./bin/fabric-ca-client ] && [ -f ./bin/fabric-ca-server ]; then
#     echo "Installation completed successfully!"
#     echo "Fabric and Fabric CA binaries are located in ./bin"
# else
#     echo "Installation failed. Please check the logs for errors."
#     exit 1
# fi
