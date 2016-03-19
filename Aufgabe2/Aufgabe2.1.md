# Aufgabe 2.1

## a)

* Host
	* uname: Darwin
	* Hostname: dynamic.eduroam.mwn.de
	* IP: 192.168.56.1
	* MAC: 0a:00:27:00:00:00
* Gast
	* uname: Linux
	* Hostname: pcvirtuell
	* IP: 192.168.56.101
	* MAC: 08:00:27:8e:25:00

## b)

Mithilfe von *nslookup* kann man die Übersetzung von IP-Adresse -> Hostname, beziehungsweise auch von Hostname -> IP-Adresse vornehmen.nslookup stellt dabei anfragen an den im Netzwerkadapter konfigurierten DNS-Server und verarbeitet die Antwort.

Die numerische Adresse des DNS-Servers ist: 10.156.33.53

## c)

Über *netstat* lassen sich sämtliche Informationen über das Netzwerk des Linux-Systems ausgeben
Mithilfe von *netstat -p TCP* lassen sich nur Verbindungen die das Protokoll TCP nutzen anzeigen.

## d)

Das Programm *ping* versendet *ECHO_REQUEST* s über das ICMP-Protokoll um die Verbindung zwischen Teilnehmern eines Netzwerks testen  zu können

## e)

*traceroute* bietet die Möglichkeit den weg von versendeten IP-Paketen über eventuelle zwischenpunkte nachzuverfolgen. So kann man etwa den Weg eines Pakets über ein Gateway und Proxies bis hin zur Ziel-Adresse genau nachverfolgen.

Ein Aufruf mit dem Hostname von heise.de aus einem privaten Netzwerk ergab folgenden output:
```
root@pcvirtuell:~# traceroute heise.de
traceroute to heise.de (193.99.144.80), 30 hops max, 60 byte packets
 1  192.168.1.1 (192.168.1.1)  1.876 ms  4.060 ms  4.056 ms
 2  192.168.0.1 (192.168.0.1)  3.099 ms  16.393 ms  16.514 ms
 3  10.220.208.1 (10.220.208.1)  16.557 ms  16.589 ms  16.613 ms
 4  ve-cmts.mes-muc-01.de.infra.cablesurf.de (95.157.61.1)  17.953 ms  18.121 ms  18.114 ms
 5  85.232.1.156 (85.232.1.156)  24.705 ms  29.059 ms  29.019 ms
 6  ams-ix.nl.plusline.net (80.249.208.161)  47.395 ms  29.194 ms  38.195 ms
 7  ams-ix.nl.plusline.net (80.249.208.161)  38.415 ms  38.396 ms  38.341 ms
 8  82.98.102.17 (82.98.102.17)  38.360 ms  38.324 ms  38.340 ms
 9  * * *
10  * * *
11  * * *
12  * * *
13  * * *
14  * 212.19.61.13 (212.19.61.13)  31.931 ms !X *
```
