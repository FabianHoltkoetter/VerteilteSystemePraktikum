echo -e "Rechnername\t:" $(hostname)
echo -e "Betriebssystem\t:" $(uname)
echo -e "Anzahl der CPUs\t:" $(grep "cpu cores" /proc/cpuinfo | awk '{print $4}')
echo -e "Durchschn. Last\t:" $(uptime | awk -F'[a-z]:' '{ print $2}')
echo -e "Speicher gesamt\t:" $(grep MemTotal /proc/meminfo | awk '{print $2}') "kb"
echo -e "Speicher frei\t:" $(grep MemFree /proc/meminfo | awk '{print $2}') "kb"
