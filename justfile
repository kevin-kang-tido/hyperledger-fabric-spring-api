network option:
    #!/bin/bash
     case {{option}} in
     "up")
        echo "Bring the test-nework up 🚀🚀🚀"
        cd fabric-samples/test-network && ./network.sh down && ./network.sh up createChannel -ca
        echo "Deploy the chainCode. 🚀🚀🚀🚀🚀🚀"
         bash network.sh deployCC
        ;;
     "down")
     echo "Bring the test-network down 🔥🔥🔥"
     cd fabric-samples/test-network && ./network.sh down
     ;;
     "*")
     echo "Check your comment a agian ! (Just network up|down)"
     ;;
     esac
utils option:
  #!/bin/bash
  case {{option}} in
  "binary")
    echo "Installing the Binaries 🚀🚀🚀"
     ./scripts/installFabricBinaries.sh
    ;;
  "*")
    echo "Invalid Command, Please Check it again! 🔥🔥🔥"
    ;;
  esac


