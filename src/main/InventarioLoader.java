package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class InventarioLoader {

    public static void cargarDesdeArchivo(String ruta, Map<String, String> inventario) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            int numeroLinea = 0;

            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                linea = linea.trim();

                if (linea.isEmpty()) continue;

                String[] partes = linea.split("\\|");

                if (partes.length != 2) {
                    System.out.println("Advertencia: línea " + numeroLinea + " con formato inválido, se omite.");
                    continue;
                }

                String categoria = partes[0].trim();
                String producto  = partes[1].trim();

                if (categoria.isEmpty() || producto.isEmpty()) {
                    System.out.println("Advertencia: línea " + numeroLinea + " con campo vacío, se omite.");
                    continue;
                }

                inventario.put(producto, categoria);
            }

            System.out.println("Inventario cargado: " + inventario.size() + " productos leídos desde \"" + ruta + "\".");

        } catch (IOException e) {
            System.out.println("Error al leer el archivo \"" + ruta + "\": " + e.getMessage());
            System.out.println("Verifique que el archivo exista en el directorio de ejecución.");
        }
    }
}