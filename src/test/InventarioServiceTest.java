package test;
import main.InventarioService;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("main.InventarioService - Operaciones 1 a 6")
public class InventarioServiceTest {


    private Map<String, String>  inventario;
    private Map<String, Integer> coleccion;

    private final ByteArrayOutputStream salidaCapturada = new ByteArrayOutputStream();
    private PrintStream salidaOriginal;

    @BeforeEach
    void setup() {
        // Inventario de prueba con productos de distintas categorías
        inventario = new HashMap<>();
        inventario.put("Coca cola 1 litro",          "Bebidas");
        inventario.put("Coca cola 2 litros",          "Bebidas");
        inventario.put("Té frío",                     "Bebidas");
        inventario.put("Res",                         "Carnes");
        inventario.put("Pollo",                       "Carnes");
        inventario.put("Cerdo",                       "Carnes");
        inventario.put("Manzana roja",                "Frutas");
        inventario.put("Manzana verde",               "Frutas");
        inventario.put("Queso de cabra",              "Lácteos");
        inventario.put("Leche entera",                "Lácteos");
        inventario.put("Sillas de jardín",            "Mueble de terraza");
        inventario.put("Sirope de regaliz",           "Condimentos");

        coleccion = new HashMap<>();

        salidaOriginal = System.out;
        System.setOut(new PrintStream(salidaCapturada));
    }

    @AfterEach
    void teardown() {
        System.setOut(salidaOriginal);
    }

    private String salida() {
        return salidaCapturada.toString();
    }

    @Nested
    @DisplayName("Operación 1 - agregarProducto()")
    class AgregarProductoTests {

        @Test
        @DisplayName("Agregar producto válido lo añade a la colección con cantidad 1")
        void testAgregarProductoValido() {
            InventarioService.agregarProducto(inventario, coleccion, "Res");

            assertTrue(coleccion.containsKey("Res"), "El producto debe estar en la colección");
            assertEquals(1, coleccion.get("Res"), "La cantidad inicial debe ser 1");
        }

        @Test
        @DisplayName("Agregar el mismo producto dos veces incrementa la cantidad a 2")
        void testAgregarMismoProductoDosVeces() {
            InventarioService.agregarProducto(inventario, coleccion, "Pollo");
            InventarioService.agregarProducto(inventario, coleccion, "Pollo");

            assertEquals(2, coleccion.get("Pollo"), "Dos inserciones del mismo producto deben dar cantidad 2");
        }

        @Test
        @DisplayName("Agregar el mismo producto cinco veces incrementa la cantidad a 5")
        void testAgregarMismoProductoVariasVeces() {
            for (int i = 0; i < 5; i++) {
                InventarioService.agregarProducto(inventario, coleccion, "Manzana roja");
            }
            assertEquals(5, coleccion.get("Manzana roja"));
        }

        @Test
        @DisplayName("Agregar dos productos distintos los añade ambos a la colección")
        void testAgregarDosProductosDistintos() {
            InventarioService.agregarProducto(inventario, coleccion, "Res");
            InventarioService.agregarProducto(inventario, coleccion, "Manzana roja");

            assertEquals(2, coleccion.size());
            assertEquals(1, coleccion.get("Res"));
            assertEquals(1, coleccion.get("Manzana roja"));
        }

        @Test
        @DisplayName("Agregar producto que no existe muestra error y NO modifica la colección")
        void testAgregarProductoInexistente() {
            InventarioService.agregarProducto(inventario, coleccion, "Producto Fantasma");

            assertTrue(coleccion.isEmpty(), "La colección no debe modificarse con un producto inexistente");
            assertTrue(salida().contains("Error"),
                    "Debe mostrar un mensaje de error");
        }

        @Test
        @DisplayName("Agregar producto con nombre vacío muestra error")
        void testAgregarProductoNombreVacio() {
            InventarioService.agregarProducto(inventario, coleccion, "");

            assertTrue(coleccion.isEmpty());
            assertTrue(salida().contains("Error"));
        }

        @Test
        @DisplayName("La colección confirma el mensaje de producto agregado exitosamente")
        void testMensajeExitoAlAgregar() {
            InventarioService.agregarProducto(inventario, coleccion, "Leche entera");

            assertTrue(salida().contains("Leche entera"),
                    "El mensaje de éxito debe mencionar el producto agregado");
        }
    }


    @Nested
    @DisplayName("obtenerProductosPorCategoria() - auxiliar Op.1")
    class ObtenerProductosPorCategoriaTests {

        @Test
        @DisplayName("Retorna todos los productos de la categoría 'Bebidas'")
        void testCategoriaBebidas() {
            List<String> resultado = InventarioService.obtenerProductosPorCategoria(inventario, "Bebidas");

            assertEquals(3, resultado.size(), "Deben haber 3 bebidas");
            assertTrue(resultado.contains("Coca cola 1 litro"));
            assertTrue(resultado.contains("Coca cola 2 litros"));
            assertTrue(resultado.contains("Té frío"));
        }

        @Test
        @DisplayName("Retorna todos los productos de la categoría 'Carnes'")
        void testCategoriaCarnes() {
            List<String> resultado = InventarioService.obtenerProductosPorCategoria(inventario, "Carnes");

            assertEquals(3, resultado.size());
            assertTrue(resultado.containsAll(Arrays.asList("Res", "Pollo", "Cerdo")));
        }

        @Test
        @DisplayName("La búsqueda es case-insensitive ('bebidas' == 'Bebidas')")
        void testCategoriaInsensible() {
            List<String> resultado = InventarioService.obtenerProductosPorCategoria(inventario, "bebidas");

            assertFalse(resultado.isEmpty(),
                    "Debe encontrar productos aunque el caso de la categoría sea diferente");
        }

        @Test
        @DisplayName("Categoría inexistente retorna lista vacía (no null)")
        void testCategoriaInexistente() {
            List<String> resultado = InventarioService.obtenerProductosPorCategoria(inventario, "Electrónicos");

            assertNotNull(resultado, "No debe retornar null");
            assertTrue(resultado.isEmpty(), "Debe retornar lista vacía para categoría inexistente");
        }

        @Test
        @DisplayName("Categoría vacía retorna lista vacía")
        void testCategoriaVacia() {
            List<String> resultado = InventarioService.obtenerProductosPorCategoria(inventario, "");

            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
        }
    }

    @Nested
    @DisplayName("Operación 2 - mostrarCategoria()")
    class MostrarCategoriaTests {

        @Test
        @DisplayName("Producto existente muestra su categoría correcta")
        void testProductoExistenteMuestraCategoria() {
            InventarioService.mostrarCategoria(inventario, "Res");
            assertTrue(salida().contains("Carnes"),
                    "Debe mostrar 'Carnes' para el producto 'Res'");
        }

        @Test
        @DisplayName("Otro producto existente muestra su categoría correcta")
        void testOtroProductoExistente() {
            InventarioService.mostrarCategoria(inventario, "Coca cola 1 litro");
            assertTrue(salida().contains("Bebidas"));
        }

        @Test
        @DisplayName("Producto inexistente muestra mensaje de error")
        void testProductoInexistenteMuestraError() {
            InventarioService.mostrarCategoria(inventario, "Producto XYZ");
            assertTrue(salida().contains("Error") || salida().contains("no encontrado"),
                    "Debe mostrar mensaje de error para producto inexistente");
        }

        @Test
        @DisplayName("La salida incluye el nombre del producto buscado")
        void testSalidaIncluyeNombreProducto() {
            InventarioService.mostrarCategoria(inventario, "Manzana roja");
            assertTrue(salida().contains("Manzana roja"),
                    "La salida debe incluir el nombre del producto buscado");
        }
    }


    @Nested
    @DisplayName("Operación 3 - mostrarColeccion()")
    class MostrarColeccionTests {

        @Test
        @DisplayName("Colección vacía muestra mensaje apropiado")
        void testColeccionVacia() {
            InventarioService.mostrarColeccion(inventario, coleccion);
            assertTrue(salida().contains("vacía") || salida().contains("vacia"),
                    "Debe indicar que la colección está vacía");
        }

        @Test
        @DisplayName("Muestra el producto agregado con su categoría y cantidad")
        void testMuestraProductoAgregado() {
            coleccion.put("Res", 2);

            InventarioService.mostrarColeccion(inventario, coleccion);
            String s = salida();

            assertTrue(s.contains("Res"),      "Debe mostrar el nombre del producto");
            assertTrue(s.contains("Carnes"),   "Debe mostrar la categoría");
            assertTrue(s.contains("2"),        "Debe mostrar la cantidad");
        }

        @Test
        @DisplayName("Muestra todos los productos de la colección cuando hay varios")
        void testMuestraVariosProductos() {
            coleccion.put("Res",          3);
            coleccion.put("Manzana roja", 1);
            coleccion.put("Leche entera", 2);

            InventarioService.mostrarColeccion(inventario, coleccion);
            String s = salida();

            assertTrue(s.contains("Res"));
            assertTrue(s.contains("Manzana roja"));
            assertTrue(s.contains("Leche entera"));
        }

        @Test
        @DisplayName("No debe lanzar excepción con colección de un solo elemento")
        void testColeccionUnElemento() {
            coleccion.put("Pollo", 1);
            assertDoesNotThrow(() -> InventarioService.mostrarColeccion(inventario, coleccion));
        }
    }

    @Nested
    @DisplayName("Operación 4 - mostrarColeccionOrdenada()")
    class MostrarColeccionOrdenadaTests {

        @Test
        @DisplayName("Colección vacía muestra mensaje apropiado")
        void testColeccionVacia() {
            InventarioService.mostrarColeccionOrdenada(inventario, coleccion);
            assertTrue(salida().contains("vacía") || salida().contains("vacia"));
        }

        @Test
        @DisplayName("Los productos aparecen ordenados por categoría (Bebidas antes que Carnes)")
        void testOrdenPorCategoria() {
            coleccion.put("Res",              1);  // Carnes
            coleccion.put("Coca cola 1 litro", 1); // Bebidas

            InventarioService.mostrarColeccionOrdenada(inventario, coleccion);
            String s = salida();

            int posBebidas = s.indexOf("Bebidas");
            int posCarnes  = s.indexOf("Carnes");

            assertTrue(posBebidas < posCarnes,
                    "Bebidas debe aparecer antes que Carnes en el ordenamiento");
        }

        @Test
        @DisplayName("Dentro de la misma categoría, productos ordenados alfabéticamente")
        void testOrdenSecundarioPorProducto() {
            coleccion.put("Pollo", 1);
            coleccion.put("Cerdo", 1);
            coleccion.put("Res",   1);

            InventarioService.mostrarColeccionOrdenada(inventario, coleccion);
            String s = salida();

            int posCerdo = s.indexOf("Cerdo");
            int posPollo = s.indexOf("Pollo");
            int posRes   = s.indexOf("Res");

            assertTrue(posCerdo < posPollo && posPollo < posRes,
                    "Dentro de Carnes: Cerdo < Pollo < Res (orden alfabético)");
        }

        @Test
        @DisplayName("No lanza excepción con un solo elemento en la colección")
        void testUnElemento() {
            coleccion.put("Res", 5);
            assertDoesNotThrow(() ->
                    InventarioService.mostrarColeccionOrdenada(inventario, coleccion));
        }
    }

    @Nested
    @DisplayName("Operación 5 - mostrarInventario()")
    class MostrarInventarioTests {

        @Test
        @DisplayName("Inventario vacío muestra mensaje apropiado")
        void testInventarioVacio() {
            InventarioService.mostrarInventario(new HashMap<>());
            assertTrue(salida().contains("vacío") || salida().contains("vacio"));
        }

        @Test
        @DisplayName("Muestra todos los productos del inventario")
        void testMuestraTodosLosProductos() {
            InventarioService.mostrarInventario(inventario);
            String s = salida();

            for (String producto : inventario.keySet()) {
                assertTrue(s.contains(producto),
                        "El inventario debe mostrar el producto: " + producto);
            }
        }

        @Test
        @DisplayName("Muestra las categorías de los productos")
        void testMuestraCategorias() {
            InventarioService.mostrarInventario(inventario);
            String s = salida();

            assertTrue(s.contains("Bebidas"));
            assertTrue(s.contains("Carnes"));
            assertTrue(s.contains("Frutas"));
        }

        @Test
        @DisplayName("No lanza excepción con inventario de un solo producto")
        void testInventarioUnProducto() {
            Map<String, String> mini = new HashMap<>();
            mini.put("Res", "Carnes");

            assertDoesNotThrow(() -> InventarioService.mostrarInventario(mini));
            assertTrue(salida().contains("Res"));
            assertTrue(salida().contains("Carnes"));
        }

        @Test
        @DisplayName("La cantidad de productos en la salida coincide con el tamaño del mapa")
        void testCantidadProductosMostrados() {
            InventarioService.mostrarInventario(inventario);
            String s = salida();

            long lineasProducto = s.lines()
                    .filter(l -> inventario.keySet().stream().anyMatch(l::contains))
                    .count();

            assertEquals(inventario.size(), lineasProducto,
                    "Cada producto debe aparecer exactamente una vez");
        }
    }


    @Nested
    @DisplayName("Operación 6 - mostrarInventarioOrdenado()")
    class MostrarInventarioOrdenadoTests {

        @Test
        @DisplayName("Inventario vacío muestra mensaje apropiado")
        void testInventarioVacio() {
            InventarioService.mostrarInventarioOrdenado(new HashMap<>());
            assertTrue(salida().contains("vacío") || salida().contains("vacio"));
        }

        @Test
        @DisplayName("Bebidas aparece antes que Carnes en el inventario ordenado")
        void testBebidasAntesQueCarnes() {
            InventarioService.mostrarInventarioOrdenado(inventario);
            String s = salida();

            int posBebidas = s.indexOf("Bebidas");
            int posCarnes  = s.indexOf("Carnes");

            assertTrue(posBebidas < posCarnes,
                    "Bebidas (B) debe aparecer antes que Carnes (C) al ordenar por categoría");
        }

        @Test
        @DisplayName("Lácteos aparece después de Frutas en el inventario ordenado")
        void testLacteosDesquepuesDeFrutas() {
            InventarioService.mostrarInventarioOrdenado(inventario);
            String s = salida();

            int posFrutas  = s.indexOf("Frutas");
            int posLacteos = s.indexOf("Lácteos");

            assertTrue(posFrutas < posLacteos,
                    "Frutas (F) debe aparecer antes que Lácteos (L)");
        }

        @Test
        @DisplayName("Muestra todos los productos del inventario sin omitir ninguno")
        void testMuestraTodosLosProductos() {
            InventarioService.mostrarInventarioOrdenado(inventario);
            String s = salida();

            for (String producto : inventario.keySet()) {
                assertTrue(s.contains(producto),
                        "El inventario ordenado debe incluir: " + producto);
            }
        }

        @Test
        @DisplayName("Dentro de la misma categoría, productos en orden alfabético")
        void testOrdenAlfabeticoEnMismaCategoria() {
            // Frutas: Manzana roja, Manzana verde
            InventarioService.mostrarInventarioOrdenado(inventario);
            String s = salida();

            int posManzanaRoja  = s.indexOf("Manzana roja");
            int posManzanaVerde = s.indexOf("Manzana verde");

            assertTrue(posManzanaRoja < posManzanaVerde,
                    "Manzana roja debe aparecer antes que Manzana verde (alfabético)");
        }
    }
}
