#!/bin/bash
# source ./addOrRemoveHost.sh
source ./scripts/addOrRemoveHost.sh

manage_entries add "127.0.0.1    peer0.org1.example.com"
manage_entries add "127.0.0.1    peer0.org2.example.com"
manage_entries add "127.0.0.1    orderer.example.com"
manage_entries add "127.0.0.1    ca.org1.example.com"
manage_entries add "127.0.0.1    ca.org2.example.com"

# manage_entries remove "127.0.0.1    ca.org2.example.com"