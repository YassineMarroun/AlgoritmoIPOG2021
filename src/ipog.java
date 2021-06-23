import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @ (#) ipog.java
 * 
 *   Pruebas de Software
 *   PEC: Implementación y Prueba del Algoritmo IPOG
 *   Curso 2020-2021
 *
 * @author Yassine Marroun Nettah ymarroun1@alumno.uned.es
 * 
 */
public class ipog {

	public ArrayList<int[]> obtener_combinaciones_parametros(int m, int n) throws Exception {

		ArrayList<int[]> combinaciones = new ArrayList<int[]>();

		// Gestión de errores
		if (m <= 0 | n <= 0 | n > m) {
			throw new Exception("m, n > 0 y m >= n");
		}

		int[] combinacion = new int[m];
		Arrays.fill(combinacion, 0, m - n, 0);
		Arrays.fill(combinacion, m - n, m, 1);
		combinaciones.add(combinacion.clone());
		boolean fin = false;

		while (!fin) {
			int g = m - 2;
			boolean encontrado = false;
			while (!encontrado && g >= 0) {
				if (combinacion[g] == 0 && combinacion[g + 1] == 1) {
					encontrado = true;
				} else {
					g = g - 1;
				}
			}
			if (g >= 0) {
				if (combinacion[m - 1] == 1) {
					combinacion[g] = 1;
					combinacion[g + 1] = 0;
				} else {
					int r = Arrays.stream(Arrays.copyOfRange(combinacion, 0, g)).sum();
					combinacion[g] = 1;
					int ultimos_a_1 = n - r - 1;
					for (int i = g + 1; i < m - ultimos_a_1; i++) {
						combinacion[i] = 0;
					}
					if (ultimos_a_1 > 0) {
						for (int i = m - ultimos_a_1; i < m; i++) {
							combinacion[i] = 1;
						}
					}
				}
			}
			if (encontrado) {
				combinaciones.add(combinacion.clone());
			} else {
				fin = true;
			}
		}
		return combinaciones;
	}

	static boolean elementWiseCompare(int[] a, int[] b) {
		for (int i = 0; i < a.length && i < b.length; i++) {
			if (a[i] != b[i]) {
				return false;
			}
		}
		return true;
	}

	public int getIndexOfLargest(int[] array) {
		if (array == null || array.length == 0)
			return -1; // null or empty

		int largest = 0;
		for (int i = 1; i < array.length; i++) {
			if (array[i] > array[largest])
				largest = i;
		}
		return largest; // position of the first largest found
	}

	public ArrayList<int[]> obtener_combinaciones_valores(List<Integer> dimensiones, int[] combinacion_parametros)
			throws Exception {

		ArrayList<int[]> combinaciones = new ArrayList<int[]>();

		// Gesti?n de errores
		if (dimensiones.size() <= 0 || combinacion_parametros.length <= 0
				|| dimensiones.size() != combinacion_parametros.length) {
			throw new Exception(
					"|dimensiones|, |combinacion_parametros| > 0 y |dimensiones| = |combinacion_parametros|");
		}
		for (int i = 0; i < dimensiones.size(); i++) {
			if (dimensiones.get(i) < 2) {
				throw new Exception("el dominio de todos los par?metros debe ser 2 valores como m?nimo");
			}
		}
		int s = Arrays.stream(combinacion_parametros).sum();

		if (s < 1) {
			throw new Exception("la combinaci?n debe tener, al menos, un par?metro activado");
		}

		int n = 0;
		List<Integer> max_combinacion = new ArrayList<Integer>();

		for (int i = 0; i < combinacion_parametros.length; i++) {
			int parametro = combinacion_parametros[i];
			if (parametro == 1) {
				n++;
				max_combinacion.add(dimensiones.get(i) - 1);
			}
		}

		int[] combinacion = new int[n];
		combinaciones.add(combinacion.clone());
		int[] max_combinacion_int = max_combinacion.stream().mapToInt(i -> i).toArray();
		int i = 0;
		while (elementWiseCompare(combinacion, max_combinacion_int) != true) {
			i = n - 1;
			while (combinacion[i] == max_combinacion_int[i]) {
				combinacion[i] = 0;
				i--;
			}
			combinacion[i] = combinacion[i] + 1;
			combinaciones.add(combinacion.clone());
		}

		return combinaciones;
	}

	public int combinaciones_cubiertas(ArrayList<int[]> combinaciones_parametros, ArrayList<ArrayList<int[]>> pi,
			int[] caso) {

		int resultado = 0;
		for (int i = 0; i < combinaciones_parametros.size(); i++) {
			int[] combinacion_parametros = combinaciones_parametros.get(i);
			ArrayList<int[]> combinacion_valores = pi.get(i);
			ArrayList<Integer> seleccion_caso = new ArrayList<Integer>();

			for (int j = 0; j < combinacion_parametros.length; j++) {
				if (combinacion_parametros[j] == 1) {
					seleccion_caso.add(caso[j]);
				}
			}

			if (seleccion_caso.size() > 0) {
				for (int[] valor : combinacion_valores) {
					if (elementWiseCompare(valor, seleccion_caso.stream().mapToInt(s -> s).toArray())) {
						resultado = resultado + 1;
					}
				}
			}
		}
		return resultado;
	}

	public ArrayList<ArrayList<int[]>> eliminar_combinaciones_cubiertas(ArrayList<int[]> combinaciones_parametros,
			ArrayList<ArrayList<int[]>> pi, int[] caso) {

		for (int i = 0; i < combinaciones_parametros.size(); i++) {
			int[] combinacion_parametros = combinaciones_parametros.get(i);
			ArrayList<int[]> combinacion_valores = pi.get(i);
			ArrayList<Integer> seleccion_caso = new ArrayList<Integer>();
			ArrayList<Integer> js_a_borrar = new ArrayList<Integer>();
			// Rellena la lista seleccion de caso
			for (int j = 0; j < combinacion_parametros.length; j++) {
				if (combinacion_parametros[j] == 1) {
					seleccion_caso.add(caso[j]);
				}
			}
			if (seleccion_caso.size() == 0) {
				continue;
			}
			// Rellena la lista js_a_borrar con las combinaciones a borrar
			for (int j = 0; j < combinacion_valores.size(); j++) {
				int[] valor = combinacion_valores.get(j);
				if (elementWiseCompare(valor, seleccion_caso.stream().mapToInt(s -> s).toArray()) == true) {
					js_a_borrar.add(j);
				}

			}
			Collections.sort(js_a_borrar, Collections.reverseOrder());
			// Elimina de pi las combinaciones a borrar
			for (int h : js_a_borrar) {
				pi.get(i).remove(h);
			}
		}
		return (pi);
	}

	public ArrayList<int[]> funcion_ipog(InfoArchivoEntrada info) throws Exception {
		// Gesti?n de errores
		if (info.valorColumnas.size() <= 0) {
			throw new Exception("como m?nimo hace falta un par?metro");
		}
		if (info.t <= 0 || info.t > info.valorColumnas.size()) {
			throw new Exception("t ha de ser mayor que 0 y menor o igual que el n? de par?metros");
		}
		for (int valor : info.valorColumnas) {
			if (valor < 2) {
				throw new Exception("el dominio de todos los par?metros debe ser 2 valores como m?nimo");
			}
		}
		Collections.sort(info.valorColumnas, Collections.reverseOrder());
		int num_parametros = info.valorColumnas.size();
		int[] combinacion_parametros = new int[num_parametros];
		Arrays.fill(combinacion_parametros, 0, info.t, 1);
		Arrays.fill(combinacion_parametros, info.t, num_parametros, 0);
		ArrayList<int[]> juego_pruebas = this.obtener_combinaciones_valores(info.valorColumnas, combinacion_parametros);

		if (num_parametros > info.t) {
			for (int i = info.t; i < num_parametros; i++) {

				ArrayList<int[]> combinaciones_parametros = this.obtener_combinaciones_parametros(i + 1, info.t);

				// Filtramos las combinaciones que en la posicion i tienen un uno
				ArrayList<int[]> solo_combinaciones_con_i = new ArrayList<int[]>();
				for (int[] comb : combinaciones_parametros) {
					if (comb[i] == 1) {
						solo_combinaciones_con_i.add(comb.clone());
					}
				}
				ArrayList<ArrayList<int[]>> pi = new ArrayList<ArrayList<int[]>>();

				for (int[] c_parametros_con_i : solo_combinaciones_con_i) {
					pi.add(this.obtener_combinaciones_valores(info.valorColumnas.subList(0, i + 1),
							c_parametros_con_i));
				}

				// extensi?n horizontal del juego de pruebas para a?adir el par?metro i
				for (int j = 0; j < juego_pruebas.size(); j++) {
					int[] caso = juego_pruebas.get(j);

					int[] combinaciones_cubiertas_en_pi = new int[info.valorColumnas.get(i)];

					for (int valor = 0; valor <= (info.valorColumnas.get(i) - 1); valor++) {
						List<Integer> caso_array = Arrays.stream(caso).boxed().collect(Collectors.toList());
						caso_array.add(valor);
						int[] caso_array_extended = new int[caso_array.size()];
						for (int ind = 0; ind < caso_array.size(); ind++)
							caso_array_extended[ind] = caso_array.get(ind);

						combinaciones_cubiertas_en_pi[valor] = this.combinaciones_cubiertas(solo_combinaciones_con_i,
								pi, caso_array_extended);
					}
					// Obtener la posicion del maximo
					int maximo = getIndexOfLargest(combinaciones_cubiertas_en_pi);

					List<Integer> caso_aux = Arrays.stream(caso).boxed().collect(Collectors.toList());
					caso_aux.add(maximo);
					juego_pruebas.set(j, caso_aux.stream().mapToInt(x -> x).toArray());
					pi = eliminar_combinaciones_cubiertas(solo_combinaciones_con_i, pi,
							caso_aux.stream().mapToInt(x -> x).toArray());
				}

				// Extension vertical
				for (int j = 0; j < pi.size(); j++) {
					int[] combinacion_parametros_vertical = solo_combinaciones_con_i.get(j);
					ArrayList<int[]> combinacion_valores = pi.get(j);
					for (int[] valor : combinacion_valores) {
						boolean encontrado = false;
						int k = 0;
						while (!encontrado && k < juego_pruebas.size()) {
							int[] caso_vertical = juego_pruebas.get(k);
							ArrayList<Integer> seleccion_caso = new ArrayList<Integer>();

							for (int p = 0; p < combinacion_parametros_vertical.length; p++) {
								if (combinacion_parametros_vertical[p] == 1) {
									seleccion_caso.add(caso_vertical[p]);
								}
							}
							// (seleccion_caso == Inf)
							if (elementWiseCompare(seleccion_caso.stream().mapToInt(y -> y).toArray(), valor) == true) {
								encontrado = true;
							} else {
								k++;
							}
						}

						if (encontrado) {
							int m = 0;
							for (int l = 0; l < combinacion_parametros_vertical.length; l++) {
								if (combinacion_parametros_vertical[l] == 1) {
									juego_pruebas.get(k)[l] = valor[m];
									m++;
								}
							}
						} else {
							ArrayList<Integer> nuevo_caso = new ArrayList<Integer>();
							for (int z = 0; z <= i; z++) {
								nuevo_caso.add(Integer.MAX_VALUE);
							}
							int m = 0;

							for (int l = 0; l < combinacion_parametros_vertical.length; l++) {
								if (combinacion_parametros_vertical[l] == 1) {
									nuevo_caso.set(l, valor[m]);
									m++;
								}
							}

							juego_pruebas.add(nuevo_caso.stream().mapToInt(y -> y).toArray());
						}
					}
				}
			}
		}
		return juego_pruebas;
	}
}
