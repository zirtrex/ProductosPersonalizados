package net.zirtrex.productospersonalizados.Util;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import net.zirtrex.productospersonalizados.Activities.R;

/**
 * Created by Rafael on 03/05/2018.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the Fragments to the toolbar object
        if(toolbar != null) {
            toolbar.setLogo(R.mipmap.ic_launcher);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }
    }

    protected abstract int getLayoutResource();

    protected void setActionBarIcon(int iconRes){
        toolbar.setNavigationIcon(iconRes);
    }

}
