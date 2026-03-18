import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Map<String, String> inventario = new HashMap<>();
        Map<String, Integer> coleccion = new HashMap<>();

        // INVENTARIO
        inventario.put("Mesas de jardín", "Mueble de terraza");
        inventario.put("Sillas de jardín", "Mueble de terraza");
        inventario.put("Conjuntos mesas y sillas de jardín", "Mueble de terraza");
        inventario.put("Mesas de Ping Pong exteriores", "Mueble de terraza");

        inventario.put("Cojines y colchonetas de masaje", "Sillones de masaje");
        inventario.put("Sillones relax y sofás de masajes", "Sillones de masaje");
        inventario.put("Sillones de masajes avanzados", "Sillones de masaje");
        inventario.put("Sofás camas", "Sillones de masaje");

        inventario.put("Cerveza tibetana Barley", "Bebidas");
        inventario.put("Té frio", "Bebidas");
        inventario.put("Coca cola 1 litro", "Bebidas");
        inventario.put("Coca cola 2 litros", "Bebidas");

        inventario.put("Sirope de regaliz", "Condimentos");
        inventario.put("Especies Cajun del chef", "Condimentos");
        inventario.put("Mezcla Gumbo del chef", "Condimentos");

        inventario.put("Peras secas", "Frutas");
        inventario.put("Pasas", "Frutas");
        inventario.put("Manzana roja", "Frutas");
        inventario.put("Manzana verde", "Frutas");

        inventario.put("Res", "Carnes");
        inventario.put("Pollo", "Carnes");
        inventario.put("Cerdo", "Carnes");
        inventario.put("Camarones", "Carnes");
        inventario.put("Pescados", "Carnes");

        inventario.put("Queso de cabra", "Lácteos");
        inventario.put("Queso Manchego", "Lácteos");
        inventario.put("Leche descremada", "Lácteos");
        inventario.put("Leche deslactosada", "Lácteos");
        inventario.put("Leche entera", "Lácteos");

        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Agregar producto");
            System.out.println("2. Mostrar categoría");
            System.out.println("3. Mostrar colección");
            System.out.println("5. Mostrar inventario");
            System.out.println("0. Salir");

            opcion = sc.nextInt();
            sc.nextLine();

            switch(opcion) {
                case 1:
                    System.out.println("Ingrese producto:");
                    String p1 = sc.nextLine();
                    InventarioService.agregarProducto(inventario, coleccion, p1);
                    break;

                case 2:
                    System.out.println("Ingrese producto:");
                    String p2 = sc.nextLine();
                    InventarioService.mostrarCategoria(inventario, p2);
                    break;

                case 3:
                    InventarioService.mostrarColeccion(inventario, coleccion);
                    break;

                case 5:
                    InventarioService.mostrarInventario(inventario);
                    break;
            }

        } while(opcion != 0);
    }
}