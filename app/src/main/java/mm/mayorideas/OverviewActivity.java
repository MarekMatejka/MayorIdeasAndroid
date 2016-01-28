package mm.mayorideas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class OverviewActivity extends AppCompatActivity {

    private Drawer mResult;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createDrawer(toolbar);

        mFragmentManager = getSupportFragmentManager();
        switchFragments(0);
    }

    private void createDrawer(Toolbar toolbar) {
        mResult = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(createAccountHeader())
                .addDrawerItems(
                        createDrawerItem(GoogleMaterial.Icon.gmd_fire, R.string.hot_ideas, R.color.flame),
                        createDrawerItem(FontAwesome.Icon.faw_star, R.string.top_ideas, R.color.yellow),
                        createDrawerItem(GoogleMaterial.Icon.gmd_pin, R.string.ideas_on_map, R.color.holo_green_light),
                        createDrawerItem(GoogleMaterial.Icon.gmd_home, R.string.my_ideas, R.color.mayorideas_blue),
                        createDrawerItem(FontAwesome.Icon.faw_heart, R.string.following, android.R.color.holo_red_dark),
                        createDrawerItem(FontAwesome.Icon.faw_tags, R.string.all_categories, R.color.mayorideas_blue_dark),
                        new DividerDrawerItem(),
                        createDrawerItem(GoogleMaterial.Icon.gmd_info, R.string.about, android.R.color.darker_gray)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switchFragments(position-1);
                        return false;
                    }
                })
                .build();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mResult.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }

    private AccountHeader createAccountHeader() {
       return new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.mayorideas_blue)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("Marek Matejka")
                                .withEmail("marek@matejka.com")
                                .withIcon(R.drawable.ic_launcher)
                )
               .withSelectionListEnabledForSingleProfile(false)
               .withProfileImagesClickable(false)
               .build();
    }

    private void switchFragments(int position) {
        mFragmentManager
                .beginTransaction()
                .replace(R.id.container, getFragment(position))
                .commit();
        getSupportActionBar().setTitle(getFragmentTitle(position));
    }

    private Fragment getFragment(int position) {
        switch (position) {
            case 0: return TrendingIdeasListFragment.newInstance();
            case 1: return Top10IdeasListFragment.newInstance();
            case 2: return MapIdeasFragment.newInstance();
            case 3: return MyIdeasListFragment.newInstance();
            case 4: return FollowingIdeasListFragment.newInstance();
            default: return Top10IdeasListFragment.newInstance();
        }
    }

    private int getFragmentTitle(int position) {
        switch (position) {
            case 0: return R.string.hot_ideas;
            case 1: return R.string.top_ideas;
            case 2: return R.string.ideas_on_map;
            case 3: return R.string.my_ideas;
            case 4: return R.string.following;
            case 5: return R.string.all_categories;
            //case 6: Divider Item = no action
            case 7: return R.string.about;
            default: return R.string.hot_ideas;
        }
    }

    private PrimaryDrawerItem createDrawerItem(IIcon icon, int text, int iconColor) {
        return new PrimaryDrawerItem()
                .withIcon(icon)
                .withIconColorRes(iconColor)
                .withSelectedIconColorRes(iconColor)
                .withName(getString(text))
                .withTextColorRes(android.R.color.black)
                .withSelectedTextColorRes(android.R.color.black);
    }

    public void addNewIdea(View v) {
        Intent intent = new Intent(this, NewIdeaActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = mResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mResult != null && mResult.isDrawerOpen()) {
            mResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
