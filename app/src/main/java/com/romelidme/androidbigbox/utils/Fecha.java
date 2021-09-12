package com.romelidme.androidbigbox.utils;



import android.util.Log;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Jovad on 13/04/2018.
 */

public class Fecha {

 static SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy");
    public static String Actual_Formato() {
        final Calendar calendar = Calendar.getInstance();
        String Fecha;
        int año = calendar.get(Calendar.YEAR);
        int Mes = calendar.get(Calendar.MONTH);
        int Dia = calendar.get(Calendar.DAY_OF_MONTH);
        Fecha = String.valueOf(Dia) +"/"+ String.valueOf(Mes + 1)+ "/"+ String.valueOf(año);
        return Fecha;
    }
    public static String diasRegistro(Date fechaInicial, Date fechaFinal){
        long diferencia = fechaFinal.getTime() - fechaInicial.getTime();
        long segsMilli = 1000;
        long minsMilli = segsMilli * 60;
        long horasMilli = minsMilli * 60;
        long diasMilli = horasMilli * 24;

        long diasTranscurridos = diferencia / diasMilli;
        Log.i("infocomprarniubiz","dias: " +diasTranscurridos);
        return String.valueOf(diasTranscurridos);
    }
    public static String Actual_Hora(){
        Calendar calendario = new GregorianCalendar();
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minutos = calendario.get(Calendar.MINUTE);
        int seg = calendario.get(Calendar.SECOND);
        return hora + ":" + minutos+ ":" + seg;
    }
    public static boolean Comparador_FechaValida(int Año, int Mes, int Dia) {
        final Calendar calendar = Calendar.getInstance();
        int AñoAc = calendar.get(Calendar.YEAR);
        int MesAc = calendar.get(Calendar.MONTH);
        int DiaAc = calendar.get(Calendar.DAY_OF_MONTH);
        if (AñoAc > Año) {//año pasado
            return false;
        } else {
            if (AñoAc == Año) {//Mismo Años
                if (MesAc > Mes) {//Mes pasado
                    return false;
                } else {
                    if (MesAc == Mes) {
                        //Mismo Mes
                        if (DiaAc > Dia) {//dia pasado
                            return false;
                        } else {//Mismo dia o Mayor
                            return true;
                        }
                    } else {//Mes Mayor
                        return true;
                    }
                }
            } else {//año mayor
                return true;
            }
        }
    }
    public static String diaSemana () {
        String letraD = "";
        int nD = -1;
        Calendar c = Calendar.getInstance();
        int año = c.get(Calendar.YEAR);
        int Mes = c.get(Calendar.MONTH);
        int Dia = c.get(Calendar.DAY_OF_MONTH);
        c.set(año, Mes, Dia);
        nD = c.get(Calendar.DAY_OF_WEEK);
        switch (nD) {
            case 1:
                letraD = "Domingo";
                break;
            case 2:
                letraD = "Lunes";
                break;
            case 3:
                letraD = "Martes";
                break;
            case 4:
                letraD = "Miercoles";
                break;
            case 5:
                letraD = "Jueves";
                break;
            case 6:
                letraD = "Viernes";
                break;
            case 7:
                letraD = "Sabado";
                break;
        }

        return letraD;
    }

    public static long toTimestamp(String value){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        try {
            Timestamp ts = new Timestamp(((Date)df.parse(value)).getTime());
            return ts.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static Date fromTimestamp(String value) {
        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }
    public static Date toDate(String fecha){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {

            Date date = formatter.parse(fecha);
           return date;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean verificarHoras(String fechaServicio){
        String fechaActual = Actual_Formato() + " "+ wholetec.cliente.bigbox.utils.Fecha.Actual_Hora()+":00";

        Date dateNueva = toDateTotal(fechaActual);
        Date dateServicio = toDateTotal(fechaServicio);
        long minutos = (dateServicio.getTime()-dateNueva.getTime())/(1000*60);
        if(minutos>180)
            return true;
        else
            return false;
    }
    public static Date toDateTotal(String fecha){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        try {
            Date date = formatter.parse(fecha);
            return date;

        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("infoDates","error: " + e.getMessage());
            return null;
        }
    }
}
