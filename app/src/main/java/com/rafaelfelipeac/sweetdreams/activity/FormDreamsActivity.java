package com.rafaelfelipeac.sweetdreams.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.rafaelfelipeac.sweetdreams.DAO.DreamDAO;
import com.rafaelfelipeac.sweetdreams.R;
import com.rafaelfelipeac.sweetdreams.adapter.PagerAdapter;
import com.rafaelfelipeac.sweetdreams.helper.FormDreamsHelper;
import com.rafaelfelipeac.sweetdreams.models.Dream;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class FormDreamsActivity extends BaseActivity  {

    private FormDreamsHelper helper;
    private Dream dream;
    private Dream dreamDay;
    private Dream dreamAux;
    private TagView tagGroup;
    private ScrollView sv;
    private ActionBar toolbar;

    int yearX;
    int monthX;
    int dayX;

    private final int DIALOG_DATE_ID = 0;
    private static final int REQUEST_RECORD_AUDIO = 0;
    private static String AUDIO_FILE_PATH;

    public static final int RequestPermissionCode = 1;

    String RandomAudioFileName = "abcdefghijklmnopqrstuvwxyz";

    Random random;

    private RelativeLayout llAudio;

    private View viewTextFragment;

    private boolean hasAudio = false;

    private Button btnPlayAudio;
    private Button btnDeleteAudio;

    private LinearLayout llButtons;

    private MediaPlayer mediaPlayer;

    private String lastPathAudio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_dreams);

        getSupportActionBar().setElevation(0);

        llAudio = (RelativeLayout) LayoutInflater.from(getApplication()).inflate(R.layout.fragment_form_audio, null);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.form_dreams_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.form_dreams_fragment_text));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.form_dreams_fragment_audio));
        tabLayout.setForegroundGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.form_dreams_pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        AUDIO_FILE_PATH =  Environment.getExternalStorageDirectory().getPath() + "/" + CreateRandomAudioFileName(10) + ".wav";
        lastPathAudio = "";

        final Calendar cal = Calendar.getInstance();

        toolbar = getSupportActionBar();

        sv = (ScrollView) findViewById(R.id.form_dreams);
        tagGroup = (TagView) findViewById(R.id.tag_group_form);

        yearX = (cal.get(Calendar.YEAR));
        monthX = (cal.get(Calendar.MONTH));
        dayX = (cal.get(Calendar.DAY_OF_MONTH));

        showDatePickerDialog();

        TextView date = (TextView) findViewById(R.id.form_dreams_date);
        date.setText(String.format("%02d", dayX) + "/" + String.format("%02d", monthX+1) + "/" + yearX);

        helper = new FormDreamsHelper(this);

        Intent intent = getIntent();
        dream = (Dream) intent.getSerializableExtra("dream");

        dreamDay = (Dream) intent.getSerializableExtra("dreamDay");
        if(dreamDay != null)
            onCreateDialog(DIALOG_DATE_ID);

        if(dream != null) {
            helper.makeDream(dream);
            toolbar.setTitle(R.string.form_edit_activity);

            if(!dream.getAudioPath().isEmpty()) {
                hasAudio = true;
                lastPathAudio = dream.getAudioPath();
                makePlayDeleteButtons();
            }
        }
        else
            toolbar.setTitle(R.string.form_activity);

        mediaPlayer = new MediaPlayer();

        tagGroup.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {

            @Override
            public void onTagDeleted(final TagView view, com.cunoraz.tagview.Tag tag, final int position) {
                view.remove(position);
            }
        });

        Button btnNewTag = (Button) findViewById(R.id.form_dreams_btnNewTag);
        btnNewTag.setBackgroundResource(R.drawable.buttonwhite);
        btnNewTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newTag = (EditText) findViewById(R.id.form_dreams_tag);

                if(!newTag.getText().toString().isEmpty() && newTag.getText().toString().trim().length() > 0) {
                    com.cunoraz.tagview.Tag tag = new Tag(newTag.getText().toString());
                    tag.radius = 10f;
                    tag.layoutColor = Color.rgb(95, 170, 223);
                    tag.isDeletable = true;
                    newTag.setText("");
                    tagGroup.addTag(tag);
                }
                else
                    Snackbar.make(sv, R.string.form_dreams_empty_tag, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        recordAudio();
                    }
                    else {
                        Toast.makeText(FormDreamsActivity.this, R.string.form_dreams_permission_denied, Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        overridePendingTransition(R.xml.fade_in, R.xml.fade_out);

        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (resultCode == RESULT_OK) {
                if(dream == null)
                    dream = new Dream();

                dream.setAudioPath(AUDIO_FILE_PATH);

                hasAudio = true;

                makePlayDeleteButtons();

                Snackbar.make(sv, R.string.form_dreams_audio_record_success, Snackbar.LENGTH_SHORT).setAction("Action", null).show();

            } else if (resultCode == RESULT_CANCELED) {
                hasAudio = false;

                Snackbar.make(sv, R.string.form_dreams_audio_record_failed, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        }


    }

    public void makePlayDeleteButtons() {
        llButtons = (LinearLayout) findViewById(R.id.form_dreams_buttons);
        llButtons.getLayoutParams().height = 250;
        llButtons.requestLayout();

        btnPlayAudio = (Button) findViewById(R.id.form_dreams_audio_play);
        btnDeleteAudio = (Button) findViewById(R.id.form_dreams_audio_delete);

        btnPlayAudio.setBackgroundResource(R.drawable.buttonwhite);
        btnDeleteAudio.setBackgroundResource(R.drawable.buttonwhite);


        btnPlayAudio.setVisibility(View.VISIBLE);
        btnDeleteAudio.setVisibility(View.VISIBLE);

        btnPlayAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mediaPlayer.isPlaying()) {
                    playAudio(v);
                    btnPlayAudio.setClickable(false);
                }
            }
        });

        btnDeleteAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAudioDelete();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopAudio();
    }

    public void record(View v) {
        if(checkPermission()) {
            if (hasAudio)
                showDialogAudioFileExistent();
            else
                recordAudio();
        }
        else
            requestPermission();
    }

    public void recordAudio() {
        AndroidAudioRecorder.with(this)
                .setFilePath(AUDIO_FILE_PATH)
                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setRequestCode(REQUEST_RECORD_AUDIO)
                .setSource(AudioSource.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_48000)
                .setAutoStart(false)
                .setKeepDisplayOn(true)
                .record();
        overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
    }

    private DatePickerDialog.OnDateSetListener dpickerListener =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    yearX = year;
                    monthX = month + 1;
                    dayX = dayOfMonth;

                    TextView date = (TextView) findViewById(R.id.form_dreams_date);
                    date.setText(String.format("%02d", dayX) + "/" + String.format("%02d", monthX) + "/" + yearX);
                }
            };

    public void showDatePickerDialog() {
        Button btnSetDate = (Button) findViewById(R.id.form_dreams_btnSetDate);
        btnSetDate.setBackgroundResource(R.drawable.buttonblue);

        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE_ID);
            }
        });
    }

    protected Dialog onCreateDialog(int id) {
        if(id == DIALOG_DATE_ID) {
            DatePickerDialog dpd;

            if(dreamDay != null) {
                TextView date = (TextView) findViewById(R.id.form_dreams_date);
                date.setText(String.format("%02d", dreamDay.getDay()) + "/" + String.format("%02d", dreamDay.getMonth()) + "/" + dreamDay.getYear());
                dpd = new DatePickerDialog(this, dpickerListener, dreamDay.getYear(), dreamDay.getMonth() - 1, dreamDay.getDay());
                dreamAux = dreamDay;
                dreamDay = null;
            }
            else if(dream == null && dreamAux != null) {
                dpd = new DatePickerDialog(this, dpickerListener, dreamAux.getYear(), dreamAux.getMonth() - 1, dreamAux.getDay());
                dreamAux = null;
            }
            else if(dream == null)
                dpd = new DatePickerDialog(this, dpickerListener, yearX, monthX, dayX);
            else
                dpd = new DatePickerDialog(this, dpickerListener, dream.getYear(), dream.getMonth() - 1, dream.getDay());

            dpd.getDatePicker().setMaxDate(System.currentTimeMillis());

            return dpd;
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.form_confirm, menu);

        if(dream != null)
            setTextDescription(dream.getDescription());

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Dream dream = helper.getDream();

        if(hasAudio && !lastPathAudio.equals(""))
            dream.setAudioPath(lastPathAudio);
        else if(hasAudio)
            dream.setAudioPath(AUDIO_FILE_PATH);



        Intent intentDream;
        switch (item.getItemId()) {
            case R.id.menu_form_confirm:
                if(emptyDream(2)) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(FormDreamsActivity.this);
                    alert.setMessage(R.string.form_dreams_save).setCancelable(false)
                            .setPositiveButton(R.string.form_dreams_ok, null);
                    alert.show();
                }
                else {
                    DreamDAO dao = new DreamDAO(FormDreamsActivity.this);

                    if(dream.getId() != null)
                        dao.Update(dream);
                    else {
                        dao.Insert(dream);

                        List<Dream> lst = dao.Read();
                        dream = lst.get(lst.size()-1);
                    }

                    dao.close();
                    intentDream = new Intent(FormDreamsActivity.this, DreamsActivity.class);
                    intentDream.putExtra("dream", dream);
                    finish();
                    startActivity(intentDream);
                    overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
                }
                break;
            case android.R.id.home:
                DreamDAO dao = new DreamDAO(this);
                List<Dream> dreams = dao.Read();
                Dream originalDream = new Dream();
                if(this.dream != null) {
                    for(Dream d : dreams) {
                        if(d.getId().equals(this.dream.getId()))
                            originalDream = d;
                    }
                }

                if(emptyDream(1) || ((compareDreams(originalDream, dream)))) {
                    if(dream.getId() == null) {
                        intentDream = new Intent(FormDreamsActivity.this, MainNavDrawerActivity.class);
                    }
                    else {
                        intentDream = new Intent(FormDreamsActivity.this, DreamsActivity.class);
                    }

                    intentDream.putExtra("dream", originalDream);
                    startActivity(intentDream);
                    finish();
                    overridePendingTransition(0, R.xml.fade_out);
                }
                else
                    showDialogExitSave();


                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogExitSave() {
        final Intent intentA = new Intent(FormDreamsActivity.this, MainNavDrawerActivity.class);
        AlertDialog.Builder alert = new AlertDialog.Builder(FormDreamsActivity.this);
        alert.setMessage(R.string.form_dreams_without_save).setCancelable(false)
                .setNegativeButton(R.string.form_dreams_cancel, null)
                .setPositiveButton(R.string.form_dreams_exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(intentA);
                        overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
                    }
                });
        alert.show();
    }

    private void showDialogAudioFileExistent() {
        AlertDialog.Builder alert = new AlertDialog.Builder(FormDreamsActivity.this);
        alert.setMessage(R.string.form_dreams_audio_file_exist).setCancelable(false)
                .setNegativeButton(R.string.form_dreams_no, null)
                .setPositiveButton(R.string.form_dreams_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recordAudio();
                        lastPathAudio = AUDIO_FILE_PATH;
                    }
                });
        alert.show();
    }

    private void showDialogAudioDelete() {
        AlertDialog.Builder alert = new AlertDialog.Builder(FormDreamsActivity.this);
        alert.setMessage(R.string.form_dreams_audio_delete).setCancelable(true)
                .setNegativeButton(R.string.form_dreams_no, null)
                .setPositiveButton(R.string.form_dreams_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dream.setAudioPath("");
                        hasAudio = false;

                        llButtons.getLayoutParams().height = 0;
                        llButtons.requestLayout();

                        btnDeleteAudio.setVisibility(View.INVISIBLE);
                        btnPlayAudio.setVisibility(View.INVISIBLE);
                    }
                });
        alert.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
    }

    public boolean emptyDream(int n) {
        Dream dream = helper.getDream();
        if(dream.getAudioPath() == null)
            dream.setAudioPath("");
        dream.setDescription(getTextDescription());

        switch(n) {
            case 1:
                return (dream.getTitle().trim().length() == 0 && dream.getDescription().trim().length() == 0 && dream.getAudioPath().trim().length() == 0);
            case 2:
                return (dream.getTitle().trim().length() == 0 || dream.getDescription().trim().length() == 0 && dream.getAudioPath().trim().length() == 0);

        }
        return false;
    }

    public boolean compareDreams(Dream dream, Dream dream2) {
        if(dream.getTitle() != null) {

            return dream.getTitle().equals(dream2.getTitle()) &&
                    dream.getDescription().equals(dream2.getDescription()) &&
                    dream.getTags().equals(dream2.getTags());
        }
        return false;
    }

    public String CreateRandomAudioFileName(int size) {
        StringBuilder stringBuilder = new StringBuilder(size);
        int i = 0;

        random = new Random();

        while (i < size) {
            stringBuilder.append(RandomAudioFileName.charAt(random.nextInt(RandomAudioFileName.length())));
            i++;
        }

        return stringBuilder.toString();
    }

    public void getViewFromTextFragment(View view) {
        this.viewTextFragment = view;
    }

    public void setTextDescription(String text) {
        EditText description = (EditText) viewTextFragment.findViewById(R.id.form_dreams_description);
        description.setText(text);
    }

    public String getTextDescription() {
        EditText description = (EditText) viewTextFragment.findViewById(R.id.form_dreams_description);
        return description.getText().toString();
    }

    public boolean checkPermission() {
        int resultStorage = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int resultAudio = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);

        return resultStorage == PackageManager.PERMISSION_GRANTED && resultAudio == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(FormDreamsActivity.this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    public void stopAudio() {

        if(mediaPlayer != null)
            mediaPlayer.stop();
    }

    public void playAudio(View v) {
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(dream.getAudioPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnPlayAudio.setClickable(true);
            }
        });
    }
}