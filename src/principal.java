import java.util.ArrayList;

/**
 * 
 * @ (#) principal.java
 * 
 *   Pruebas de Software
 *   PEC: Implementación y Prueba del Algoritmo IPOG
 *   Curso 2020-2021
 *
 * @author Yassine Marroun Nettah
 * ymarroun1@alumno.uned.es
 * 
 */
public class principal {

	public static void main(String[] args) throws Exception {

		LecturaEscrituraArchivos lea = new LecturaEscrituraArchivos();
		InfoArchivoEntrada info = lea.leer_datos_entrada("ejemplo.csv");
		InfoArchivoEntrada info_clon = lea.leer_datos_entrada("ejemplo.csv");

		ipog i = new ipog();
		ArrayList<int[]> resultado = i.funcion_ipog(info);

		lea.escribir_juego_pruebas_ACTS(info_clon, resultado);
	}
}
