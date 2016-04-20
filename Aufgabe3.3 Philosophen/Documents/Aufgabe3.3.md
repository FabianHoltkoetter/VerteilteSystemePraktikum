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
| --- | ---
|       P0      |       6128
|       P1      |       6126
|       P2      |       6128
|       P3      |       6125
|       P4      |       6123
Gesamtzahl: 30630

Durchschnitt: 6126

#### Messung 2 (30, 0, 30)
| Philosoph | Essvorgänge | Philosoph | Essvorgänge | Philosoph | Essvorgänge
| --- | --- | --- | --- | --- | ---
|       P0      |       6129|   P10     |       6126|   P20     |       6133
|       P1      |       6132|   P11     |       6138|   P21     |       6135
|       P2      |       6135|   P12     |       6132|   P22     |       6129
|       P3      |       6131|   P13     |       6126|   P23     |       6129
|       P4      |       6132|   P14     |       6129|   P24     |       6135
|       P5      |       6134|   P15     |       6134|   P25     |       6135
|       P6      |       6137|   P16     |       6126|   P26     |       6132
|       P7      |       6132|   P17     |       6135|   P27     |       6135
|       P8      |       6132|   P18     |       6126|   P28     |       6134
|       P9      |       6132|   P19     |       6130|   P29     |       6136
Gesamtzahl: 183961

Durchschnitt: 6132

#### Messung 3 (50, 0, 10)
| Philosoph | Essvorgänge | Philosoph | Essvorgänge | Philosoph | Essvorgänge | Philosoph | Essvorgänge | Philosoph | Essvorgänge
| --- | --- | --- | --- | --- | --- | --- | --- | --- | ---
|       P0      |       4967|   P10     |       4950|   P20     |       4957|   P30     |       4987|   P40     |       4994
|       P1      |       4991|   P11     |       4983|   P21     |       4992|   P31     |       4937|   P41     |       4974
|       P2      |       4977|   P12     |       4978|   P22     |       4985|   P32     |       4957|   P42     |       4957
|       P3      |       4979|   P13     |       4967|   P23     |       4986|   P33     |       4969|   P43     |       4969
|       P4      |       4981|   P14     |       4976|   P24     |       4995|   P34     |       4953|   P44     |       4972
|       P5      |       4977|   P15     |       4974|   P25     |       4974|   P35     |       4959|   P45     |       4956
|       P6      |       4978|   P16     |       4956|   P26     |       4986|   P36     |       4990|   P46     |       4995
|       P7      |       4973|   P17     |       4962|   P27     |       4983|   P37     |       4960|   P47     |       4966
|       P8      |       4978|   P18     |       4990|   P28     |       4980|   P38     |       4968|   P48     |       4971
|       P9      |       4993|   P19     |       4968|   P29     |       4980|   P39     |       4966|   P49     |       4970
Gesamtzahl: 248686

Durchschnitt: 4973