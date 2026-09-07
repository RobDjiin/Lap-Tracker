# RobDjiin Racing – Offline PB Tracker

Native Android app. Keine Anmeldung, kein Server, keine Internet-Berechtigung.
Alle Rundenzeiten werden lokal in Android SharedPreferences gespeichert.

## Funktionen
- Assetto Corsa Competizione + Le Mans Ultimate
- Spiel / Strecke / Fahrzeug
- Zahleneingabe Min : Sek . Millisekunden
- PB-Historie mit Datum und optionaler Notiz
- Gesamtverbesserung
- Lange auf einen Rekord drücken = löschen
- Komplett offline

## APK bauen
1. Projektordner in Android Studio öffnen.
2. Gradle-Sync abwarten.
3. Build > Build App Bundle(s) / APK(s) > Build APK(s).
4. Die Debug-APK liegt anschließend unter app/build/outputs/apk/debug/app-debug.apk.

Hinweis: In der ChatGPT-Laufzeit war kein Android SDK/Gradle installiert, daher ist im ZIP
das vollständige Android-Studio-Projekt enthalten, aber keine vorgebaute APK.
