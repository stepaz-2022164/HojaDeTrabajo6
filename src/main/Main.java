package main;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final String ARCHIVO_INVENTARIO = "inventario.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Tienda Online - Gestión Inventario");
        System.out.println("\nSeleccione la implementación de MAP a utilizar:");
        System.out.println("  1. HashMap");
        System.out.println("  2. TreeMap");
        System.out.println("  3. LinkedHashMap");
        System.out.print("Opción: ");

        int tipoMap = leerEntero(scanner);

        MapFactory<String, String>  inventarioFactory = new MapFactory<>();
        MapFactory<String, Integer> coleccionFactory  = new MapFactory<>();

        Map<String, String>  inventario       = inventarioFactory.getMap(tipoMap);
        Map<String, Integer> coleccionUsuario = coleccionFactory.getMap(tipoMap);

        InventarioLoader.cargarDesdeArchivo(ARCHIVO_INVENTARIO, inventario);

        if (inventario.isEmpty()) {
            System.out.println("No se pudo cargar el inventario. Verifique el archivo \"" + ARCHIVO_INVENTARIO + "\".");
            scanner.close();
            return;
        }

        boolean salir = false;
        while (!salir) {
            System.out.println("MENÚ");
            System.out.println("1. Agregar producto a colección");
            System.out.println("2. Mostrar categoría de producto");
            System.out.println("3. Mostrar colección");
            System.out.println("4. Colección ordenada");
            System.out.println("5. Mostrar inventario completo");
            System.out.println("6. Inventario ordenado");
            System.out.println("7. Comparar tiempos (profiler)");
            System.out.println("8. Salir");
            System.out.print("Elija una opción: ");

            int opcion = leerEntero(scanner);

            switch (opcion) {

                case 1:
                    System.out.print("Ingrese la categoría del producto a agregar: ");
                    String categoriaIngresada = scanner.nextLine().trim();

                    List<String> productosDeCategoria =
                            InventarioService.obtenerProductosPorCategoria(inventario, categoriaIngresada);

                    if (productosDeCategoria.isEmpty()) {
                        System.out.println("Error: la categoría \"" + categoriaIngresada + "\" no existe en el inventario.");
                        break;
                    }

                    System.out.println("Productos disponibles en \"" + categoriaIngresada + "\":");
                    for (int i = 0; i < productosDeCategoria.size(); i++) {
                        System.out.println("  " + (i + 1) + ". " + productosDeCategoria.get(i));
                    }

                    System.out.print("Seleccione el número del producto: ");
                    int numProducto = leerEntero(scanner);

                    if (numProducto < 1 || numProducto > productosDeCategoria.size()) {
                        System.out.println("Número fuera de rango.");
                        break;
                    }

                    String productoElegido = productosDeCategoria.get(numProducto - 1);
                    InventarioService.agregarProducto(inventario, coleccionUsuario, productoElegido);
                    break;

                case 2:
                    System.out.print("Ingrese el nombre del producto: ");
                    String prodCategoria = scanner.nextLine().trim();
                    InventarioService.mostrarCategoria(inventario, prodCategoria);
                    break;

                case 3:
                    InventarioService.mostrarColeccion(inventario, coleccionUsuario);
                    break;

                case 4:
                    InventarioService.mostrarColeccionOrdenada(inventario, coleccionUsuario);
                    break;

                case 5:
                    long inicio = System.nanoTime();
                    InventarioService.mostrarInventario(inventario);
                    long fin = System.nanoTime();
                    System.out.println("\n[Profiler] Tiempo de ejecución op.5: " + (fin - inicio) + " ns");
                    break;

                case 6:
                    InventarioService.mostrarInventarioOrdenado(inventario);
                    break;

                case 7:
                    compararImplementaciones(inventario);
                    break;

                case 8:
                    salir = true;
                    System.out.println("¡Hasta luego!");
                    break;

                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
        }

        scanner.close();
    }

    private static void compararImplementaciones(Map<String, String> inventarioOriginal) {
        final int REPETICIONES = 5;

        System.out.println("PROFILER - Comparación de implementaciones ");
        System.out.println("Se ejecuta mostrarInventario() " + REPETICIONES + " veces por implementación.\n");

        String[] nombres = {"HashMap", "TreeMap", "LinkedHashMap"};
        long[] promedios = new long[3];

        for (int tipo = 1; tipo <= 3; tipo++) {
            MapFactory<String, String> factory = new MapFactory<>();
            Map<String, String> mapa = factory.getMap(tipo);
            mapa.putAll(inventarioOriginal);

            long totalNs = 0;
            for (int r = 0; r < REPETICIONES; r++) {
                java.io.PrintStream originalOut = System.out;
                System.setOut(new java.io.PrintStream(java.io.OutputStream.nullOutputStream()));

                long t1 = System.nanoTime();
                InventarioService.mostrarInventario(mapa);
                long t2 = System.nanoTime();

                System.setOut(originalOut);
                totalNs += (t2 - t1);
            }

            long promedio = totalNs / REPETICIONES;
            promedios[tipo - 1] = promedio;
            System.out.printf("%-15s → promedio: %,10d ns  (~%.3f ms)%n",
                    nombres[tipo - 1], promedio, promedio / 1_000_000.0);
        }

        int indiceMasRapido = 0;
        for (int i = 1; i < promedios.length; i++) {
            if (promedios[i] < promedios[indiceMasRapido]) indiceMasRapido = i;
        }
        System.out.println("\n Implementación más rápida: " + nombres[indiceMasRapido]);

        System.out.println(" Complejidad de tiempo — HashMap.mostrarInventario()");
        System.out.println(" El método itera sobre todos los pares");
        System.out.println(" categoría) del mapa usando entrySet()");
        System.out.println(" - entrySet() en HashMap  → O(1)");
        System.out.println(" - Iteración sobre N pares → O(N)");
        System.out.println("- System.out.printf por par → O(1)");
        System.out.println("TOTAL: O(N)  donde N = número de productos");
    }

    private static int leerEntero(Scanner scanner) {
        while (true) {
            String linea = scanner.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.print("Por favor ingrese un número válido: ");
            }
        }
    }
}