import java.util.Map;

public class InventarioService {

    public static void agregarProducto(Map<String, String> inventario,
                                       Map<String, Integer> coleccion,
                                       String producto) {

        if (!inventario.containsKey(producto)) {
            System.out.println("Error: el producto no existe");
            return;
        }

        int cantidadActual = coleccion.getOrDefault(producto, 0);
        coleccion.put(producto, cantidadActual + 1);

        System.out.println("Producto agregado correctamente");
    }

    public static void mostrarCategoria(Map<String, String> inventario,
                                        String producto) {

        if (inventario.containsKey(producto)) {
            System.out.println("Categoría: " + inventario.get(producto));
        } else {
            System.out.println("Producto no encontrado");
        }
    }

    public static void mostrarColeccion(Map<String, String> inventario,
                                        Map<String, Integer> coleccion) {

        if (coleccion.isEmpty()) {
            System.out.println("La colección está vacía");
            return;
        }

        for (String producto : coleccion.keySet()) {
            String categoria = inventario.get(producto);
            int cantidad = coleccion.get(producto);

            System.out.println(producto + " | " + categoria + " | " + cantidad);
        }
    }

    public static void mostrarInventario(Map<String, String> inventario) {
        for (String producto : inventario.keySet()) {
            System.out.println(producto + " | " + inventario.get(producto));
        }
    }
}