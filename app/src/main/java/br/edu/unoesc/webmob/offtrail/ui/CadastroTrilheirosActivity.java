package br.edu.unoesc.webmob.offtrail.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import br.edu.unoesc.webmob.offtrail.R;
import br.edu.unoesc.webmob.offtrail.helper.DatabaseHelper;
import br.edu.unoesc.webmob.offtrail.model.Grupo;
import br.edu.unoesc.webmob.offtrail.model.GrupoTrilheiro;
import br.edu.unoesc.webmob.offtrail.model.Moto;
import br.edu.unoesc.webmob.offtrail.model.Trilheiro;

@EActivity(R.layout.activity_cadastro_trilheiros)
@Fullscreen
@WindowFeature(Window.FEATURE_NO_TITLE)
public class CadastroTrilheirosActivity extends AppCompatActivity {

    @ViewById
    ImageView imvFoto;
    @ViewById
    EditText edtNomeTrilheiro;
    @ViewById
    EditText edtIdadeTrilheiro;
    @ViewById
    Spinner spnMotos;
    @ViewById
    Spinner spnGrupos;
    @Bean
    DatabaseHelper dh;

    Trilheiro trilheiro;
    GrupoTrilheiro grupoTrilheiro;

    @AfterViews
    public void inicializar() {
        try {
            // cria o adapter
            ArrayAdapter<Moto> motos = new ArrayAdapter<Moto>(this, android.R.layout.simple_spinner_item, dh.getMotoDao().queryForAll());
            ArrayAdapter<Grupo> grupos = new ArrayAdapter<Grupo>(this, android.R.layout.simple_spinner_item, dh.getGrupoDao().queryForAll());

            // vincula o adapter ao spinner
            spnMotos.setAdapter(motos);
            spnGrupos.setAdapter(grupos);

            // se houver um parâmetro trilheiro vindo na intent, então popula os campos para edição do cadastro
            trilheiro = (Trilheiro) getIntent().getSerializableExtra("trilheiro");
            if (trilheiro != null) {
                edtNomeTrilheiro.setText(trilheiro.getNome());
                edtIdadeTrilheiro.setText(trilheiro.getIdade().toString());
                imvFoto.setImageBitmap(BitmapFactory.decodeByteArray(trilheiro.getFoto(), 0, trilheiro.getFoto().length));
                GrupoTrilheiro gt = dh.selectGrupoTrilheiroByTrilheiro(trilheiro);
                grupoTrilheiro = gt;
                spnMotos.setSelection(motos.getPosition(trilheiro.getMoto()));
                spnGrupos.setSelection(grupos.getPosition(grupoTrilheiro.getGrupo()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cadastrar(View v) {
        try {
            if (trilheiro == null) {
                trilheiro = new Trilheiro();
            }
            trilheiro.setNome(edtNomeTrilheiro.getText().toString());
            trilheiro.setIdade(Integer.parseInt(edtIdadeTrilheiro.getText().toString()));
            trilheiro.setMoto((Moto) spnMotos.getSelectedItem());
            Bitmap bitmap = ((BitmapDrawable) imvFoto.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            trilheiro.setFoto(baos.toByteArray());
            dh.getTrilheiroDao().createOrUpdate(trilheiro);

            if (grupoTrilheiro == null) {
                grupoTrilheiro = new GrupoTrilheiro();
            }
            grupoTrilheiro.setTrilheiro(trilheiro);
            grupoTrilheiro.setGrupo((Grupo) spnGrupos.getSelectedItem());
            grupoTrilheiro.setDataCadastro(new Date());
            dh.getGrupoTrilheiroDao().createOrUpdate(grupoTrilheiro);

            finish();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelar(View v) {
        finish();
    }

    @LongClick(R.id.imvFoto)
    public void capturarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 100);
        }
    }

    @OnActivityResult(100)
    void onResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imvFoto.setImageBitmap(imageBitmap);
        }
    }
}
