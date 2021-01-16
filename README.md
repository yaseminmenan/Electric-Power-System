# Proiect Energy System Etapa 2

## Despre

Proiectare Orientata pe Obiecte, Seriile CA, CD
2020-2021

<https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/proiect/etapa2>

Student: Menan Yasemin, 323CD

## Rulare teste

Clasa Test#main
  * ruleaza solutia pe testele din checker/, comparand rezultatele cu cele de referinta
  * ruleaza checkstyle

Biblioteci necesare pentru implementare:
* Jackson Core 
* Jackson Databind 
* Jackson Annotations
* Json Simple 0.3

## Implementare

### Entitati

In clasa Main se citesc datele primite in input, se realizeaza simularea
si apoi se scriu rezultatele simularii folosind ObjectMapper.

Clasa Input contine toate datele citite de clasa InputLoader din
fisierul de input, si tot in aceasta se realizeaza modificarile in timpul
fiecarei runde.

Simularea se bazeaza pe trei tipuri de entitati: consumatori (consumer), 
distribuitori (distributor) si producatori (producer). Consumatorii au 
nevoie de o sursa de energie pe care o obtin de la distribuitori, care la
randul lor au nevoie de energia produsa de producatori.

Fiecare entitate are propriul package ce contine o interfata, o clasa 
care contine datele corespunzatoare si un factory. 

Simularea se realizeaza in clasa Turn, si sunt utilizate clasele InitialTurn
si NormalTurn, iar in ambele clase operatiile pentru consumatori si
distribuitori se fac utilizand clasele ConsumerOperations si DistributorOperations

Pentru alegerea producatorilor de fiecare distribuitor se folosesc clasele
Green Strategy, Price Strategy, Quantity Strategy, care sorteaza producatorii
in functie de tipul de strategie al distribuitorului.

In package-ul common se gasesc clase utilizate de celelalte:
* Constants - constante utilizate in InputLoader sau pentru numere;
* Contract - contractul care se incheie intre un consumator si distribuitor;
* Debt - datoria consumatorului catre un distribuitor;
* MonthlyStat - contine lista de distribuitori a unui producator intr-o 
anumita luna;
* Update - contine lista cu schimbari ce trebuie realizate in input 
intr-o anumita luna.

### Flow

Simularea lunilor se face in clasa Turn. 

Mai intai se efectueaza runda initiala in clasa InitialTurn:
* Fiecare distribuitor isi alege producatorii astfel incat i se asigura energia
necesara, dupa care isi calculeaza costurile de productie si de contract.
* Se realizeaza operatiile pentru consumatori si pentru distribuitori.

Dupa aceea, se simuleaza rundele normale:
* Se realizeaza modificarile lunii respective, adica se adauga noi consumatori
si se modifica costul infrastructurii distribuitorilor;
* Se realizeaza operatiile pentru consumatori si pentru distribuitori;
* Se realizeaza modificarile pentru producatori, adica se schimba cantitatea de
energie furnizata fiecarui distribuitor;
* Daca se modifica cantitatea de energie a unui producator, distribuitorii acestuia
sunt anuntati si isi cauta alti producatori;
* La final, fiecare producator retine distribuitorii carora le-a produs energie
in acea luna si ii elimina pe cei care au dat faliment.

Daca toti distribuitorii dau faliment, simularea se opreste.

Operatiile pentru consumatori intr-o runda:
* Se gaseste distribuitorul cu cel mai ieftin cost al contractului;
* Se parcurge lista de consumatori, ignorandu-i pe cei care au dat faliment;
* Daca consumatorul nu are un contract sau ii expira contractul in acea luna,
va incheia contract cu cel mai ieftin distribuitor gasit mai sus;
* Consumatorul plateste pretul contractului pentru acea luna. Daca nu isi permite,
o sa aibe datorie si o va plati luna urmatoare. Daca nu isi poate plati datoria,
va da faliment.

Operatiile pentru distribuitori intr-o runda:
* Se parcurge lista de distribuitori, ignorandu-i pe cei care au dat faliment;
* Se elimina contractele expirate;
* Se calculeaza cheltuiele si se platesc;
* Daca bugetul distribuitorului este mai mic ca 0, acesta da faliment si ii se
elimina lista de contracte;
* Se elimina contractele cu consumatorii care au dat faliment;
* Se scade numarul de luni ramase din fiecare contract ramas cu 1;
* Se sorteaza lista cu contracte crescator mai intai dupa numarul de luni ramase
si apoi dupa id-ul consumatorilor.

### Design patterns

In cadrul proiectului am folosit pattern-urile Singleton, Factory, Strategy
si Observer.

Pentru crearea consumatorilor, distribuitorilor si producatorilor am folosit
pattern-ul Singleton Factory, pentru a avea o singura instanta a fiecarui
tip de factory.

Pattern-ul Strategy a fost aplicat pentru obtinerea listei cu cei mai buni
producatori in functie de tipul de energie dorit de distribuitor. Astfel, am
avut trei tipuri: GreenStrategy, PriceStrategy si QuantityStrategy, create
prin pattern-ul Factory.

Am folosit pattern-ul Observer pentru a notifica distribuitorii (observer)
atunci cand unul din producatorii sai (observable) isi modifica cantitatea
de energie. Dupa ce au fost notificati toti distribuitorii, acestia isi cauta
alti producatori.







