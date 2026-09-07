\
package de.robdjiin.racing;

import android.app.*;
import android.os.Bundle;
import android.content.*;
import android.graphics.Color;
import android.view.*;
import android.widget.*;
import org.json.*;
import java.text.*;
import java.util.*;

public class MainActivity extends Activity {
    Spinner game, track, car;
    EditText minutes, seconds, millis, note;
    TextView best, stats;
    LinearLayout history;
    SharedPreferences prefs;

    final Map<String,String[]> tracks = new LinkedHashMap<>();
    final Map<String,String[]> cars = new LinkedHashMap<>();

    @Override public void onCreate(Bundle b) {
        super.onCreate(b); setContentView(R.layout.activity_main);
        prefs=getSharedPreferences("pbdata", MODE_PRIVATE);
        game=findViewById(R.id.game); track=findViewById(R.id.track); car=findViewById(R.id.car);
        minutes=findViewById(R.id.minutes); seconds=findViewById(R.id.seconds);
        millis=findViewById(R.id.millis); note=findViewById(R.id.note);
        best=findViewById(R.id.best); stats=findViewById(R.id.stats); history=findViewById(R.id.history);

        tracks.put("Assetto Corsa Competizione", new String[]{
          "Monza","Spa-Francorchamps","Nürburgring","Nürburgring 24h","Imola","Brands Hatch",
          "Barcelona","Misano","Paul Ricard","Silverstone","Zandvoort","Hungaroring","Mount Panorama",
          "Watkins Glen","Valencia","Kyalami","Laguna Seca","Suzuka","Oulton Park","Donington Park",
          "Snetterton","COTA","Red Bull Ring"
        });
        cars.put("Assetto Corsa Competizione", new String[]{
          "Ferrari 296 GT3","BMW M4 GT3","Porsche 911 (992) GT3 R","McLaren 720S GT3 Evo",
          "Mercedes-AMG GT3 EVO","Lamborghini Huracán GT3 EVO2","Audi R8 LMS GT3 evo II",
          "Aston Martin V8 Vantage GT3","Bentley Continental GT3","Ferrari 488 GT3 Evo",
          "Honda NSX GT3 Evo","Lexus RC F GT3","Nissan GT-R Nismo GT3","BMW M6 GT3",
          "McLaren 650S GT3","Porsche 991 II GT3 R","Ford Mustang GT3"
        });
        tracks.put("Le Mans Ultimate", new String[]{
          "Le Mans – Circuit de la Sarthe","Spa-Francorchamps","Monza","Sebring","Bahrain","Portimão",
          "Fuji Speedway","Imola","Interlagos","Circuit of the Americas","Lusail","Barcelona-Catalunya",
          "Paul Ricard","Silverstone","Daytona International Speedway","WeatherTech Raceway Laguna Seca"
        });
        cars.put("Le Mans Ultimate", new String[]{
          "Ferrari 499P LMH","Toyota GR010 Hybrid LMH","Porsche 963 LMDh","Cadillac V-Series.R LMDh",
          "Cadillac V-Series.R Evo","Peugeot 9X8 LMH","Peugeot 9X8 2024 LMH","BMW M Hybrid V8 LMDh",
          "Alpine A424 LMDh","Aston Martin Valkyrie AMR LMH","Isotta Fraschini Tipo 6-C",
          "Glickenhaus SCG 007 LMH","Vanwall Vandervell 680","Oreca 07 Gibson LMP2",
          "Ferrari 296 LMGT3","BMW M4 LMGT3","Porsche 911 GT3 R LMGT3",
          "Aston Martin Vantage AMR LMGT3 Evo","Corvette Z06 LMGT3.R","Ford Mustang LMGT3",
          "Lexus RC F LMGT3","McLaren 720S LMGT3 Evo","Mercedes-AMG LMGT3 Evo",
          "Lamborghini Huracán LMGT3 Evo2","Ferrari 488 GTE Evo","Porsche 911 RSR-19 GTE",
          "Corvette C8.R GTE","Aston Martin Vantage AMR GTE","Ligier JS P325 LMP3",
          "Ginetta G61-LT-P3 Evo LMP3","Duqueine D09 LMP3","ADESS AD25 LMP3"
        });

        setSpinner(game, tracks.keySet().toArray(new String[0]));
        game.setOnItemSelectedListener(listener(true));
        track.setOnItemSelectedListener(listener(false));
        car.setOnItemSelectedListener(listener(false));
        findViewById(R.id.save).setOnClickListener(v -> saveTime());
        refreshSelectors(); refresh();
    }

    AdapterView.OnItemSelectedListener listener(boolean gameChanged) {
        return new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                if(gameChanged) refreshSelectors(); refresh();
            }
            public void onNothingSelected(AdapterView<?> p) {}
        };
    }

    void setSpinner(Spinner s, String[] vals) {
        ArrayAdapter<String> a=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, vals) {
            @Override public View getView(int pos, View cv, android.view.ViewGroup parent) {
                TextView t=(TextView)super.getView(pos,cv,parent); t.setTextColor(Color.WHITE); t.setTextSize(16); return t;
            }
        };
        s.setAdapter(a);
    }

    void refreshSelectors() {
        String g=(String)game.getSelectedItem(); if(g==null)return;
        setSpinner(track, tracks.get(g)); setSpinner(car, cars.get(g));
    }

    String key() { return game.getSelectedItem()+"|"+track.getSelectedItem()+"|"+car.getSelectedItem(); }

    JSONArray load() {
        try { return new JSONArray(prefs.getString("records","[]")); }
        catch(Exception e) { return new JSONArray(); }
    }

    void save(JSONArray a) { prefs.edit().putString("records",a.toString()).apply(); }

    void saveTime() {
        try {
            int m=Integer.parseInt(minutes.getText().toString());
            int s=Integer.parseInt(seconds.getText().toString());
            String z=millis.getText().toString();
            if(s<0||s>59||z.length()<1||z.length()>3) throw new Exception();
            while(z.length()<3) z += "0";
            int ms=m*60000+s*1000+Integer.parseInt(z);
            JSONArray a=load(); JSONObject o=new JSONObject();
            o.put("key",key()); o.put("ms",ms); o.put("date",new SimpleDateFormat("yyyy-MM-dd",Locale.GERMANY).format(new Date()));
            o.put("note",note.getText().toString()); a.put(o); save(a);
            seconds.setText(""); millis.setText(""); note.setText(""); refresh();
            Toast.makeText(this,"Zeit gespeichert 🏁",Toast.LENGTH_SHORT).show();
        } catch(Exception e) { Toast.makeText(this,"Bitte gültige Zeit eingeben",Toast.LENGTH_SHORT).show(); }
    }

    String fmt(int ms) {
        return String.format(Locale.GERMANY,"%d:%02d.%03d",ms/60000,(ms%60000)/1000,ms%1000);
    }

    void refresh() {
        if(game.getSelectedItem()==null||track.getSelectedItem()==null||car.getSelectedItem()==null)return;
        JSONArray a=load(); ArrayList<JSONObject> all=new ArrayList<>();
        try { for(int i=0;i<a.length();i++){JSONObject o=a.getJSONObject(i);if(key().equals(o.getString("key")))all.add(o);} } catch(Exception ignored){}
        all.sort(Comparator.comparing(o->{try{return o.getString("date");}catch(Exception e){return "";}}));
        int running=Integer.MAX_VALUE; ArrayList<JSONObject> pbs=new ArrayList<>();
        for(JSONObject o:all) try {int x=o.getInt("ms");if(x<running){running=x;pbs.add(o);}}catch(Exception ignored){}
        history.removeAllViews();
        if(pbs.isEmpty()){best.setText("PB —");stats.setText("Noch keine Zeit für diese Kombination.");return;}
        try {
            int first=pbs.get(0).getInt("ms"), last=pbs.get(pbs.size()-1).getInt("ms");
            best.setText("PB  "+fmt(last));
            stats.setText("PBs: "+pbs.size()+"   •   Gesamt verbessert: "+String.format(Locale.GERMANY,"%.3f s",(first-last)/1000.0));
        } catch(Exception ignored){}
        Collections.reverse(pbs);
        for(JSONObject o:pbs) try {
            TextView t=new TextView(this); t.setTextColor(Color.WHITE);t.setTextSize(17);t.setPadding(12,16,12,16);
            String n=o.optString("note",""); t.setText("🏆  "+fmt(o.getInt("ms"))+"   •   "+o.getString("date")+(n.isEmpty()?"":"\n"+n));
            t.setOnLongClickListener(v->{ confirmDelete(o); return true; }); history.addView(t);
        } catch(Exception ignored){}
    }

    void confirmDelete(JSONObject target) {
        new AlertDialog.Builder(this).setTitle("Eintrag löschen?")
          .setMessage("Diesen Rekord wirklich entfernen?")
          .setNegativeButton("Abbrechen",null)
          .setPositiveButton("Löschen",(d,w)->{
              JSONArray old=load(), neu=new JSONArray();
              for(int i=0;i<old.length();i++) try {JSONObject x=old.getJSONObject(i);if(x!=target && !x.toString().equals(target.toString()))neu.put(x);}catch(Exception ignored){}
              save(neu);refresh();
          }).show();
    }
}
