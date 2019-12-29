package com.yigitcakmak.diet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class diet extends Fragment{
    EditText boy, kilo;
    TextView sonuc,textView5;
    Button hesapla;
    RadioButton kadin,erkek;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diet, container, false);

        textView5=view.findViewById(R.id.textView5);
        kadin=view.findViewById(R.id.kadin);
        erkek=view.findViewById(R.id.erkek);
        sonuc=view.findViewById(R.id.sonuc);
        boy = view.findViewById(R.id.edt_boy);
        kilo = view.findViewById(R.id.edt_kilo);
        hesapla = view.findViewById(R.id.btn_hesapla_begin);
        hesapla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kadin.isChecked()) {
                    float sayi1 = Float.parseFloat(boy.getText().toString());
                    float sayi2 = Float.parseFloat(kilo.getText().toString());
                    float islem=(sayi2/(sayi1*sayi1))-2;
                    sonuc.setText(String.valueOf(islem));
                    if (islem>25)
                    {
                        textView5.setText("İSVEÇ DİYETİ YAPIN");
                    }
                    if (islem<=25 && islem>=19)
                    {
                        textView5.setText("Kilo İdeal Diyete Gerek Yok");
                    }
                    if (islem<19)
                    {
                        textView5.setText("Zayıfsınız. Bulk değeri yapınız.");
                    }

                }
                if(erkek.isChecked()) {
                    float sayi1 = Float.parseFloat(boy.getText().toString());
                    float sayi2 = Float.parseFloat(kilo.getText().toString());
                    float islem=sayi2/(sayi1*sayi1);
                    sonuc.setText(String.valueOf(islem));
                    if (islem>25)
                    {
                        textView5.setText("İSVEÇ DİYETİ YAPIN");
                    }
                    if (islem<=25 && islem>=19)
                    {
                        textView5.setText("Kilo İdeal Diyete Gerek Yok");
                    }
                    if (islem<19)
                    {
                        textView5.setText("Zayıfsınız. Bulk değeri yapınız.");
                    }
                }

            }
        });

        return view;
    }
}
