package main;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InventarioService {

    public static void agregarProducto(Map<String, String> inventario,
                                       Map<String, Integer> coleccion,
                                       String producto) {

        if (!inventario.containsKey(producto)) {
            System.out.println("Error: el producto \"" + producto + "\" no existe en el inventario.");
            return;
        }

        int cantidadActual = coleccion.getOrDefault(producto, 0);
        coleccion.put(producto, cantidadActual + 1);
        System.out.println("Producto \"" + producto + "\" agregado. Cantidad actual: " + (cantidadActual + 1));
    }

    public static List<String> obtenerProductosPorCategoria(Map<String, String> inventario,
                                                            String categoria) {
        return inventario.entrySet().stream()
                .filter(e -> e.getValue().equalsIgnoreCase(categoria))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static void mostrarCategoria(Map<String, String> inventario,
                                        String producto) {
        if (inventario.containsKey(producto)) {
            System.out.println("Categoría de \"" + producto + "\": " + inventario.get(producto));
        } else {
            System.out.println("Error: producto \"" + producto + "\" no encontrado en el inventario.");
        }
    }

    public static void mostrarColeccion(Map<String, String> inventario,
                                        Map<String, Integer> coleccion) {
        if (coleccion.isEmpty()) {
            System.out.println("La colección está vacía.");
            return;
        }

        System.out.println("\n--- Colección del Usuario ---");
        System.out.printf("%-40s %-25s %s%n", "Producto", "Categoría", "Cantidad");
        System.out.println("-".repeat(75));

        for (Map.Entry<String, Integer> entry : coleccion.entrySet()) {
            String producto  = entry.getKey();
            String categoria = inventario.getOrDefault(producto, "Desconocida");
            int cantidad     = entry.getValue();
            System.out.printf("%-40s %-25s %d%n", producto, categoria, cantidad);
        }
    }

    public static void mostrarColeccionOrdenada(Map<String, String> inventario,
                                                Map<String, Integer> coleccion) {
        if (coleccion.isEmpty()) {
            System.out.println("La colección está vacía.");
            return;
        }

        System.out.println("\n--- Colección del Usuario (ordenada por categoría) ---");
        System.out.printf("%-40s %-25s %s%n", "Producto", "Categoría", "Cantidad");
        System.out.println("-".repeat(75));

        coleccion.entrySet().stream()
                .sorted((e1, e2) -> {
                    String cat1 = inventario.getOrDefault(e1.getKey(), "");
                    String cat2 = inventario.getOrDefault(e2.getKey(), "");
                    int cmp = cat1.compareToIgnoreCase(cat2);
                    return cmp != 0 ? cmp : e1.getKey().compareToIgnoreCase(e2.getKey());
                })
                .forEach(e -> {
                    String producto  = e.getKey();
                    String categoria = inventario.getOrDefault(producto, "Desconocida");
                    int cantidad     = e.getValue();
                    System.out.printf("%-40s %-25s %d%n", producto, categoria, cantidad);
                });
    }

    public static void mostrarInventario(Map<String, String> inventario) {
        if (inventario.isEmpty()) {
            System.out.println("El inventario está vacío.");
            return;
        }

        System.out.println("\n--- Inventario Completo ---");
        System.out.printf("%-40s %s%n", "Producto", "Categoría");
        System.out.println("-".repeat(65));

        for (Map.Entry<String, String> entry : inventario.entrySet()) {
            System.out.printf("%-40s %s%n", entry.getKey(), entry.getValue());
        }
    }

    public static void mostrarInventarioOrdenado(Map<String, String> inventario) {
        if (inventario.isEmpty()) {
            System.out.println("El inventario está vacío.");
            return;
        }

        System.out.println("\n--- Inventario Completo (ordenado por categoría) ---");
        System.out.printf("%-40s %s%n", "Producto", "Categoría");
        System.out.println("-".repeat(65));

        inventario.entrySet().stream()
                .sorted((e1, e2) -> {
                    int cmp = e1.getValue().compareToIgnoreCase(e2.getValue());
                    return cmp != 0 ? cmp : e1.getKey().compareToIgnoreCase(e2.getKey());
                })
                .forEach(e -> System.out.printf("%-40s %s%n", e.getKey(), e.getValue()));
    }
}