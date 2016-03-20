if [ "$#" -ne 2 ]; then
    echo "Illegal number of parameters. Need Username and IP of remote host."
    exit
fi
cat GetSysInfo.sh | ssh $1@$2
