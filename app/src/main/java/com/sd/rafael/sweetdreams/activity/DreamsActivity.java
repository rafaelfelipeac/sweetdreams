package com.sd.rafael.sweetdreams.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.nfc.Tag;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.cunoraz.tagview.TagView;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.sd.rafael.sweetdreams.DAO.DreamDAO;
import com.sd.rafael.sweetdreams.R;
import com.sd.rafael.sweetdreams.helper.DreamsHelper;
import com.sd.rafael.sweetdreams.models.Dream;

import java.io.IOException;
import java.util.Locale;

public class DreamsActivity extends BaseActivity  {

    private DreamsHelper helper;
    private Dream dream;
    private TagView tagGroup;
    private ScrollView sv;
    private LikeButton likeButton;
    private MediaPlayer mediaPlayer;
    private Button audioPlay;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dreams);

        final DreamDAO dao = new DreamDAO(this);

        helper = new DreamsHelper(this);

        Intent intent = getIntent();
        dream = (Dream) intent.getSerializableExtra("dream");

        sv = (ScrollView) findViewById(R.id.activity_dreams);
        tagGroup = (TagView) findViewById(R.id.tag_group);
        likeButton = (LikeButton) findViewById(R.id.favorite_dreams);
        audioPlay = (Button) findViewById(R.id.form_dreams_audio_play);

        if(dream != null)
            helper.makeDream(dream);

        audioPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dream.getAudioPath() == null || dream.getAudioPath().isEmpty())
                    Snackbar.make(v, "Source file don't find.", Snackbar.LENGTH_SHORT).show();
                else
                    playAudio(v);
            }
        });

        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                dream.setFavorite(true);
                Snackbar.make(findViewById(R.id.activity_dreams), R.string.dreams_favorite, Snackbar.LENGTH_SHORT).show();

                if(dream.getId() == null) {
                    dao.Remove(dream);
                    dao.Insert(dream);
                }
                else
                    dao.Update(dream);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                dream.setFavorite(false);

                if(dream.getId() == null) {
                    dao.Remove(dream);
                    dao.Insert(dream);
                }
                else
                    dao.Update(dream);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dream, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Dream dream = helper.getDream();
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_dream_edit:
                Intent intentForm = new Intent(DreamsActivity.this, FormDreamsActivity.class);
                intentForm.putExtra("dream", dream);
                startActivity(intentForm);
                finish();
                break;
            case R.id.menu_dream_delete:
                AlertDialog.Builder alert = new AlertDialog.Builder(DreamsActivity.this);
                //alert.setMessage(" " + R.string.dreams_delete_message + " '" + dream.getTitle()+"'?").setCancelable(false)
                alert.setMessage(R.string.dreams_delete_message).setCancelable(false)
                    .setNegativeButton(R.string.dreams_cancel , null)
                    .setPositiveButton(R.string.dreams_delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        Dream dream = helper.getDream();
                        DreamDAO dao = new DreamDAO(DreamsActivity.this);
                        dao.Read();
                        dao.Remove(dream);
                        dao.close();
                        Intent intentMain = new Intent(DreamsActivity.this, MainNavDrawerActivity.class);
                        startActivity(intentMain);
                        finish();
                    }
                });

                alert.show();
                break;
        }
        return super.onOptionsItemSelected(item);
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
    }
}
