package com.example.ivan.archivos;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 11/4/2015.
 */
public class ClaseAdaptador extends ArrayAdapter<String> {


        private Context ctx;
        private int res;
        private LayoutInflater lInflator;
        private ArrayList<String> valores;
        private List<String> Tlf;
        private DownloadManager contentResolver;
        private List<String> item = new ArrayList<String>();
        private String wiam;

        public DownloadManager getContentResolver() {
            return contentResolver;
        }

        static class ViewHolder {
            public TextView tv1, tv2;
            public ImageView iv;
        }

        public ClaseAdaptador(Context context, int resource, List<String> lista, String wiam) {
            super(context, resource, lista);
            this.ctx = context;
            this.res = resource;
            this.valores = (ArrayList<String>) lista;
            this.lInflator = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            this.wiam = wiam;
        }

        public static void borrar(String wiam) {

            File f = new File(wiam);
            f.delete();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //1
            ViewHolder gv = new ViewHolder();
            List<String> tipo=new ArrayList<>();
            tipo=tipoArchivos();


            if(convertView==null){
                convertView = lInflator.inflate(res, null);
                ImageView iv = (ImageView) convertView.findViewById(R.id.imgS);
                TextView tv = (TextView) convertView.findViewById(R.id.tvNom);
                gv.tv1 = tv;
                gv.iv = iv;
                convertView.setTag(gv);
            } else {
                gv = (ViewHolder) convertView.getTag();
            }
            if(tipo.get(position).compareToIgnoreCase("Carpeta")==0){
                gv.iv.setImageResource(R.drawable.carpeta);
            }else{
                gv.iv.setImageResource(R.drawable.archivo);
            }
            gv.tv1.setText(valores.get(position).toString());
            return convertView;
        }
        //Método que devolverá una lista con los archivos y carpetas dentro de la ruta en que estamos
        public static List<String> leerArchivos(String wiam) {

            List<String> item=new ArrayList<>();
            File f = new File(wiam);

            //Creamos un array de Files(Archivos) y almacenamos los archivos que contiene nuestra ruta.
            File[] files = f.listFiles();

            //Recorremos un el array creado anteriormente y almacenamos el nombre de cada uno de sus
            // archivos en un Arraylist<String>
            for (int i = 0; i < files.length; i++)
            {
                item.add(files[i].getName());
            }


            //Devolvemos el ArrayList<String> que contiene los nombres de los archivos.
            return item;

        }
//
    public List<String> tipoArchivos() {
        List<String> item=new ArrayList<>();
        File f = new File(wiam);
        File[] files = f.listFiles();

        for (int i = 0; i < files.length; i++)

        {
            File file = files[i];
            if (file.isDirectory())

                item.add("Carpeta");

            else

                item.add("Fichero");
        }
        return item;
    }


    public static void renameArch(String wiam,String name,String nameto){

        File f = new File(wiam+"/"+name);
        File x= new File(wiam+"/"+nameto);
        f.renameTo(x);
    }

//    Con este método creamos una nueva carpeta
    public static void newFile(String wiam, String name){

        File f = new File(wiam+"/"+name);
        f.mkdir();
    }


    public static int comprobar(String wiam, String name){

        File f = new File(wiam+"/"+name);
        if (f.isDirectory()){
            return 1;
        }else{
            return 2;
        }
    }


}
