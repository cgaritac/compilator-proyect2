/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carlosgarita.proyecto2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

/**
 *
 * @author cgari
 */
public class Procesamiento {

//    Declaración de HashSet para almacenar los identificadores
    private static boolean scannerCheck = false;
    private static boolean mainCheck = false;
    private static boolean ifCheck = false;
    private static boolean comandoIfCheck = false;
    private static boolean elseCheck = false;
    private static boolean elseSolitarioCheck = false;
    private static boolean comandoElseCheck = false;
    private static boolean whileCheck = false;
    private static boolean comandoWhileCheck = false;
    private static int lineaIf = 0;
    private static int conteoIf = 0;
    private static int conteoElse = 0;
    private static int lineaElse = 0;
    private static int lineaWhile = 0;
    private static int conteoWhile = 0;
    private static Hashtable<String, String> identificadores = new Hashtable<>();
    private static String[] tiposDatos = {"String", "char", "byte", "short", "int", "long", "float", "double", "boolean"};
    private static String[] palabrasReservadas = {"abstract", "boolean", "break", "byte", "byvalue", "case", "cast",
        "catch", "char", "class", "const", "continue", "default", "do",
        "double", "else", "extends", "final", "finally", "float", "for",
        "future", "generic", "goto", "if", "implements", "import", "inner",
        "instanceof", "int", "interface", "long", "native", "new", "null",
        "operator", "outer", "package", "private", "protected", "public",
        "rest", "return", "String", "short", "static", "super", "switch", "synchronized",
        "this", "throw", "throws", "trasient", "try", "var", "void", "volatile",
        "while"};

    public void manejoArchivo(String nombArch, String nombNuevArch) {

        try {
            BufferedReader reader = new BufferedReader(new FileReader(nombArch));
            BufferedWriter writer = new BufferedWriter(new FileWriter(nombNuevArch));

//            Declaración de variables
            String linea;
            String lineaCortada;
            int lineCount = 0;

//            Recorrido del código
            while ((linea = reader.readLine()) != null) {
                boolean verificador = false;
                lineCount++;
                lineaCortada = linea.trim();

//                Declara un arreglo "palabras" para almacenar todas las palabras de la linea
                String[] palabras = lineaCortada.split("\\s+");

                if (palabras.length > 1) {
//                    Se determina si el identificador termina en ";"
                    if (palabras[1].endsWith(";")) {
//                       Quitar el ";" de la segunda palabra en palabras[1]
                        palabras[1] = palabras[1].substring(0, palabras[1].length() - 1);

//                        Se declara un nuevo arreglo de tamaño 3 y se almacenan los valores de "palabras" y el ";"
                        String[] nuevoArreglo = new String[3];
                        nuevoArreglo[0] = palabras[0];
                        nuevoArreglo[1] = palabras[1];
                        nuevoArreglo[2] = ";";

//                        Se alamacena el nuevo arreglo de tamaño 3 en "palabras"
                        palabras = nuevoArreglo;
                    }
                }

//                Numeración de las líneas
                if (lineCount <= 9) {
                    writer.write(lineCount + "   " + linea + "\n");
                    writer.flush();
                } else {
                    writer.write(lineCount + "  " + linea + "\n");
                    writer.flush();
                }

//                Punto 1: Validación de que no se trata de un comentario
                if (!lineaCortada.startsWith("//")) {

                    if (!palabras[0].isEmpty()) {

//                        Validación de que ya se paso por el main
                        validarMain(lineaCortada);

//                        Punto 1: Validación de definición incorrecta de comentarios
                        if (!"Ok".equals(comentarioValido(palabras[0]))) {
                            writer.write("    Error" + comentarioValido(palabras[0]));
                            writer.flush();
                        } else {
//                            Punto 5: Validación de Scanner
                            if ("Ok".equals(validarScanner(lineaCortada))) {
                                verificador = true;
                            } else if (!"Ok".equals(validarScanner(lineaCortada)) && !"".equals(validarScanner(lineaCortada))) {
                                writer.write("    Error" + validarScanner(lineaCortada));
                                writer.flush();
                                verificador = true;
                            }

//                            Punto 7: Validación de la Salida de Datos
                            if ("Ok".equals(validarSalidaDatos(lineaCortada))) {
                                verificador = true;
                            } else if (!"Ok".equals(validarSalidaDatos(lineaCortada)) && !"".equals(validarSalidaDatos(lineaCortada))) {
                                writer.write("    Error" + validarSalidaDatos(lineaCortada));
                                writer.flush();
                                verificador = true;
                            }

//                            Punto 10: Validación de palabras reservadas
                            if (validarPalabraReservada(palabras[0])) {

//                                Punto 3: Validación del tipo de datos    
                                if ("Ok".equals(validarTipoDatos(palabras[0]))) {
                                    if (palabras.length > 1) {
//                                         Punto 2: Validaciones de definición de identificadores
                                        if (!"".equals(identificadorValido(palabras[1], palabras))) {
                                            writer.write(identificadorValido(palabras[1], palabras));
                                            writer.flush();
                                        }
                                    } else {
                                        writer.write("    Error13: Debe existir al menos una variable declarada para el tipo de dato.\n");
                                        writer.flush();
                                    }
                                }

//                                Punto 8: Validación de "if"
                                if (palabras[0].toLowerCase().contains("if") || lineaCortada.toLowerCase().contains("else") || ifCheck == true || elseCheck == true) {
                                    if (!"Ok".equals(validarIf(palabras, lineaCortada, lineCount)) && !"".equals(validarIf(palabras, lineaCortada, lineCount))) {
                                        writer.write(validarIf(palabras, lineaCortada, lineCount));
                                        writer.flush();
                                    }

//                                    if (!"".equals(validarFormatoIf(nombArch, lineCount))) {
//                                        writer.write(validarFormatoIf(nombArch, lineCount));
//                                        writer.flush();
//                                    }
//                                    ifCheck = false;
//                                    comandoIfCheck = false;
//                                    elseCheck = false;
//                                    comandoElseCheck = false;
                                }

//                                Punto 9: Validación de "while"
                                if (palabras[0].toLowerCase().contains("while")) {
                                    if (!"Ok".equals(validarWhile(palabras, lineaCortada, lineCount)) && !"".equals(validarWhile(palabras, lineaCortada, lineCount))) {
                                        writer.write("    Error" + validarWhile(palabras, lineaCortada, lineCount));
                                        writer.flush();
                                    }

                                }

                                verificador = true;
                            } else {
//                                Punto 3: Validación del tipo de datos    
                                if (!"".equals(validarTipoDatos(palabras[0]))) {
                                    writer.write("    Error" + validarTipoDatos(palabras[0]));
                                    writer.flush();

                                    if (palabras.length > 1) {
//                                        Punto 2: Validaciones de definición de identificadores
                                        if (!"".equals(identificadorValido(palabras[1], palabras))) {
                                            writer.write(identificadorValido(palabras[1], palabras));
                                            writer.flush();
                                            verificador = true;
                                        }
                                    } else {
                                        writer.write("    Error13: Debe existir al menos una variable declarada para el tipo de dato.\n");
                                        writer.flush();
                                        verificador = true;
                                    }
                                }

//                                Punto 4: Validación de la asignación   
                                if ("".equals(validarTipoDatos(palabras[0])) && (valorarArreglo("=", palabras) || valorarArreglo(":", palabras)) && (!valorarArreglo("scanner", palabras) || !valorarArreglo("system", palabras))) {
                                    int contador = 0;
                                    boolean dobleIgual = false;

                                    for (String dato : palabras) {
                                        if ("=".equals(dato)) {
                                            contador++;
                                        }

                                        if ("==".equals(dato)) {
                                            dobleIgual = true;
                                        }
                                    }

                                    if (!"Ok".equals(validarVariable(palabras[0])) && !"".equals(validarVariable(palabras[0]))) {
                                        writer.write("    Error" + validarVariable(palabras[0]));
                                        writer.flush();
                                    }

                                    if ((valorarArreglo(":", palabras) || contador > 1 || dobleIgual == true) && ifCheck != true && whileCheck != true && elseCheck != true) {
                                        writer.write("    Error15: La asignación debe ser realizada utilizando solamente un '='.\n");
                                        writer.flush();
                                    }

                                    if (!"Ok".equals(validarDerechaIgual(palabras))) {
                                        writer.write("    Error" + validarDerechaIgual(palabras));
                                        writer.flush();
                                    }
                                    verificador = true;
                                }

//                                Punto 8: Validación de "if"
                                if (palabras[0].toLowerCase().contains("if") || lineaCortada.toLowerCase().contains("else") || ifCheck == true || elseCheck == true) {
                                    if (!"Ok".equals(validarIf(palabras, lineaCortada, lineCount)) && !"".equals(validarIf(palabras, lineaCortada, lineCount))) {
                                        writer.write(validarIf(palabras, lineaCortada, lineCount));
                                        writer.flush();
                                    }

                                    verificador = true;
                                }

//                                Punto 9: Validación de "while"
                                if (palabras[0].toLowerCase().contains("while") || whileCheck == true) {
                                    if (!"Ok".equals(validarWhile(palabras, lineaCortada, lineCount)) && !"".equals(validarWhile(palabras, lineaCortada, lineCount))) {
                                        writer.write("    Error" + validarWhile(palabras, lineaCortada, lineCount));
                                        writer.flush();
                                    }

                                    verificador = true;
                                }
                            }

                            if (!verificador && whileCheck != true && ifCheck != true && elseCheck != true) {
                                writer.write("    Error35: Comando Java inválido.\n");
                                writer.flush();
                            }
                        }
                    }
                }
            }

        } catch (FileNotFoundException ex) {
            System.out.println("Archivo no encontrado");
        } catch (IOException ex) {
            System.out.println("Archivo no encontrado o no se pudo abrir");
        }
    }

//    Punto 1: Método para validar el comentario cuando encuentra un "/"
    private String comentarioValido(String palabra) {
        String error = "Ok";

        if (palabra.charAt(0) == '/') {
            error = "1: El comentario debe comenzar con '//' en la línea.\n";
            return error;
        } else {
            return error;
        }
    }

//    Valida si ya se pasó por el main
    private void validarMain(String lineaCortada) {
        if (lineaCortada.contains("main")) {
            mainCheck = true;
        }
    }

//    Punto 2: Método para validar el identificador
    private String identificadorValido(String palabra, String[] palabras) {
        String error = "";
        boolean registroError = false;
        boolean charNoValido = false;
        boolean palabraNoValida = false;
        boolean identificadorRepetido = false;

//        Se recorre la palabra buscando caracteres no permitidos
        for (int i = 1; i < palabra.length(); i++) {
            char c = palabra.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '_' && c != '$' && !palabras[palabras.length - 1].endsWith(";")) {
                charNoValido = true;
            }
        }

//        Se recorre el listado de palabras reservadas para determinar si la palabra es una palabra reservada
        for (String palabraReservada : palabrasReservadas) {
            if (palabra.startsWith(palabraReservada)) {
                palabraNoValida = true;
            }
        }

        for (String identificador : identificadores.keySet()) {
            if (palabra.equals(identificador)) {
                identificadorRepetido = true;
                break;
            }
        }

        if (!Character.isLetter(palabra.charAt(0))) {
            error = error + "    Error2: El identificador de la variable debe comenzar con una letra.\n";
            registroError = true;
        }

        if (charNoValido) {
            error = error + "    Error3: El identificador de la variable posee caracteres no validos, solamente se permiten letras, números y los símbolos '_' y '$'.\n";
            registroError = true;
        }

        if (palabraNoValida) {
            error = error + "    Error4: El identificador de la variable no es válido por coincidir con una palabra reservada.\n";
            registroError = true;
        }

        if (palabras.length > 3) {
            error = error + "    Error5: El identificador de la variable no puede poseer espacios ni blancos, solamente se permiten letras, números y los símbolos '_' y '$'.\n";
            registroError = true;
        } else if (palabras.length >= 2 && 3 >= palabras.length && !palabras[palabras.length - 1].endsWith(";")) {
            error = error + "    Error6: El identificador de la variable debe terminar con ';'.\n";
            registroError = true;
        }

        if (identificadorRepetido) {
            error = "    Error7: El identificador de la variable ya existente en la línea.\n";
            registroError = true;
        }

        if (registroError == false) {
            identificadores.put(palabras[1], palabras[0]);
        }
        return error;
    }

//    Punto 3: Método para validar el tipo de datos
    private String validarTipoDatos(String palabra) {
        String error = "";

        for (String tipoDato : tiposDatos) {
            if (palabra.toLowerCase().contains(tipoDato) && !palabra.startsWith(tipoDato) && !palabra.toLowerCase().startsWith("print")) {
                error = "12: El tipo de dato definido es incorrecto.\n";
                break;
            } else if (palabra.startsWith(tipoDato)) {
                error = "Ok";
                break;
            }
        }
        return error;
    }

//    Punto 4: Método para validar si la variable ya existe
    private String validarVariable(String palabra) {
        String error = "";
        boolean variableExiste = false;

//        separa la cadena si hay un igual en ella
        if (palabra.contains("=") && 1 < palabra.length()) {
            String[] palabras = palabra.split("=");

            palabra = palabras[0];
        }

        for (String identificador : identificadores.keySet()) {
            if (palabra.equals(identificador)) {
                variableExiste = true;
                break;
            }
        }

        if (!variableExiste && !palabra.toLowerCase().contains("println") && !palabra.toLowerCase().contains("while") && !palabra.toLowerCase().contains("if") && !palabra.toLowerCase().contains("else") && whileCheck != true && ifCheck != true && elseCheck != true) {
            error = "14: Variable no declarada aún.\n";
        }

        if (variableExiste) {
            error = "Ok";
        }
        return error;
    }

//    Determina si un arreglo posee un String determinado
    private boolean valorarArreglo(String valor, String arreglo[]) {
        boolean resultado = false;

        for (String dato : arreglo) {
            if (dato.toLowerCase().contains(valor)) {
                resultado = true;
            }
        }
        return resultado;
    }

//    Punto 4 y Punto 6: Método para validar si la variable ya existe y la entrada de datos
    private String validarDerechaIgual(String palabras[]) {
        String error = "Ok";
        String tipoVariableIzquierda = "";
        String tipoVariableDerecha = "";
        String entradaDato = "";
        int contadorIguales = 0;
        int contadorAvance = 0;
        int contadorAvance2 = 0;
        boolean puntoYComa = false;
        boolean ifOWhile = false;

//        Cuenta los iguales o los dos puntos
        for (String dato : palabras) {
            if ("=".equals(dato) || "==".equals(dato) || ":".equals(dato) || ":=".equals(dato) || "=:".equals(dato)) {
                contadorIguales++;
            }
        }

        for (String dato : palabras) {
//            Valora si ya se paso por los iguales
            if ("=".equals(dato) || "==".equals(dato) || ":".equals(dato) || ":=".equals(dato) || "=:".equals(dato)) {
                contadorAvance++;
            }

//            Valora si la variable está antes del igual
            if (contadorIguales != contadorAvance) {
//                Valida si la variable existe
                if ("Ok".equals(validarVariable(dato))) {
                    for (String identificador : identificadores.keySet()) {
                        if (dato.equals(identificador)) {
                            tipoVariableIzquierda = identificadores.get(identificador);
                            break;
                        }
                    }
                }
            }

//            Valora si la variable está después del igual
            if (contadorIguales == contadorAvance) {

                for (String dato2 : palabras) {
                    if (dato2.contains(";")) {
                        puntoYComa = true;
                    }
                }

                if (!puntoYComa && !palabras[0].toLowerCase().contains("while") && !palabras[0].toLowerCase().contains("if")) {
                    error = "17: Falta el ';' al final.\n";
                    break;
                }

                if (!"=".equals(dato) && !"==".equals(dato) && !":".equals(dato) && !":=".equals(dato) && !"=:".equals(dato)) {

                    if (dato.endsWith(";")) {
                        dato = dato.substring(0, dato.length() - 1);
                    }

                    if (dato.matches("\\d+")) {
                        tipoVariableDerecha = "int";
                        break;
                    }

//                    Punto 6: Valora los comandos de entrada de datos
                    if (dato.toLowerCase().contains("sc.next")) {

//                        Conforma el String completo de entrada de datos a la derecha del igual, por los espacios
                        for (String dato3 : palabras) {
                            if ("=".equals(dato3) || "==".equals(dato3) || ":".equals(dato3) || ":=".equals(dato3) || "=:".equals(dato3)) {
                                contadorAvance2++;
                            }
                            if (contadorIguales == contadorAvance2 && (!"=".equals(dato3) && !"==".equals(dato3) && !":".equals(dato3) && !":=".equals(dato3) && !"=:".equals(dato3))) {
                                entradaDato = entradaDato + dato3;
                            }
                        }

//                        Elimina el ";" al final del String
                        if (entradaDato.endsWith(";")) {
                            entradaDato = entradaDato.substring(0, entradaDato.length() - 1);
                        }

//                        Valora os diferentes tipos de entrada de datos
                        if (("char".equals(tipoVariableIzquierda) || "String".equals(tipoVariableIzquierda) && !entradaDato.matches("sc\\.nextLine\\s*\\(\\s*\\)"))) {
                            error = "18: La entrada de datos para String o char se encuentra mal indicada.\n";
                            break;
                        }

                        if (("byte".equals(tipoVariableIzquierda) && !entradaDato.matches("sc\\.nextByte\\s*\\(\\s*\\)"))) {
                            error = "19: La entrada de datos para byte se encuentra mal indicada.\n";
                            break;
                        }

                        if (("short".equals(tipoVariableIzquierda) && !entradaDato.matches("sc\\.nextShort\\s*\\(\\s*\\)"))) {
                            error = "20: La entrada de datos para short se encuentra mal indicada.\n";
                            break;
                        }

                        if (("int".equals(tipoVariableIzquierda) && !entradaDato.matches("sc\\.nextInt\\s*\\(\\s*\\)"))) {
                            error = "21: La entrada de datos para int se encuentra mal indicada.\n";
                            break;
                        }

                        if (("long".equals(tipoVariableIzquierda) && !entradaDato.matches("sc\\.nextLong\\s*\\(\\s*\\)"))) {
                            error = "22: La entrada de datos para long se encuentra mal indicada.\n";
                            break;
                        }

                        if (("float".equals(tipoVariableIzquierda) && !entradaDato.matches("sc\\.nextFloat\\s*\\(\\s*\\)"))) {
                            error = "23: La entrada de datos para float se encuentra mal indicada.\n";
                            break;
                        }

                        if (("double".equals(tipoVariableIzquierda) && !entradaDato.matches("sc\\.nextDouble\\s*\\(\\s*\\)"))) {
                            error = "24: La entrada de datos para double se encuentra mal indicada.\n";
                            break;
                        }

                        if (("boolean".equals(tipoVariableIzquierda) && !entradaDato.matches("sc\\.nextBoolean\\s*\\(\\s*\\)"))) {
                            error = "25: La entrada de datos para boolean se encuentra mal indicada.\n";
                            break;
                        }
                    }

                    for (String identificador : identificadores.keySet()) {
//                        Determina el tipo de dato a la derecha del igual
                        if (dato.equals(identificador)) {
                            tipoVariableDerecha = identificadores.get(identificador);

                            for (String dato4 : palabras) {
                                if (dato4.toLowerCase().contains("while") || dato4.toLowerCase().contains("if")) {
                                    ifOWhile = true;
                                    break;
                                }
                            }

                            if (!tipoVariableIzquierda.equals(tipoVariableDerecha) && ifOWhile != true && whileCheck != true && ifCheck != true && elseCheck != true) {
                                error = "16: Hay variables a la derecha del igual cuyo tipo no coincide con la variable a la izquierda del igual.\n";
                                break;
                            }
                        }
                    }
                }
            }
        }
        return error;
    }

    //    Punto 5: Método para validar el comando Scanner
    private String validarScanner(String lineaCortada) {
        String error = "";

        if (!lineaCortada.matches("\\s*static\\s+Scanner\\s+sc\\s*=\\s*new\\s+Scanner\\s*\\(\\s*System\\.in\\s*\\)\\s*;")/*.equals(comandoScanner)*/ && (lineaCortada.toLowerCase().startsWith("st") || lineaCortada.toLowerCase().contains("scanner"))) {
            error = "9: La sintaxis del comando Scanner para entrada de datos no es correcto.\n";
        } else if (lineaCortada.matches("\\s*static\\s+Scanner\\s+sc\\s*=\\s*new\\s+Scanner\\s*\\(\\s*System\\.in\\s*\\)\\s*;")/*.equals(comandoScanner)*/) {

            if (!mainCheck) {
                error = "10: El comando Scanner debe ir después del main.\n";
            } else {
                if (scannerCheck) {
                    error = "11: El comando Scanner ya fue declarado.\n";
                } else {
                    scannerCheck = true;
                    error = "Ok";
                }
            }
        }
        return error;
    }

//    Punto 7: Método para validar la Salida de Datos
    private String validarSalidaDatos(String lineaCortada) {
        String error = "";

        if (lineaCortada.toLowerCase().startsWith("pr") && !lineaCortada.matches("\\s*println\\s*\\(\\s*\".*\"\\s*\\)\\s*;")) {
            error = "8: La sintaxis del comando de la salida de datos no es correcto.\n";
        }

        if (lineaCortada.matches("\\s*println\\s*\\(\\s*\".*\"\\s*\\)\\s*;")) {
            error = "Ok";
        }
        return error;
    }

//    Punto 8: Método para validar el "if"
    private String validarIf(String palabras[], String lineaCortada, int lineCount) {
        String error = "";
        boolean fin = false;

        if (ifCheck == true) {

            if (!"".equals(lineaCortada) && !"}".contains(lineaCortada) && !lineaCortada.toLowerCase().contains("else") && !lineaCortada.toLowerCase().contains("if")) {
                comandoIfCheck = true;
            }

            if (comandoIfCheck == false && lineaCortada.contains("}")) {
                error = error + "    Error31: Faltan los comandos dentro del 'if' de la linea " + lineaIf + ".\n";
            }

            if ((lineaCortada.toLowerCase().contains("if") || lineaCortada.toLowerCase().contains("else")) && !lineaCortada.matches("\\}\\s*else\\s*\\{")) {
                error = error + "    Error32: Falta el cierre del comando 'if' de la linea " + lineaIf + ".\n";
                conteoIf++;
                fin = true;
            }

            if (lineaCortada.toLowerCase().contains("else") && !lineaCortada.matches("\\}\\s*else\\s*\\{")) {
                error = error + "    Error34: El formato del comando 'else'  del comando 'if' de la linea " + lineaIf + " es incorrecto.\n";
            }

            if ("}".equals(lineaCortada)) {
                conteoIf++;
                fin = true;
            }
        }

        if (palabras[0].toLowerCase().contains("if") && !lineaCortada.startsWith("if") && ifCheck == false && !lineaCortada.toLowerCase().contains("else") && elseCheck == false) {
            error = error + "    Error26: La sintaxis del comando 'if' no es correcta.\n";
        }

        if (!lineaCortada.matches("if\\s*\\(\\s*.*?\\s*\\)\\s*\\{") && ifCheck == false && !lineaCortada.toLowerCase().contains("else") && elseCheck == false) {
            error = error + "    Error26: La sintaxis del comando 'if' no es correcta.\n";
        }

        if (lineaCortada.toLowerCase().contains("else") && !lineaCortada.matches("\\}\\s*else\\s*\\{") && ifCheck == false) {
            error = error + "    Error33: El comando 'else' no se puede encontrar sin un comando 'if' asociado.\n";
            elseCheck = true;
            elseSolitarioCheck = true;
        }

        if (ifCheck == true && elseSolitarioCheck == true) {

            if (!"".equals(lineaCortada) && !lineaCortada.contains("}")) {
                comandoIfCheck = true;
            }

            if ("}".equals(lineaCortada)) {
                conteoElse++;
                fin = true;
            }
        }

//        Condicion para limpiar valores del else en solitario cuando llega el "}" y no encuentra errores pero tampoco error es Ok por no ser un "else" 
        if (fin == true && conteoIf > 1 && "".equals(error) && elseSolitarioCheck == true) {
            elseCheck = false;
            elseSolitarioCheck = false;
            conteoElse = 0;
        }

//        Condicion para limpiar valores del "if" cuando llega el "}" y no encuentra errores pero tampoco error es Ok por no ser un "if"   
        if (fin == true && conteoIf > 1 && "".equals(error)) {
            ifCheck = false;
            lineaIf = 0;
            conteoIf = 0;
            comandoIfCheck = false;
        }

//        Condicion para limpiar valores cuando llega el "}" y pero encuentra errores   
        if (fin == true && conteoIf > 2 && error.contains("32") && lineaCortada.toLowerCase().contains("else")) { //Esta condición ocurre cuando se encuentra otro if sin haber cerrado el primero
            ifCheck = false;
            lineaIf = 0;
            conteoIf = 0;
            comandoIfCheck = false;
        } else if (fin == true && conteoIf > 2 && error.contains("32")) { //Esta condición ocurre cuando se encuentra otro if sin haber cerrado el primero
            ifCheck = true;
            lineaIf = lineCount;
            conteoIf = 0;
            comandoIfCheck = false;
        } else if (fin == true && conteoIf > 2) {
            ifCheck = false;
            lineaIf = 0;
            conteoIf = 0;
            comandoIfCheck = false;
        }

        if (lineaCortada.matches("if\\s*\\(\\s*.*?\\s*\\)\\s*\\{") && "".equals(error)) {
            error = "Ok";
            ifCheck = true;
            lineaIf = lineCount;
        }

        if (lineaCortada.matches("\\}\\s*else\\s*\\{") && "".equals(error) && ifCheck == true) {
            error = "Ok";
            elseCheck = true;
            lineaElse = lineCount;
        }
        return error;
    }

//    Punto 9: Método para validar el "if"
    private String validarWhile(String palabras[], String lineaCortada, int lineCount) {
        String error = "";
        boolean fin = false;

        if (whileCheck == true) {

            if (!"".equals(lineaCortada) && !"}".equals(lineaCortada) && !lineaCortada.toLowerCase().contains("while")) {
                comandoWhileCheck = true;
            }

            if (comandoWhileCheck == false && "}".equals(lineaCortada)) {
                error = "27: Faltan los comandos dentro del 'while' de la linea " + lineaWhile + ".\n";
            }

            if (lineaCortada.toLowerCase().contains("while")) {
                error = "28: Falta el cierre del comando 'while' de la linea " + lineaWhile + ".\n";
                conteoWhile++;
                fin = true;
            }

            if ("}".equals(lineaCortada)) {
                conteoWhile++;
                fin = true;
            }
        }

        if (palabras[0].toLowerCase().contains("while") && !lineaCortada.startsWith("while") && whileCheck == false) {
            error = "29: La sintaxis del comando 'while' no es correcta.\n";
        }

        if (!lineaCortada.matches("while\\s*\\(\\s*.*?\\s*\\)\\s*\\{") && whileCheck == false) {
            error = "30: La sintaxis del comando 'while' no es correcta.\n";
        }

//        Condicion para limpiar valores cuando llega el "}" y no encuentra errores pero tampoco error es Ok por no ser un while        
        if (fin == true && conteoWhile > 1 && "".equals(error)) {
            whileCheck = false;
            lineaWhile = 0;
            conteoWhile = 0;
            comandoWhileCheck = false;
        }

//        Condicion para limpiar valores cuando llega el "}" y pero encuentra errores        
        if (fin == true && conteoWhile > 2 && error.contains("28")) { //Esta condición ocurre cuando se encuentra otro while sin haber cerrado el primero
            whileCheck = true;
            lineaWhile = lineCount;
            conteoWhile = 0;
            comandoWhileCheck = false;
        } else if (fin == true && conteoWhile > 2) {
            whileCheck = false;
            lineaWhile = 0;
            conteoWhile = 0;
            comandoWhileCheck = false;
        }

        if (lineaCortada.matches("while\\s*\\(\\s*.*?\\s*\\)\\s*\\{") && "".equals(error)) {
            error = "Ok";
            whileCheck = true;
            lineaWhile = lineCount;
        }

        return error;
    }

//    Punto 10: Método para validar si es una palabra reservada
    private boolean validarPalabraReservada(String palabra) {
        boolean esReservada = false;

        for (String palabrasReservada : palabrasReservadas) {
            if (palabra.startsWith(palabrasReservada)) {
                esReservada = true;
                break;
            }
        }
        return esReservada;
    }
}
