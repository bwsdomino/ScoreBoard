package com.example.todolist;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity {


    List<String> gracz;
    Map<String,Integer> gracze = new HashMap<>();
    ArrayAdapter<String> arrayAdapter;
    ListView listView;

    InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!Character.isLetterOrDigit(source.charAt(i))) {
                    return "";
                }
            }

            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gracz = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this,R.layout.element_listy,R.id.tekst,gracz);
        listView = findViewById(R.id.id_listView);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int which_item = position;
                final String[] nazwa =gracz.get(which_item).split(": ");
                final String a=nazwa[0];

                final EditText ile = new EditText(MainActivity.this);
                ile.setInputType(InputType.TYPE_CLASS_NUMBER);
                ile.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
                ile.setHint("wartość");

                AlertDialog.Builder d = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("GRACZ: "+a)
                        .setView(ile)
                        .setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (ile.getText().length() != 0) {
                                    int p = Integer.parseInt(String.valueOf(ile.getText()));
                                    if (gracze.get(a) + p > 2147483647) {
                                        gracze.put(a, 2147483647);
                                    } else {
                                        gracze.put(a, gracze.get(a) + p);
                                    }
                                    gracz.set(which_item, a + ": " + gracze.get(a));
                                    arrayAdapter.notifyDataSetChanged();
                                    sortuj();
                                }
                                else{}
                            }

                        })
                        .setNegativeButton("Odejmij", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (ile.getText().length() != 0) {
                                    int p = Integer.parseInt(String.valueOf(ile.getText()));
                                    if (gracze.get(a) - p < -214748364) {
                                        gracze.put(a, -214748364);
                                    } else {
                                        gracze.put(a, gracze.get(a) - p);
                                    }
                                    gracz.set(which_item, a + ": " + gracze.get(a));
                                    arrayAdapter.notifyDataSetChanged();
                                    sortuj();
                                } else {}
                            }
                        })
                        .setNeutralButton("Usuń gracza", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                gracz.remove(which_item);
                                gracze.remove(a);
                                arrayAdapter.notifyDataSetChanged();
                                sortuj();
                            }
                        });
                AlertDialog dialog1 = d.create();
                dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                        dialog1.show();



                }
        });
    }
    public void addItemToList(View view){
        final EditText im = new EditText(MainActivity.this);
                im.setFilters(new InputFilter[]{filter});
                im.setHint("Podaj imię");

        AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this)

                .setTitle("Dodawanie")
                .setView(im)
                .setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(im.getText().length()!=0 && gracze.get(im.getText().toString())==null){
                            gracze.put(im.getText().toString(),0);
                            gracz.add(im.getText().toString() +": "+gracze.get(im.getText().toString()));
                            arrayAdapter.notifyDataSetChanged();
                            sortuj();}

                        else {}
                    }

                });
        AlertDialog dialog = b.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
            public static Map<String, Integer> sortByValue(Map<String, Integer> gracze) {
        return gracze.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sortuj(){
        final Map<String, Integer> sortedByCount=sortByValue(gracze);
        Integer o = 0;
        for (Map.Entry<String, Integer> myk : sortedByCount.entrySet()) {
            String gk = myk.getKey();
            Integer gw = myk.getValue();
            gracz.set(o, gk + ": " + gw);
            arrayAdapter.notifyDataSetChanged();
            o++;

        }
    }
    }








