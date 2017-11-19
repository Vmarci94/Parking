# Mobil- és webes szoftverek (VIAUAC00)
## Parkolás
*Android alkalmazás, házi feladat 2017-18/1*

### követelmények
- Autentikáció (sosem árt :D)
- Térképen parkolási zónák mutatása, és a parkolási zónák különböző információinak megmutatása (Fizetési időszak, összeg, stb)
- Lokalizáció alapján a parkolási zóna meghatározása, gyors parkolási lehetőség megvalósítása
- sms küldési lehetőség a mobilparkolás szolgáltatás eléréséhez
- Autóink rendszámának elmentése, és aktív kijelölése, nem árt ha idegen autó gyors leparkolását is támogatjuk.
- Táblázatos megjelenítési a parkolási szokásainknak

### működő fnkciók és problémáik, információk
- Bejelentkezés Firebase autentikációval, email segítségével. (jelenleg mindent elfogad ami emailformátumú). De küld egyébként visszaigazoló emailt is.
- Google maps api használata, és helylokalizáció. Jelenleg nincs lekezelve az engedélykérés, csak annyira hogy nem fagy le. Úgyhogy manuálisan kell jelenleg engedélyt adni.
- Egész Zugló "új" parkolási térképét tartalmazza az erőforrásfájlok között. De jelenleg csak az ikea környéke van berakva, az egyszerűség kedvéért.
- Főbb gondok a KmlPolygon és a Simple google maps Polygon közti átjárással van.
