package net.zirtrex.productospersonalizados.Adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FormasPagoAdapter extends FragmentStatePagerAdapter{

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> tabsTitulos = new ArrayList<>();

    public FormasPagoAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragments(Fragment fragment, String titulo){
        fragmentList.add(fragment);
        tabsTitulos.add(titulo);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabsTitulos.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
