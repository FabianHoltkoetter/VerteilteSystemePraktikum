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
| Philosoph | Essvorgänge
| --- | ---
|	P0	|	5442
|	P1	|	5442
|	P2	|	5439
|	P3	|	5442
|	P4	|	5439
Gesamtzahl: 27204

Durchschnitt: 5440

#### Messung 2 (30, 0, 30)
| Philosoph | Essvorgänge | Philosoph | Essvorgänge | Philosophen |Essvorgänge |
| --- | --- | --- | --- | --- | --- |
|	P0	|	5189|	P10	|	5197|	P20	|	5184 |
|	P1	|	5181|	P11	|	5184|	P21	|	5183 |
|	P2	|	5188|	P12	|	5193|	P22	|	5184 |
|	P3	|	5183|	P13	|	5180|	P23	|	5193 |
|	P4	|	5187|	P14	|	5179|	P24	|	5187 |
|	P5	|	5188|	P15	|	5183|	P25	|	5190 |
|	P6	|	5184|	P16	|	5189|	P26	|	5189 |
|	P7	|	5194|	P17	|	5193|	P27	|	5186 |
|	P8	|	5193|	P18	|	5186|	P28	|	5185 |
|	P9	|	5191|	P19	|	5187|	P29	|	5183 |

Gesamt: 155613

Durchschnitt: 5187

#### Messung 3 (50, 0, 10) |
| Philosoph | Essvorgänge | Philosoph | Essvorgänge | Philosophen | Essvorgänge | Philosoph | Essvorgänge | Philosoph | Essvorgänge |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| P0  | 784 | P10 | 798 | P20 | 790 | P30 | 840 | P40 | 765 |
| P1  | 756 | P11 | 820 | P21 | 828 | P31 | 821 | P41 | 797 |
| P2  | 795 | P12 | 801 | P22 | 839 | P32 | 774 | P42 | 796 |
| P3  | 812 | P13 | 796 | P23 | 829 | P33 | 774 | P43 | 814 |
| P4  | 790 | P14 | 807 | P24 | 802 | P34 | 816 | P44 | 754 |
| P5  | 835 | P15 | 800 | P25 | 813 | P35 | 822 | P45 | 812 |
| P6  | 785 | P16 | 812 | P26 | 788 | P36 | 754 | P46 | 805 |
| P7  | 808 | P17 | 795 | P27 | 826 | P37 | 813 | P47 | 787 |
| P8  | 805 | P18 | 787 | P28 | 817 | P38 | 788 | P48 | 822 |
| P9  | 774 | P19 | 802 | P29 | 790 | P39 | 824 | P49 | 788 |

Gesamt: 40050

Durchschnitt: 801

---

### Maschine 2
Laptop: Surface Pro 3
CPU:    i5-4300U
Kerne:  2 (physikalisch)
Takt:   1.9GHz
RAM:    4GB

#### Messungs 1 (5, 0, 5)
| Philosoph | Essvorgänge
| --- | --- |
|	P0	|	5907 |
|	P1	|	5928 |
|	P2	|	5928 |
|	P3	|	5921 |
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
