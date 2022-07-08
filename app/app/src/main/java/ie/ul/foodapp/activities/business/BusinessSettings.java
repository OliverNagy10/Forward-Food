package ie.ul.foodapp.activities.business;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.MapView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalTime;

import ie.ul.foodapp.R;
import ie.ul.foodapp.firebase.FirebaseLink;
import ie.ul.foodapp.model.Business;
import ie.ul.foodapp.utils.BitmapUtils;
import ie.ul.foodapp.utils.StringUtils;
import ie.ul.foodapp.utils.TimeSpan;

public class BusinessSettings extends AppCompatActivity implements TabLayout.OnTabSelectedListener, CompoundButton.OnCheckedChangeListener, TextWatcher {

    protected final int INTENT_CODE_PICK_IMAGE = 1;

    protected static class Updater {

        protected Runnable onUpdateTask;
        protected int skip = 0;
        protected boolean suspended = false;

        public Updater(Runnable onUpdateTask) {
            this.onUpdateTask = onUpdateTask;
        }

        public void skipNextUpdates(int nbrOfUpdates) {
            if (!suspended) {
                skip += nbrOfUpdates;
            }
        }

        public void suspend() {
            suspended = true;
        }

        public void resume() {
            suspended = false;
        }

        public void update() {
            if (!suspended) {
                if (skip > 0) {
                    skip--;
                } else {
                    onUpdateTask.run();
                }
            }
        }

    }

    /* UI parts */

    protected EditText name;

    protected ImageView banner;

    protected TabLayout daysOfTheWeek;
    protected int selectedTab;

    protected Switch openThisDay;

    protected LinearLayout openingHours;

    protected EditText from;

    protected EditText to;

    protected MapView map;

    protected FloatingActionButton save;

    protected FloatingActionButton cancel;

    /* attributes */

    protected Updater u;

    protected Business business;

    protected Business pendingChanges;

    /* onCreate */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_settings);

        /* UI parts */

        name = findViewById(R.id.Name);
        name.addTextChangedListener(this);

      //  banner = findViewById(R.id.buss_name);

        daysOfTheWeek = findViewById(R.id.tabLayout_daysOfTheWeek);
        daysOfTheWeek.addOnTabSelectedListener(this);

        openThisDay = findViewById(R.id.switch_openThisDay);
        openThisDay.setOnCheckedChangeListener(this);

        openingHours = findViewById(R.id.linearLayout_openingHours);

        from = findViewById(R.id.editTextTime_fromTime);
        from.addTextChangedListener(this);

        to = findViewById(R.id.editTextTime_toTime);
        to.addTextChangedListener(this);

        map = findViewById(R.id.mapView);

        save = findViewById(R.id.fab_save);

        /* attributes */

        u = new Updater(() -> {
            save.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
        });

        load(FirebaseLink.getCurrentBusiness());
    }

    /* layout update */

    protected void load(Business b) {
        u.suspend();

        business = b;
        pendingChanges = new Business(business); /* using the copy constructor */

        name.setText(pendingChanges.getName());

        banner.setImageBitmap(business.getBanner());

        selectTab(daysOfTheWeek.getTabAt(0));

        save.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);

        u.resume();
    }

    protected void selectTab(TabLayout.Tab tab) {
        if (tab ==  daysOfTheWeek.getTabAt(daysOfTheWeek.getSelectedTabPosition())) {
            updateOnTabSelect(daysOfTheWeek.getSelectedTabPosition());
        } else {
            daysOfTheWeek.selectTab(tab);
        }
    }

    protected void updateOnTabSelect(int tabNumber) {


        /* save current tab */
        save: {
            TimeSpan openTimeSpan;

            if (openThisDay.isChecked()) {
                int[] tmp = new int[4];
                LocalTime[] lts = new LocalTime[2];

                try {
                    tmp[0] = Integer.parseInt(from.getText().toString().split(":")[0]);
                    tmp[1] = Integer.parseInt(from.getText().toString().split(":")[1]);
                    tmp[2] = Integer.parseInt(to.getText().toString().split(":")[0]);
                    tmp[3] = Integer.parseInt(to.getText().toString().split(":")[1]);

                    if ((tmp[0] < 0) || (tmp[0] > 23)) throw new NumberFormatException();
                    if ((tmp[1] < 0) || (tmp[1] > 59)) throw new NumberFormatException();
                    if ((tmp[2] < 0) || (tmp[2] > 23)) throw new NumberFormatException();
                    if ((tmp[3] < 0) || (tmp[3] > 59)) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Invalid time format, should be 'hour:minutes'", Toast.LENGTH_LONG).show();
                    break save; // escape save process
                }

                lts[0] = LocalTime.of(tmp[0], tmp[1]);
                lts[1] = LocalTime.of(tmp[2], tmp[3]);
                if (lts[0].isAfter(lts[1])) {
                    Toast.makeText(getApplicationContext(), "Opening time is after closing time", Toast.LENGTH_LONG).show();
                    break save; // escape save process
                }

                openTimeSpan = new TimeSpan(lts[0], lts[1]);
            } else {
                openTimeSpan = null;
            }

            pendingChanges.setOpeningHours(openTimeSpan, selectedTab);
        }

        /* load new tab */
        selectedTab = tabNumber;

        switchDay(null != pendingChanges.getOpeningHours(selectedTab));
    }

    protected void switchDay(boolean open) {
        if (openThisDay.isChecked() == open) {
            updateOnDaySwitch(open);
        } else {
            u.skipNextUpdates(1);
            openThisDay.setChecked(open);
        }
    }

    protected void updateOnDaySwitch(boolean open) {
        if (open) {
            TimeSpan openTimeSpan = pendingChanges.getOpeningHours(selectedTab);
            if (openTimeSpan == null) {
                pendingChanges.setOpeningHours(new TimeSpan(), selectedTab);
                openTimeSpan = pendingChanges.getOpeningHours(selectedTab);
            }

            u.skipNextUpdates(2);
            from.setText(StringUtils.toString(openTimeSpan.getFrom()));
            to.setText(StringUtils.toString(openTimeSpan.getFrom()));
        } else {
            pendingChanges.setOpeningHours(null, selectedTab);
        }

        openingHours.setVisibility(open ? View.VISIBLE : View.GONE);
    }

    /* listeners */

 /*   public void OnClickFabCancel (View view) {
        load(business);
    }*/

 /*   public void OnClickFabSave (View view) {
        pendingChanges.setName(name.getText().toString());
        selectTab(daysOfTheWeek.getTabAt(daysOfTheWeek.getSelectedTabPosition()));

        load(pendingChanges);
    }
*/
   /* public void OnClickFabBusinessBanner (View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a business banner"), INTENT_CODE_PICK_IMAGE);
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            final Uri fileURI = data.getData();
            if (fileURI != null) {
                Bitmap newBanner;
                try {
                    newBanner = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileURI);
                } catch (Exception e) {
                    newBanner = null;
                }

                pendingChanges.setBanner(BitmapUtils.cropToAspectRatio(newBanner, 2.5f, 1f));
                banner.setImageBitmap(pendingChanges.getBanner());
                u.update();
            }
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        updateOnTabSelect(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}

    @Override
    public void onTabReselected(TabLayout.Tab tab) {}

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        u.update();
        updateOnDaySwitch(isChecked);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        u.update();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
