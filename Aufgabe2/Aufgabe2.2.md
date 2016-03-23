# Aufgabe 2.2

## a)

Systemdienste auf Ports:

Befehle:
netstat -lntup | grep "LISTEN"

ps \<PID\>

```
Local Address           PID/Program name	Path

0.0.0.0:111             408/rpcbind     	/sbin/rpcbind
0.0.0.0:22              453/sshd					/usr/sbin/sshd
127.0.0.1:631           493/cupsd       	/usr/sbin/cupsd
0.0.0.0:54744           424/rpc.statd   	/sbin/rpc.statd
127.0.0.1:25            898/exim4       	/usr/sbin/exim4
```

## b)

SSH war bereits aktiviert. Bei Verbindung über SSH erscheint folgende Meldung:

```
The programs included with the Debian GNU/Linux system are free software;
the exact distribution terms for each program are described in the
individual files in /usr/share/doc/*/copyright.

Debian GNU/Linux comes with ABSOLUTELY NO WARRANTY, to the extent
permitted by applicable law.
Last login: Sat Mar 19 18:36:03 2016 from 192.168.56.1
```
