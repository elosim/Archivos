package com.example.ivan.archivos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.util.List;

public class Main extends AppCompatActivity {
    private ClaseAdaptador cl;
    private ListView lv;
    private List<String> lista;
    private String wiam= Environment.getExternalStorageDirectory().toString();
    private EditText etRenom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
    public boolean onContextItemSelected(MenuItem item) {
        long id = item.getItemId();
        AdapterView.AdapterContextMenuInfo vistaInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int posicion = vistaInfo.position;

        if (id == R.id.menu_renombrar) {
            renameDialog(posicion);
            return true;
        }
        if (id == R.id.menu_nueva){
            createDialog();
            return true;
        }
        if (id == R.id.menu_compartir) {
            compartir(posicion);
        }
        if (id == R.id.menu_borrar){
            borrar(posicion);
        }
        return super.onContextItemSelected(item);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contextual, menu);

    }

//    En el init creamos el listview con el adaptador
    public void init(){
        etRenom=(EditText)findViewById(R.id.etRenomb);
        lista=ClaseAdaptador.leerArchivos(wiam);
        lv =(ListView)findViewById(R.id.lvP);
        cl = new ClaseAdaptador(this, R.layout.item,lista,wiam);
        lv.setAdapter(cl);
        lv.setTag(lista);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                cambiarCl(position);
            }
        });
        registerForContextMenu(lv);

    }
    public void borrar(int posicion){
        ClaseAdaptador.borrar(wiam+"/"+lista.get(posicion).toString());
        change(wiam);
    }



//    Método para compartir un archivo seleccionado en el ListViewm, recibe la posición de este

    public void compartir(int posicion){

//        Creamos un Intent con Action Send, que nos ayudará a enviar el archivo por las redes sociales o nubes
//        que utilicemos en el movil.
//        Creamos un Archivo "f" con su path

        Intent i = new Intent(Intent.ACTION_SEND);
        File f = new File(wiam+"/"+lista.get(posicion).toString());

//      Añadimos un Extra, donde pasamos el archivo a enviar.
//      Despues definimos el tipo, que será de imagen.

        i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
        i.setType("image/jpeg");

//      Y finalmente iniciamos la actividad.

        startActivity(Intent.createChooser(i, "Compartir"));

    }

//    Este método recibe la posicion de un elemento del listview y si pulsa una carpeta la abrirá y si pulsa un archivo
//    nos preguntará con que programa queremos abrirlo.
    public void cambiarCl(int posicion){

        if(ClaseAdaptador.comprobar(wiam,lista.get(posicion).toString())==1){

            wiam+="/"+lista.get(posicion).toString();
            change(wiam);

        }else{

            File f = new File(wiam+"/"+lista.get(posicion).toString());
            Intent i = new Intent(Intent.ACTION_VIEW);

            i.addCategory(Intent.CATEGORY_DEFAULT);
            i.setDataAndType(Uri.fromFile(f),"application/*");
            startActivity(i);
        }

    }

//    Este método nos lleva al directorio padre, en pocas palabras, atras.
    public void irAtras(View v){

        File f = new File(wiam);
        wiam=f.getParent();
        change(wiam);
    }

//    Este método nos cambia el Adaptador para refrescar la lista de contactos
    private void change(String wiam){
        lista=ClaseAdaptador.leerArchivos(wiam);
        lv =(ListView)findViewById(R.id.lvP);
        cl = new ClaseAdaptador(this, R.layout.item,lista,wiam);
        lv.setAdapter(cl);
        lv.setTag(lista);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cambiarCl(position);
            }
        });
        cl.notifyDataSetChanged();
    }

//     Este método nos lleva al Path por defecto de la aplicación
    public void goHome(View view){
        wiam = Environment.getExternalStorageDirectory().toString();
        change(wiam);
    }


//    Este método crea el dialogo donde podemos escribir el nombre al que queremos renombrar una carpeta, pasada
//    al seleccionarlo en el listview
    public void renameDialog(final int posicion){

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.nomtitulo);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.renamedialog, null);
        etRenom = (EditText)vista.findViewById(R.id.etRenomb);

        alert.setView(vista);
        alert.setPositiveButton(R.string.aceptar,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClaseAdaptador.renameArch(wiam, lista.get(posicion).toString(), etRenom.getText().toString());
                        change(wiam);
                    }
                });

        alert.setNegativeButton(R.string.atras, null);
        alert.show();
    }

//    Este método nos crea un dialogo para crear una nueva carpeta
    public void createDialog(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.createtitulo);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.renamedialog, null);
        etRenom = (EditText)vista.findViewById(R.id.etRenomb);

        alert.setView(vista);
        alert.setPositiveButton(R.string.aceptar,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClaseAdaptador.newFile(wiam, etRenom.getText().toString());
                        change(wiam);
                    }
                });

        alert.setNegativeButton(R.string.atras, null);
        alert.show();
    }

    public void createDialogV(View view){
        createDialog();
    }
}
