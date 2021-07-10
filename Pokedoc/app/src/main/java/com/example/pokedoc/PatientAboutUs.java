package com.example.pokedoc;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class PatientAboutUs extends AppCompatActivity
{

    ImageView Linkedin1,linkedin2, linkedin3, linkedin4;
    ImageView Twitter1, Twitter2, Twitter3, Twitter4;
    ImageView Github1,Github2, Github3, Github4;
    ImageView youtube1,youtube2, youtube3, youtube4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_about_us);
        int nightModeFlags = this.getApplicationContext().getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
                // Resources layout =getResources();
                //  layout.(R.drawable.backgrounddark);
                layout.setBackgroundResource(R.drawable.backdark);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                RelativeLayout layoutlight = (RelativeLayout) findViewById(R.id.layout);
                layoutlight.setBackgroundResource(R.drawable.back);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                RelativeLayout layoutd = (RelativeLayout) findViewById(R.id.layout);
                // Resources layout =getResources();
                //  layout.(R.drawable.backgrounddark);
                layoutd.setBackgroundResource(R.drawable.backdark);
                break;
        }
        Linkedin1=findViewById(R.id.linkedinswar);
        linkedin2=findViewById(R.id.linkedinanuj);
        linkedin3=findViewById(R.id.linkedinsay);
        linkedin4=findViewById(R.id.linkedinAni);

        Twitter1=findViewById(R.id.twitterswar);
        Twitter2=findViewById(R.id.twitterAnuj);
        Twitter3=findViewById(R.id.twittersay);
        Twitter4=findViewById(R.id.twitterAni);

        Github1=findViewById(R.id.githubswar);
        Github2=findViewById(R.id.githubAnuj);
        Github3=findViewById(R.id.githubsay);
        Github4=findViewById(R.id.githubAni);

        youtube1=findViewById(R.id.youtubeSwar);
        youtube2=findViewById(R.id.youtubeAnuj);
        youtube3=findViewById(R.id.youtubesay);
        youtube4=findViewById(R.id.youtubeAni);


        Linkedin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.linkedin.com/in/swarangi-patil");
            }
        });

        linkedin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.linkedin.com/in/anuja-jadhav-06111217b/");
            }
        });

        linkedin3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.linkedin.com/in/sayali-desai-0422");
            }
        });

        linkedin4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.linkedin.com/in/sayali-desai-0422");
            }
        });

        Twitter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://twitter.com/SayaliDesai15");
            }
        });

        Twitter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://twitter.com/Anuja2512");
            }
        });

        Twitter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://twitter.com/SayaliDesai15");
            }
        });

        Twitter4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://twitter.com/SayaliDesai15");
            }
        });

        Github1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://github.com/swarangi203");
            }
        });

        Github2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://github.com/Anuja2512");
            }
        });

        Github3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://github.com/SayaliDesai4");
            }
        });

        Github4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://github.com/Aniket2802");
            }
        });

        youtube1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] emails_in_to={"swarangi.patil@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, emails_in_to );
                intent.putExtra(Intent.EXTRA_SUBJECT,"Your Email’s Subject");
                intent.putExtra(Intent.EXTRA_TEXT, "Your Email’s Body");
                intent.putExtra(Intent.EXTRA_CC,"mailcc@gmail.com");
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
            }
        });

        youtube2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] emails_in_to={"anujajadhav2512@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, emails_in_to );
                intent.putExtra(Intent.EXTRA_SUBJECT,"Your Email’s Subject");
                intent.putExtra(Intent.EXTRA_TEXT, "Your Email’s Body");
                intent.putExtra(Intent.EXTRA_CC,"mailcc@gmail.com");
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
            }
        });

        youtube3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] emails_in_to={"dsayali422@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, emails_in_to );
                intent.putExtra(Intent.EXTRA_SUBJECT,"Your Email’s Subject");
                intent.putExtra(Intent.EXTRA_TEXT, "Your Email’s Body");
                intent.putExtra(Intent.EXTRA_CC,"mailcc@gmail.com");
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
            }
        });

        youtube4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] emails_in_to={"aniketsablepatil99k@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, emails_in_to );
                intent.putExtra(Intent.EXTRA_SUBJECT,"Your Email’s subject");
                intent.putExtra(Intent.EXTRA_TEXT, "Your Email’s Body");
                intent.putExtra(Intent.EXTRA_CC,"mailcc@gmail.com");
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(intent);
            }
        });
    }

    private void gotoUrl(String s) {
        Uri uri= Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

}