#!/bin/bash

function manage_entries(){
    local ACTION=$1 # action can be 'add' or 'remove'
    shift # Shifts to get entries as arguments.


    if [[ "$ACTION" != "add" && "$ACTION" != "remove" ]]; then
        echo "Usage: $0 [add|remove] \"127.0.0.1 hostname\" [additional_entries]"
        exit 1
    fi
    # check the the privileges
    if ! [ -w /etc/hosts ] && ! command -v sudo &> /dev/null; then
        echo "You need to have root privileges to run this script"
        exit 1
    fi

    # Backup the /etc/hosts file
    sudo cp /etc/hosts /etc/hosts.bak

    for ENTRY in "$@"; do
        IP=$(echo $ENTRY | awk '{print $1}')
        HOSTNAME=$(echo $ENTRY | awk '{print $2}')
        if [ "$ACTION" == "add" ]; then
            # Check if the entry already exists
            if ! grep -q "$ENTRY" /etc/hosts; then
                echo "$ENTRY" | sudo tee -a /etc/hosts > /dev/null
                echo "Added $ENTRY to /etc/hosts"
            else
                echo "Entry already exists: $ENTRY"
            fi
        elif [ "$ACTION" == "remove" ]; then
            # Remove any line that match the entry
            sudo sed -i".tmp" "/${IP}[[:space:]]*$HOSTNAME/d" /etc/hosts
            echo "Removed: $ENTRY"
        fi
    done

    echo "Host file updated successfully ! "
}


