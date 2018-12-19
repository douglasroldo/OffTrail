package br.edu.unoesc.webmob.offtrail.ui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

import br.edu.unoesc.webmob.offtrail.R;

@EActivity(R.layout.activity_preferencias)
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
public class PreferenciasActivity extends AppCompatActivity {

    @ViewById
    TextView txtPreferencias;
    @ViewById
    TextView txtCorFundo;
    @ViewById
    Spinner spnCorFundo;

    @AfterViews
    public void inicializar() {
        List<String> coresList = new ArrayList<String>();
        coresList.add("Amarelo");
        coresList.add("Azul");
        coresList.add("Branco");
        coresList.add("Cinza");
        coresList.add("Preto");
        coresList.add("Verde");
        coresList.add("Vermelho");

        ArrayAdapter<String> cores = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, coresList);
        spnCorFundo.setAdapter(cores);
    }

    public void salvar(View v) {
        if (spnCorFundo.getSelectedItem().toString().equals("Amarelo")) {
            PrincipalActivity.configuracao.edit().cor().put(Color.YELLOW).apply();
        } else if (spnCorFundo.getSelectedItem().toString().equals("Azul")) {
            PrincipalActivity.configuracao.edit().cor().put(Color.BLUE).apply();
        } else if (spnCorFundo.getSelectedItem().toString().equals("Branco")) {
            PrincipalActivity.configuracao.edit().cor().put(Color.WHITE).apply();
        } else if (spnCorFundo.getSelectedItem().toString().equals("Cinza")) {
            PrincipalActivity.configuracao.edit().cor().put(Color.LTGRAY).apply();
        } else if (spnCorFundo.getSelectedItem().toString().equals("Preto")) {
            PrincipalActivity.configuracao.edit().cor().put(Color.BLACK).apply();
        } else if (spnCorFundo.getSelectedItem().toString().equals("Verde")) {
            PrincipalActivity.configuracao.edit().cor().put(Color.GREEN).apply();
        } else if (spnCorFundo.getSelectedItem().toString().equals("Vermelho")) {
            PrincipalActivity.configuracao.edit().cor().put(Color.RED).apply();
        } else {
            PrincipalActivity.configuracao.edit().cor().put(Color.WHITE).apply();
        }
        finish();
    }

    public void cancelar(View v) {
        finish();
    }
}
