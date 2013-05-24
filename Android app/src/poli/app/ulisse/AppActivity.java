package poli.app.ulisse;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


public class AppActivity extends FragmentActivity {

    LinearLayout near;
    LinearLayout segnalation;


    //lista contenente i fragment per istanziare il viewPager
    List<Fragment> fragments = new ArrayList<Fragment>();

    private android.support.v4.view.PagerAdapter myAdapter;

    private ViewPager myPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        //crea i fragment e li aggiunge alla lista

        fragments.add(Fragment.instantiate(this, homePage.class.getName()));
        fragments.add(Fragment.instantiate(this, rankingPage.class.getName()));

        // creating adapter and linking to view pager
        this.myAdapter = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        myPager = (ViewPager) super.findViewById(R.id.pager);
        myPager.setAdapter(this.myAdapter);
        myPager.setCurrentItem(0);

        // upper bar button listener, allows direct page access
        Button button = (Button)findViewById(R.id.mapsBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myPager.setCurrentItem(0);   // go to central page
            }
        });
        button = (Button)findViewById(R.id.homeBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myPager.setCurrentItem(0);   // go to left page
            }
        });
        button = (Button)findViewById(R.id.rankBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myPager.setCurrentItem(1);   // go to right page
            }
        });

        Button mapBtn = (Button)findViewById(R.id.mapsBtn);
        mapBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(v.getContext(), MapsActivity.class));//salto del login
            }
        });
        //
    }


    public void onPage1() {

        // page 2 fragment update
        mapsPage f2 = (mapsPage) fragments.get(0);

        // if page 2 view is already created, update
        View v2 = f2.getView();
        if (v2!=null) {

        }

        // page 3 fragment update
        rankingPage f3 = (rankingPage) fragments.get(2);

        // if page 3 view is already created, update
        View v3 = f3.getView();

    }


}

