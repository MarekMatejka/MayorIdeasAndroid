package mm.mayorideas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;

import com.mikepenz.materialdrawer.DrawerBuilder;

public class OverviewActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        new DrawerBuilder().withActivity(this).build();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, IdeaListFragment.newInstance())
                .commit();
    }

    public void addNewIdea(View v) {
        Intent intent = new Intent(this, NewIdeaActivity.class);
        startActivity(intent);
    }
}
