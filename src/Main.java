import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MapFactory<String, String> inventarioFactory = new MapFactory<>();
        MapFactory<String, Integer> coleccionFactory = new MapFactory<>();

        System.out.println("Seleccione la implementación de MAP a utilizar:");
        System.out.println("1. HashMap");
        System.out.println("2. TreeMap");
        System.out.println("3. LinkedHashMap");
        System.out.print("Opción: ");
        int tipoMap = scanner.nextInt();
        scanner.nextLine(); // Consumir salto de línea

        Map<String, String> inventario = inventarioFactory.getMap(tipoMap);
        Map<String, Integer> coleccionUsuario = coleccionFactory.getMap(tipoMap);

        inventario.put("Sillas de jardín", "Mueble de terraza");
        inventario.put("Coca cola 1 litro", "Bebidas");
        inventario.put("Manzana roja", "Frutas");
        inventario.put("Res", "Carnes");

        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- MENÚ ---");
            System.out.println("1. Agregar producto a la colección");
            System.out.println("2. Mostrar categoría de un producto");
            System.out.println("3. Mostrar colección del usuario");
            System.out.println("4. Mostrar colección ordenada por categoría");
            System.out.println("5. Mostrar todo el inventario");
            System.out.println("6. Mostrar inventario ordenado por categoría");
            System.out.println("7. Salir");
            System.out.print("Elija una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el nombre del producto a agregar: ");
                    String prodAgregar = scanner.nextLine();
                    InventarioService.agregarProducto(inventario, coleccionUsuario, prodAgregar);
                    break;
                case 2:
                    System.out.print("Ingrese el nombre del producto: ");
                    String prodCategoria = scanner.nextLine();
                    InventarioService.mostrarCategoria(inventario, prodCategoria);
                    break;
                case 3:
                    InventarioService.mostrarColeccion(inventario, coleccionUsuario);
                    break;
                case 4:
                    InventarioService.mostrarColeccionOrdenada(inventario, coleccionUsuario);
                    break;
                case 5:
                    long startTime = System.nanoTime();
                    InventarioService.mostrarInventario(inventario);
                    long endTime = System.nanoTime();
                    long duration = (endTime - startTime);
                    System.out.println("\n[Profiler] Tiempo de ejecución (ns): " + duration);
                    break;
                case 6:
                    InventarioService.mostrarInventarioOrdenado(inventario);
                    break;
                case 7:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
        scanner.close();
    }
}