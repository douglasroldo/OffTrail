package br.edu.unoesc.webmob.offtrail.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import br.edu.unoesc.webmob.offtrail.R;

@EActivity(R.layout.activity_cadastro_trilheiros)
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
public class CadastroTrilheirosActivity extends AppCompatActivity {

    @ViewById
    EditText edtNomeTrilheiro;
    @ViewById
    EditText edtIdadeTrilheiro;
    @ViewById
    EditText edtMotoTrilheiro;


    public void cadastrar(View v) {

    }

    public void cancelar(View v) {

    }
}
