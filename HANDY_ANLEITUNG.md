# APK nur mit dem Handy bauen

## Einmalig
1. Auf github.com anmelden und ein neues Repository `RobDjiin-Racing` erstellen.
2. Den INHALT dieses ZIPs in das Repository hochladen. Wichtig: `.github/workflows/build-apk.yml`
   muss ebenfalls im Repository landen.
3. GitHub öffnet automatisch den Reiter **Actions** und startet nach dem Upload den Build.

## APK herunterladen
1. Repository > **Actions**
2. Workflow **Build RobDjiin Racing APK**
3. Den neuesten grünen Lauf öffnen.
4. Unten bei **Artifacts** auf **RobDjiin-Racing-APK** tippen.
5. ZIP herunterladen und entpacken. Darin liegt `app-debug.apk`.
6. APK auf Android öffnen und installieren. Falls Android fragt, für den verwendeten Browser/
   Dateimanager einmalig **Unbekannte Apps installieren** erlauben.

## Später erneut bauen
Actions > Build RobDjiin Racing APK > Run workflow.

Die App selbst benötigt keine Internet-Berechtigung; die PB-Daten bleiben lokal.
