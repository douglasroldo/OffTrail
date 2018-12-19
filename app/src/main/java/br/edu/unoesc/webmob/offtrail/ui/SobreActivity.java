package br.edu.unoesc.webmob.offtrail.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import br.edu.unoesc.webmob.offtrail.R;

@EActivity(R.layout.activity_sobre)
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
public class SobreActivity extends AppCompatActivity {

    @ViewById
    ImageView imvLogo;
    @ViewById
    TextView txtAppName;
    @ViewById
    TextView txtDescApp;
    @ViewById
    TextView txtCopyApp;
    @ViewById
    TextView txtDisciplinaApp;
    @ViewById
    TextView txtPosApp;

    @AfterViews
    public void inicializar() {

    }

    public void voltar(View v) {
        finish();
    }
}
