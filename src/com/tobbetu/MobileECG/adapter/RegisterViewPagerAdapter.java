package com.tobbetu.MobileECG.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.tobbetu.MobileECG.R;
import com.tobbetu.MobileECG.activities.RegisterActivity;
import com.tobbetu.MobileECG.service.Register;
import com.tobbetu.MobileECG.tasks.RegisterTask;
import com.tobbetu.MobileECG.util.Util;

import java.util.Date;

/**
 * Created by kanilturgut on 19/03/14.
 */
public class RegisterViewPagerAdapter extends FragmentPagerAdapter {

    static Context context = null;
    public static final String ARG_PAGE = "page";
    public static View view = null;

    public RegisterViewPagerAdapter(android.support.v4.app.FragmentManager fm, Context c) {
        super(fm);

        context = c;

    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {

        return PageFragment.create(position);
    }

    @Override
    public int getCount() {
        return 4;
    }

    //Fragments
    public static class PageFragment extends Fragment {

        int myPageNumber;

        public static PageFragment create(int pageNumber) {
            PageFragment pageFragment = new PageFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ARG_PAGE, pageNumber);
            pageFragment.setArguments(bundle);

            return pageFragment;
        }

        public PageFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            myPageNumber = getArguments().getInt(ARG_PAGE);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            switch (myPageNumber) {
                case 0:
                    view = inflater.inflate(R.layout.register_first_screen, container, false);
                    firstScreen();
                    break;
                case 1:
                    view = inflater.inflate(R.layout.register_second_screen, container, false);
                    secondScreen();
                    break;
                case 2:
                    view = inflater.inflate(R.layout.register_third_screen, container, false);
                    thirdScreen();
                    break;
                case 3:
                    view = inflater.inflate(R.layout.register_fourth_screen, container, false);
                    fourthScreen();
                    break;
            }

            return view;
        }
    }


    static boolean isEmpty(String... strings) {
        for (String string : strings) {
            if (string.equals(""))
                return true;
        }
        return false;
    }

    static void firstScreen() {

        final EditText etAdi = (EditText) view.findViewById(R.id.etRegisterUserName);
        final EditText etSoyadi = (EditText) view.findViewById(R.id.etRegisterUserSurname);
        final EditText etDogumTarihi = (EditText) view.findViewById(R.id.etRegisterBirthday);
        final EditText etAdres = (EditText) view.findViewById(R.id.etRegisterAddress);
        final EditText etTelNo = (EditText) view.findViewById(R.id.etRegisterPhoneNumber);
        Button bIleri = (Button) view.findViewById(R.id.bRegisterFirstFooterRight);

        bIleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String adi = etAdi.getText().toString();
                String soyadi = etSoyadi.getText().toString();
                String dogumtarihi = etDogumTarihi.getText().toString();
                String adres = etAdres.getText().toString();
                String tel = etTelNo.getText().toString();

                if (!isEmpty(adi, soyadi, dogumtarihi, adres, tel)) {
                    RegisterActivity.user.setName(adi.trim());
                    RegisterActivity.user.setSurname(soyadi.trim());
                    RegisterActivity.user.setBirthday(Util.stringToDate(dogumtarihi));
                    RegisterActivity.user.setAddress(adres.trim());
                    RegisterActivity.user.setPhoneNumber(tel.trim());

                    RegisterActivity.goNextPage(1);
                } else {
                    Toast.makeText(context, "Bütün alanları doldurunuz", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    static void secondScreen() {

        final EditText etUsername, etPassword;
        Button bGeri, bIleri;

        etUsername = (EditText) view.findViewById(R.id.etRegisterUsername);
        etPassword = (EditText) view.findViewById(R.id.etRegisterPassword);

        bIleri = (Button) view.findViewById(R.id.bRegisterSecondFooterRight);
        bIleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEmpty(etUsername.getText().toString(), etPassword.getText().toString())) {
                    RegisterActivity.user.setUsername(etUsername.getText().toString().trim());
                    RegisterActivity.user.setPassword(etPassword.getText().toString().trim());

                    RegisterActivity.goNextPage(2);
                } else {
                    Toast.makeText(context, "Bütün alanları doldurunuz", Toast.LENGTH_LONG).show();
                }
            }
        });

        bGeri = (Button) view.findViewById(R.id.bRegisterSecondFooterLeft);
        bGeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.goPreviousPage(0);
            }
        });

    }

    static void thirdScreen() {

        final EditText height, weight;
        final Spinner gender;
        Button bGeri, bIleri;

        String[] sex = {"Cinsiyetiniz", "Erkek", "Kadın"};

        gender = (Spinner) view.findViewById(R.id.sRegisterUserSex);
        gender.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, sex));
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0)
                    RegisterActivity.user.setSex(i - 1);
                else
                    RegisterActivity.user.setSex(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                RegisterActivity.user.setSex(0);
            }
        });

        height = (EditText) view.findViewById(R.id.etRegisterUserHeight);
        weight = (EditText) view.findViewById(R.id.etRegisterUserWeight);

        bIleri = (Button) view.findViewById(R.id.bRegisterThirdFooterRight);
        bIleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isEmpty(height.getText().toString(), weight.getText().toString())) {

                    String kilo = weight.getText().toString().trim();
                    if (kilo.contains("."))
                        kilo = kilo.substring(0, kilo.indexOf("."));

                    RegisterActivity.user.setWeight(Integer.parseInt(kilo));
                    RegisterActivity.user.setHeight(Integer.parseInt(height.getText().toString().trim()));

                    //bmi
                    double boyu = (double) Integer.parseInt(height.getText().toString().trim()) / 100;
                    double kilosu = (double) Integer.parseInt(kilo);

                    double bmi = (kilosu / (boyu * boyu));
                    RegisterActivity.user.setBmi(bmi);

                    RegisterActivity.goNextPage(3);
                } else {
                    Toast.makeText(context, "Bütün alanları doldurunuz", Toast.LENGTH_LONG).show();
                }

            }
        });

        bGeri = (Button) view.findViewById(R.id.bRegisterThirdFooterLeft);
        bGeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.goPreviousPage(1);
            }
        });

    }


    static void fourthScreen() {

        String[] activityFrequency = {"Aktivite Yapma Sıklığınız", "Yapmıyorum", "Fırsat Buldukça", "Düzenli"};
        String[] smokingFrequecny = {"Sigara İçme Sıklığınız", "Hiç", "Çok İçiyorum", "Arada Sırada", "İçmiyorum"};
        String[] alcoholFrequecny = {"Alkol Kullanma Sıklığınız", "Çok İçiyorum", "Arada Sırada", "İçmiyorum"};

        Spinner activity, smoking, alcohol;

        activity = (Spinner) view.findViewById(R.id.sRegisterUserActivity);
        activity.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, activityFrequency));
        activity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                RegisterActivity.user.setActivityFrequency(i - 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                RegisterActivity.user.setActivityFrequency(0);
            }
        });

        smoking = (Spinner) view.findViewById(R.id.sRegisterUserSmoking);
        smoking.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, smokingFrequecny));
        smoking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                RegisterActivity.user.setSmokingFrequency(i - 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                RegisterActivity.user.setSmokingFrequency(0);
            }
        });

        alcohol = (Spinner) view.findViewById(R.id.sRegisterUserAlcohol);
        alcohol.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, alcoholFrequecny));
        alcohol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                RegisterActivity.user.setAlcoholUsageFrequency(i - 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                RegisterActivity.user.setAlcoholUsageFrequency(0);
            }
        });

        final EditText kolestrolLDL, kolestrolHDL;

        kolestrolLDL = (EditText) view.findViewById(R.id.etRegisterUserKolestrolLDL);
        kolestrolHDL = (EditText) view.findViewById(R.id.etRegisterUserKolestrolHDL);

        final CheckBox yuksekTansiyon, seker;

        yuksekTansiyon = (CheckBox) view.findViewById(R.id.cRegisterUserHasHypertension);
        seker = (CheckBox) view.findViewById(R.id.cRegisterUserHasDiabets);


        Button geri, kaydet;

        geri = (Button) view.findViewById(R.id.bRegisterFourthFooterLeft);
        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.goPreviousPage(2);
            }
        });

        kaydet = (Button) view.findViewById(R.id.bRegisterFourthFooterRight);
        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEmpty(kolestrolHDL.getText().toString(), kolestrolLDL.getText().toString())) {
                    RegisterActivity.user.setKolesterolHDL(Integer.parseInt(kolestrolHDL.getText().toString().trim()));
                    RegisterActivity.user.setKolesterolLDL(Integer.parseInt(kolestrolLDL.getText().toString().trim()));
                } else {
                    RegisterActivity.user.setKolesterolHDL(0);
                    RegisterActivity.user.setKolesterolLDL(0);
                }

                if (yuksekTansiyon.isChecked())
                    RegisterActivity.user.setHasHypertension(true);
                else
                    RegisterActivity.user.setHasHypertension(false);


                if (seker.isChecked())
                    RegisterActivity.user.setHasDiabetes(true);
                else
                    RegisterActivity.user.setHasDiabetes(false);


                new RegisterTask(context).execute(RegisterActivity.user);
            }
        });
    }
}
