/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package carlosgarita.proyecto2;

/**
 *
 * @author cgari
 */
public class CarlosGaritaProyecto2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length>0)
        {
            String nombreArchivo = args[0];
            String nombreNuevoArchivo = nombreArchivo.replace(".java", "_errores.txt");
            
            //realiza una instancia de la clase analisis.
            Procesamiento archivo = new Procesamiento();
            archivo.manejoArchivo(nombreArchivo, nombreNuevoArchivo);    
            
           // System.out.println("Mi archivo:" + nombreArchivo);
        }
        else
        {
            System.out.println("No se indicó nombre de archivo a analizar!");
        }        
    }
    
}
