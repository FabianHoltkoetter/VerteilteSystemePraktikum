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
|	P0	|	6111
|	P1	|	6125
|	P2	|	6118
|	P3	|	6117
|	P4	|	6125
Gesamtzahl: 30596

Durchschnitt: 6119

#### Messung 2 (30, 0, 30)

| Philosoph | Essvorgänge | Philosoph | Essvorgänge | Philosoph | Essvorgänge
| --- | --- | --- | --- | --- | ---
|	P0	|	6126|	P10	|	6121|	P20	|	6120
|	P1	|	6120|	P11	|	6126|	P21	|	6126
|	P2	|	6118|	P12	|	6124|	P22	|	6124
|	P3	|	6120|	P13	|	6123|	P23	|	6123
|	P4	|	6121|	P14	|	6123|	P24	|	6120
|	P5	|	6124|	P15	|	6123|	P25	|	6121
|	P6	|	6117|	P16	|	6118|	P26	|	6121
|	P7	|	6123|	P17	|	6122|	P27	|	6121
|	P8	|	6120|	P18	|	6118|	P28	|	6117
|	P9	|	6123|	P19	|	6120|	P29	|	6124
Gesamtzahl: 183647

Durchschnitt: 6121

#### Messung 3 (50, 0, 10)

| Philosoph | Essvorgänge | Philosoph | Essvorgänge | Philosoph | Essvorgänge | Philosoph | Essvorgänge | Philosoph | Essvorgänge
| --- | --- | --- | --- | --- | --- | --- | --- | --- | ---
|	P0	|	4816|	P10	|	4780|	P20	|	4798|	P30	|	4797|	P40	|	4811
|	P1	|	4768|	P11	|	4803|	P21	|	4776|	P31	|	4788|	P41	|	4800
|	P2	|	4819|	P12	|	4797|	P22	|	4804|	P32	|	4779|	P42	|	4806
|	P3	|	4780|	P13	|	4767|	P23	|	4789|	P33	|	4776|	P43	|	4785
|	P4	|	4809|	P14	|	4809|	P24	|	4800|	P34	|	4766|	P44	|	4767
|	P5	|	4745|	P15	|	4806|	P25	|	4762|	P35	|	4749|	P45	|	4761
|	P6	|	4746|	P16	|	4774|	P26	|	4761|	P36	|	4817|	P46	|	4810
|	P7	|	4770|	P17	|	4824|	P27	|	4791|	P37	|	4758|	P47	|	4794
|	P8	|	4786|	P18	|	4794|	P28	|	4782|	P38	|	4784|	P48	|	4812
|	P9	|	4823|	P19	|	4833|	P29	|	4781|	P39	|	4755|	P49	|	4759
Gesamtzahl: 239367

Durchschnitt: 4787