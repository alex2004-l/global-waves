# GlobalWaves
___
##### Temă realizată de Lache Alexandra Florentina Georgiana, 321 CD
___

### Design pattern-uri folosite:
- Factory -> pentru crearea diferitelor tipuri de useri;
- Builder -> pentru crearea output-ului comenzilor;
- Observer -> pentru sistemul de notificari;
- Command -> pentru sistemul de pagini;

### Refactorizarea codului:

Am adăugat design pattern-ul Builder la implementarea clasei responsabile de output-ul comenzilor. Am ales acest design pattern, intrucat formatul mesajelor de output este alcatuit dintr-o serie de campuri obligatorii(numele comenzii + timestamp, daca ne raportam la etapa trecuta, respectiv command la etapa curenta) si o serie de campuri optionale.

### Implementare:
- Wrapped : Am implementat o clasa parinte pentru clasele wrapped si clase copil pentru fiecare tip de utilizator posibil. Perechile (nume, play-uri) sunt retinute cu ajutorul hashmap-urilor, iar metoda generale de incrementare a valorilor si de obtinere a top-urilor se afla in clasa parinte.
- endProgram : In clasa Admin se retine o lista cu datele de monetizare ale artistilor care au avut interactiuni cu vreun user din sistem, urmand ca la finalul executiei comenzilor sa se actualizeze datele de monetizare si sa fie afisate.
- buyMerch/seeMerch : La fiecare merch cumparat, se actualizeaza numarul de bucati vandute din clasa merch si se adauga denumirea merch-ului in lista produselor userului.
- Premium/Free users : se retin doua liste cu melodiile ascultate pe perioada free, respectiv premium, iar la intalnirea comenzii de adBreak pentru free, respectiv cancelPremium ori finalul programului pentru premium, se calculeaza revenue-ul fiecarui cantec si se golesc listele
- notifications : Pentru implementarea sistemului de notificari, am folosit pattern design-ul Observer. Fiecare creator de continut(obiectul observable) are o lista interna cu subscribers(observers), iar la adaugarea unui album, eveniment ori anunt sunt notificati toti subscriberii. Lista cu notificari este retinuta la nivelul userului, urmand sa fie stearsa dupa fiecare verificare a notificarilor.
- prev/nextpage : Pentru implementarea sistemului de pagini am folosit pattern design-ul Command. Am folosit acest patttern pentru functionalitatea de undo/redo a comenzilor, plus ca ar permite extinderea acestei functionalitati in cazul in care ar fi adaugate alte tipuri de comenzi pentru pagini.