## Mobilná aplikácia pre akvizíciu a úpravu HDR fotografií

Keďže som v poslednej dobe začal iba pracovať s Camera2 API, tak je zdrojový kód rozdelený na 2 "Main" súbory.

V `AndroidManifest` na riadku 18 sa prepisuje `activity name` medzi
- `.Main` - práca s Camera2
- `._MainHdr` - HDR spracovanie 4 obrázkov "lampička"
- (`._MainExtPhoto` bol prvý pokus snímania pomocou externej apk fototaparátu)

### Snímanie

Pre nás podstatným súborom je trieda `CameraFragment` kde sú metody oddelené podľa logiky.

Z callbackov je podstatný `onImageAvailable` v `ImageReader.OnImageAvailableListener`i, kde získavame buffer
vytvorenej fotografie. Získana foto sa zatiaľ zobrazuje iba na displeji pomocou triedy `ShowImage`.

V metodach `onViewCreated` a `onActivityCreated` sa budu inicializovať potrebne premenne.

V metode `captureStillPicture` sa vytvára List požiadavkov, kde každý má rovnaké nastavenia, okrem expozície.
Toto nastavenie by malo byť pomocou `SENSOR_EXPOSURE_TIME` ale to mi nefunguje (ako som ukazoval na konzultácii),
takže to zatiaľ funguje pomocou `CONTROL_AE_EXPOSURE_COMPENSATION`.
Tento List požiadavkov odovzdávame funkcii `captureBurst()` a výsledok zachytí callback `onImageAvailable`.

Z textu práce:

Pre zobrazovanie scény v reálnom čase je vytvorená `cameraPreviewSession`, s automatickými nastaveniami zaostrenia a expozície. Pri snímaní fotografie sa uzamkne zaostrenie na scénu, vytvorí sa zoznam požiadavkov s nastaveniami manuálneho módu a expozícií, ktoré majú dočasne staticky nastavenú hodnotu. Tento zoznam požiadavkov je poslaný do metódy `captureBurst()` pre `captureSession`, ktorý vytvorí sériu snímok. Callback `onImageAvailable()` nám umožňuje následne spracovať zachytené snímky, ako napríklad zobraziť ich, alebo uložiť.

### _MainHdr

(Práca na vytváraní HDR je mnou aktuálne pozastavená kvôli práci s Camera2 API.)

Algoritmus začína, až keď sa v `onResume` úspešne otestuje funkčnosť knižnice OpenCV. Komentár "HDRCV" značí začiatok kódu, kde sa HDR vytvára pomocou OpenCV metód. "SolveG" značí začiatok kódu, ktorý pracuje na alegoritme P. Debeveca.

#### HDRCV

Trieda pracuje s OpenCV a zobrazí HDR obrázok použitím E. Reinhardovho TMO.

#### HDR

Trieda riadí algoritmus na vytvorenie HDR formátu. Pracuje s triedami:
- `SolveG` aj v literatúre označovaná funkcia, ktorá získa krivku odozvy fotoaparátu pre každý kanál na základe kvadratickej objektívnej funcie 2.6 (v mojej práci) s váhovou funkciou 2.5. Kod vytvorený na základe matlab kodu solveG.m z knihy Advanced HDR (zdroj [1] v práci). Výsledok sme konzultovali a zhodnotili, že záleží na výbere vzoriek pre tento algoritmus.
- `ValueSelector` vyberal 50 vzoriek pre získanie krivky odozvy. S touto triedou sa bude experimentovať a vyberať vzorky rôznymi spôsobmi (opísanými v práci v kapitole 2.4).
- `Merge` vytvára HDR obsah pre každý kanál pomocou funkcie 2.8 (v práci).

#### Histogram

Obsahuje pomocné funkcie, ktoré pomocou OpenCV Mat vyjresľújú grafy a krivky.

#### Ospravedňujem sa za otrasný kód, prioritne teraz riešim to Camera2 API a keď dokončím túto vec, tak súbory budú celkovo nahradené novými a so zrefaktorovaným kódom.

#### AUTOR

Patrik Michalák (xmicha65)
