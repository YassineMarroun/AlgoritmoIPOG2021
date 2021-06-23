import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 
 * @ (#) LecturaEscrituraArchivos.java
 * 
 *   Pruebas de Software
 *   PEC: Implementación y Prueba del Algoritmo IPOG
 *   Curso 2020-2021
 *
 * @author Yassine Marroun Nettah
 * ymarroun1@alumno.uned.es
 * 
 */
public class LecturaEscrituraArchivos {

	public InfoArchivoEntrada leer_datos_entrada(String fichero_entrada) throws IOException {

		InfoArchivoEntrada datos_entrada = new InfoArchivoEntrada();
		BufferedReader reader;

		reader = new BufferedReader(new FileReader(fichero_entrada));
		String linea1 = reader.readLine();
		datos_entrada.nombreColumnas = Arrays.asList(linea1.split(","));
		datos_entrada.nombreColumnas = datos_entrada.nombreColumnas.subList(0, datos_entrada.nombreColumnas.size() - 1);
		String linea2 = reader.readLine();
		datos_entrada.valorColumnas = Arrays.stream(linea2.split(",")).map(String::trim).map(Integer::parseInt)
				.collect(Collectors.toList());
		datos_entrada.t = datos_entrada.valorColumnas.get(datos_entrada.valorColumnas.size() - 1);
		datos_entrada.valorColumnas = datos_entrada.valorColumnas.subList(0, datos_entrada.valorColumnas.size() - 1);
		reader.close();

		return datos_entrada;
	}

	public void escribir_juego_pruebas_ACTS(InfoArchivoEntrada info, ArrayList<int[]> resultado) throws IOException {

		File file = new File("juego_pruebas1.txt");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);

		bw.write("[Parameter]\n");

		TreeMap<Integer, ArrayList<String>> parametros_ordenados = new TreeMap<Integer, ArrayList<String>>(
				Comparator.reverseOrder());
		ArrayList<String> nombresOrdenados = new ArrayList<String>();
		for (int i = 0; i < info.valorColumnas.size(); i++) {

			int valor = info.valorColumnas.get(i);
			String nombre = info.nombreColumnas.get(i);

			if (parametros_ordenados.containsKey(valor)) {
				parametros_ordenados.get(valor).add(nombre);
			} else {
				ArrayList<String> listaNombres = new ArrayList<String>();
				listaNombres.add(nombre);
				parametros_ordenados.put(valor, listaNombres);
			}
		}
		for (Entry<Integer, ArrayList<String>> m : parametros_ordenados.entrySet()) {

			ArrayList<String> listaOrdenada = m.getValue();
			Collections.sort(listaOrdenada);
			for (String nombreOrdenado : listaOrdenada) {

				nombresOrdenados.add(nombreOrdenado);
				List<Integer> rango = IntStream.range(0, m.getKey()).boxed().collect(Collectors.toList());
				bw.write(nombreOrdenado + " (int) : " + rango.toString().replace("[", "").replace("]", ""));
				bw.write("\n");
			}
		}

		bw.write("\n[Test Set]\n");

		bw.write(nombresOrdenados.toString().replace("[", "").replace("]", "") + "\n");
		for (int i = 0; i < resultado.size(); i++) {
			String linea = "";
			for (int j = 0; j < resultado.get(i).length; j++) {
				int[] numeros = resultado.get(i);
				if (numeros[j] == Integer.MAX_VALUE) {
					linea = linea + "*, ";
				} else {
					linea = linea + numeros[j] + ", ";
				}
			}
			bw.write(linea.substring(0, linea.length() - 2));
			bw.write("\n");
		}
		bw.flush();
		bw.close();
	}
}

class InfoArchivoEntrada {

	public List<String> nombreColumnas = new ArrayList<String>();
	public List<Integer> valorColumnas = new ArrayList<Integer>();
	public int t;

	public InfoArchivoEntrada() {
	}

	public List<String> getNombreColumnas() {
		return nombreColumnas;
	}

	public void setNombreColumnas(List<String> nombreColumnas) {
		this.nombreColumnas = nombreColumnas;
	}

	public List<Integer> getValorColumnas() {
		return valorColumnas;
	}

	public void setValorColumnas(List<Integer> valorColumnas) {
		this.valorColumnas = valorColumnas;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}
}