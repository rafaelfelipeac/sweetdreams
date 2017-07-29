package com.rafaelfelipeac.sweetdreams.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.rafaelfelipeac.sweetdreams.DAO.DreamDAO;
import com.rafaelfelipeac.sweetdreams.R;
import com.rafaelfelipeac.sweetdreams.helper.DreamsHelper;
import com.rafaelfelipeac.sweetdreams.models.Dream;
import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DreamsActivity extends BaseActivity  {

    private DreamsHelper helper;
    private Dream dream;
    private MediaPlayer mediaPlayer;

    @BindView(R.id.favorite_dreams)
    LikeButton likeButton;
    @BindView(R.id.form_dreams_audio_play)
    Button audioPlay;
    @BindView(R.id.form_dreams_play_button)
    LinearLayout playButton;
    @BindView(R.id.form_dreams_ll_play_audio)
    LinearLayout llplayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dreams);

        ButterKnife.bind(this);

        getSupportActionBar().setElevation(0);

        final DreamDAO dao = new DreamDAO(this);

        helper = new DreamsHelper(this);

        Intent intent = getIntent();
        dream = (Dream) intent.getSerializableExtra("dream");

        audioPlay.setBackgroundResource(R.drawable.buttonwhite);

        mediaPlayer = new MediaPlayer();

        if(dream != null)
            helper.makeDream(dream);

        if(!dream.getAudioPath().isEmpty()) {
            playButton.setVisibility(View.VISIBLE);
            llplayButton.getLayoutParams().height = 100;
        }

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

    @OnClick(R.id.form_dreams_audio_play)
    public void audioPlayClick() {
        if(!mediaPlayer.isPlaying()) {
            playAudio();
            audioPlay.setClickable(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopAudio();
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
        overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Dream dream = helper.getDream();
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
                return true;
            case R.id.menu_dream_edit:
                Intent intentForm = new Intent(DreamsActivity.this, FormDreamsActivity.class);
                intentForm.putExtra("dream", dream);
                startActivity(intentForm);
                finish();
                overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
                break;
            case R.id.menu_dream_delete:
                AlertDialog.Builder alert = new AlertDialog.Builder(DreamsActivity.this);
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
                        overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
                    }
                });

                alert.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void stopAudio() {
        if(mediaPlayer != null)
            mediaPlayer.stop();
    }

    public void playAudio() {
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
                audioPlay.setClickable(true);
            }
        });

    }
}
