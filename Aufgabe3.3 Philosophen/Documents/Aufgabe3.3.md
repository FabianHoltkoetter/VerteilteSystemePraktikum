# Aufgabe 3.3

## a)

### Wie werden die einzelnen Elemente programmtechnisch abgebildet?

#### Philosophen

Ein Philosoph besitzt Referenzen auf eine linke Gabel und eine rechte Gabel (jeweils ein ReentrantLock). Er selbst läuft in einer dauerschleife. Die Schleife beginnt mit der Meditation (sleep), darauf folgt der Gang zum Tisch. Dort wartet der Philosoph bis ein Platz frei wird. Hat der Philosoph den Platz im Array eingenommen versucht er die linke und rechte Gabel zu bekommen. Sobald er beide erhalten hat isst er (sleep). Ist er mit essen fertig gibt er beide Gabeln wieder frei, die Refernz aus dem Platz-Array wird entfernt und der Philosoph beginnt seine Schleife von vorn.

#### Tisch

Ein Tisch besteht aus einem privaten Array aus Plätzen. Wenn ein Philosoph mit dem meditieren fertig ist und an den Tisch kehren möchte, kann er über einen Methodenaufruf einen Platz einnehmen. Ist aktuell kein Platz frei, muss der Philosoph warten.

#### Gabeln

Eine Gabel entspricht einem Java ReentrantLock.

### Welche Prozesse/Threads müssen realisiert werden?

Jeder Philosoph entspricht einem Thread.

### Wie kann eine maximale Parallelität der abläufe erreicht werden?

asdf

### Wie muss eine Synchronisation erfolgen?

Die Philosophen müssen sich auf den Tisch synchronosieren. Die Synchronisation des Zugriffs auf die Gabeln erfolgt automatisch dadurch, dass eine Gabel ein ReentrantLock ist.

### Kann es zu einem Deadlock kommen? Kann dieser vermieden oder erkannt und beseitigt werden?

asdf

## e) Testmessungen

Für die Messungen wurde die "View" nicht initialisiert und den Philosophen kein Observer zugeteilt.

### Maschine 1
Laptop: MacBook Pro 11.3
CPU:    i7-4850HQ
Kerne:  4 (physikalisch) + HT
Takt:   2.3GHz
RAM:    16GB

#### Messungs 1 (5, 0, 5)
| Philosoph | Essvorgänge |
| --- | --- |
| P0 | 8213  |
| P1 | 8207  |
| P2 | 8201  |
| P3 | 8210  |
| P4 | 8200  |

Gesamt: 41031
Durchschnitt: 8206

#### Messung 2 (30, 0, 30)
| Philosoph | Essvorgänge | Philosoph | Essvorgänge | Philosophen |Essvorgänge |
| --- | --- | --- | --- | --- | --- |
| P0  | 7078 | P10 | 7077 | P20 | 7078 |
| P1  | 7087 | P11 | 7082 | P21 | 7086 |
| P2  | 7077 | P12 | 7080 | P22 | 7085 |
| P3  | 7080 | P13 | 7085 | P23 | 7087 |
| P4  | 7080 | P14 | 7083 | P24 | 7076 |
| P5  | 7086 | P15 | 7086 | P25 | 7082 |
| P6  | 7080 | P16 | 7090 | P26 | 7080 |
| P7  | 7085 | P17 | 7082 | P27 | 7084 |
| P8  | 7083 | P18 | 7081 | P28 | 7079 |
| P9  | 7074 | P19 | 7089 | P29 | 7082 |

Gesamt: 212474
Durchschnitt: 7082

#### Messung 3 (50, 0, 10) |
| Philosoph | Essvorgänge | Philosoph | Essvorgänge | Philosophen | Essvorgänge | Philosoph | Essvorgänge | Philosoph | Essvorgänge |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| P0  | 849 | P10 | 873 | P20 | 864 | P30 | 884 | P40 | 850 |
| P1  | 866 | P11 | 875 | P21 | 845 | P31 | 880 | P41 | 879 |
| P2  | 858 | P12 | 849 | P22 | 794 | P32 | 861 | P42 | 885 |
| P3  | 892 | P13 | 878 | P23 | 870 | P33 | 811 | P43 | 853 |
| P4  | 847 | P14 | 823 | P24 | 846 | P34 | 809 | P44 | 840 |
| P5  | 857 | P15 | 842 | P25 | 880 | P35 | 859 | P45 | 854 |
| P6  | 861 | P16 | 859 | P26 | 855 | P36 | 851 | P46 | 857 |
| P7  | 882 | P17 | 850 | P27 | 829 | P37 | 829 | P47 | 865 |
| P8  | 824 | P18 | 859 | P28 | 850 | P38 | 869 | P48 | 854 |
| P9  | 830 | P19 | 822 | P29 | 858 | P39 | 844 | P49 | 816 |

Gesamt: 42637
Durchschnitt: 852

---

### Maschine 2
Laptop: Surface Pro 3
CPU:    i5-4300U
Kerne:  2 (physikalisch)
Takt:   1.9GHz
RAM:    4GB

#### Messungs 1 (5, 0, 5)
| Philosoph | Essvorgänge
| --- | ---
|	P0	|	5907
|	P1	|	5928
|	P2	|	5928
|	P3	|	5921
|	P4	|	5931 |

Gesamtzahl: 29615

Durchschnitt: 5923

#### Messung 2 (30, 0, 30)
| Philosoph | Essvorgänge | Philosoph | Essvorgänge | Philosophen |Essvorgänge |
| --- | --- | --- | --- | --- | --- |
| P0  | 3154 | P10 | 3148 | P20 | 3150 |
| P1  | 3153 | P11 | 3152 | P21 | 3149 |
| P2  | 3152 | P12 | 3149 | P22 | 3152 |
| P3  | 3151 | P13 | 3150 | P23 | 3149 |
| P4  | 3153 | P14 | 3148 | P24 | 3148 |
| P5  | 3149 | P15 | 3146 | P25 | 3150 |
| P6  | 3155 | P16 | 3148 | P26 | 3147 |
| P7  | 3149 | P17 | 3147 | P27 | 3149 |
| P8  | 3149 | P18 | 3147 | P28 | 3150 |
| P9  | 3153 | P19 | 3149 | P29 | 3150 |

Gesamt:94496
Durchschnitt:3149

#### Messung 3 (50, 0, 10) |
| Philosoph | Essvorgänge | Philosoph | Essvorgänge | Philosophen | Essvorgänge | Philosoph | Essvorgänge | Philosoph | Essvorgänge |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| P0  | 26 | P10 | 30 | P20 | 31 | P30 | 29 | P40 | 29 |
| P1  | 29 | P11 | 30 | P21 | 31 | P31 | 30 | P41 | 27 |
| P2  | 29 | P12 | 29 | P22 | 29 | P32 | 30 | P42 | 30 |
| P3  | 29 | P13 | 29 | P23 | 28 | P33 | 29 | P43 | 28 |
| P4  | 31 | P14 | 30 | P24 | 29 | P34 | 29 | P44 | 28 |
| P5  | 29 | P15 | 31 | P25 | 29 | P35 | 28 | P45 | 27 |
| P6  | 29 | P16 | 29 | P26 | 29 | P36 | 31 | P46 | 29 |
| P7  | 31 | P17 | 25 | P27 | 29 | P37 | 28 | P47 | 28 |
| P8  | 29 | P18 | 29 | P28 | 29 | P38 | 30 | P48 | 31 |
| P9  | 30 | P19 | 29 | P29 | 31 | P39 | 30 | P49 | 31 |

Gesamt:1460
Durchschnitt:29
