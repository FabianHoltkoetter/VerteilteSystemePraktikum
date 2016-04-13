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
|	P0	|	5304
|	P1	|	5313
|	P2	|	5304
|	P3	|	5313
|	P4	|	5307

Gesamtzahl: 26541

Durchschnitt: 5308

#### Messung 2 (30, 0, 30)
| Philosoph | Essvorgänge | Philosoph | Essvorgänge | Philosoph | Essvorgänge
| --- | --- | --- | --- | --- | ---
|	P0	|	5389|	P10	|	5393|	P20	|	5388
|	P1	|	5387|	P11	|	5392|	P21	|	5385
|	P2	|	5386|	P12	|	5389|	P22	|	5391
|	P3	|	5388|	P13	|	5384|	P23	|	5389
|	P4	|	5387|	P14	|	5385|	P24	|	5384
|	P5	|	5386|	P15	|	5388|	P25	|	5386
|	P6	|	5391|	P16	|	5391|	P26	|	5393
|	P7	|	5386|	P17	|	5385|	P27	|	5388
|	P8	|	5391|	P18	|	5388|	P28	|	5390
|	P9	|	5388|	P19	|	5387|	P29	|	5387

Gesamtzahl: 161642

Durchschnitt: 5388

#### Messung 3 (50, 0, 10) |
| Philosoph | Essvorgänge | Philosoph | Essvorgänge | Philosoph | Essvorgänge | Philosoph | Essvorgänge | Philosoph | Essvorgänge
| --- | --- | --- | --- | --- | --- | --- | --- | --- | ---
|	P0	|	4127|	P10	|	4072|	P20	|	4140|	P30	|	4236|	P40	|	4148
|	P1	|	4030|	P11	|	4134|	P21	|	4128|	P31	|	4197|	P41	|	4134
|	P2	|	4157|	P12	|	4000|	P22	|	4179|	P32	|	4027|	P42	|	4198
|	P3	|	4105|	P13	|	4139|	P23	|	4087|	P33	|	4104|	P43	|	4101
|	P4	|	4059|	P14	|	4111|	P24	|	4176|	P34	|	4197|	P44	|	4157
|	P5	|	4237|	P15	|	4116|	P25	|	4181|	P35	|	4118|	P45	|	4079
|	P6	|	4148|	P16	|	4161|	P26	|	4031|	P36	|	4157|	P46	|	4095
|	P7	|	4149|	P17	|	4106|	P27	|	4070|	P37	|	4063|	P47	|	4103
|	P8	|	4156|	P18	|	4192|	P28	|	4073|	P38	|	4080|	P48	|	4122
|	P9	|	4185|	P19	|	4180|	P29	|	4068|	P39	|	4083|	P49	|	4058

Gesamtzahl: 206154

Durchschnitt: 4123

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
