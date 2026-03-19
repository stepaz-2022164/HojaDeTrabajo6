package test;

import main.InventarioLoader;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InventarioLoader - Lectura del archivo inventario.txt")
public class InventarioLoaderTest {

    private File archivoTemp;

    private final ByteArrayOutputStream salidaCapturada = new ByteArrayOutputStream();
    private PrintStream salidaOriginal;

    @BeforeEach
    void setup() {
        salidaOriginal = System.out;
        System.setOut(new PrintStream(salidaCapturada));
    }

    @AfterEach
    void teardown() {
        System.setOut(salidaOriginal);
        if (archivoTemp != null && archivoTemp.exists()) {
            archivoTemp.delete();
        }
    }


    private File crearArchivoTemp(String contenido) throws IOException {
        File f = File.createTempFile("inventario_test_", ".txt");
        try (FileWriter fw = new FileWriter(f)) {
            fw.write(contenido);
        }
        return f;
    }


    @Test
    @DisplayName("Debe cargar correctamente líneas con formato 'Categoría | Producto'")
    void testCargaFormatoCorrecto() throws IOException {
        archivoTemp = crearArchivoTemp(
                "Bebidas | Coca cola 1 litro\n" +
                        "Bebidas | Té frío\n" +
                        "Carnes  | Res\n"
        );

        Map<String, String> inventario = new HashMap<>();
        InventarioLoader.cargarDesdeArchivo(archivoTemp.getAbsolutePath(), inventario);

        assertEquals(3, inventario.size(), "Deben cargarse 3 productos");
        assertEquals("Bebidas", inventario.get("Coca cola 1 litro"));
        assertEquals("Bebidas", inventario.get("Té frío"));
        assertEquals("Carnes",  inventario.get("Res"));
    }

    @Test
    @DisplayName("Debe recortar espacios en blanco de categoría y producto")
    void testRecortaEspacios() throws IOException {
        archivoTemp = crearArchivoTemp("   Frutas   |   Manzana roja   \n");

        Map<String, String> inventario = new HashMap<>();
        InventarioLoader.cargarDesdeArchivo(archivoTemp.getAbsolutePath(), inventario);

        assertTrue(inventario.containsKey("Manzana roja"),
                "La clave debe estar sin espacios extras");
        assertEquals("Frutas", inventario.get("Manzana roja"),
                "La categoría debe estar sin espacios extras");
    }

    @Test
    @DisplayName("Debe ignorar líneas vacías sin lanzar excepción")
    void testIgnoraLineasVacias() throws IOException {
        archivoTemp = crearArchivoTemp(
                "Bebidas | Coca cola 1 litro\n" +
                        "\n" +
                        "\n" +
                        "Carnes | Pollo\n"
        );

        Map<String, String> inventario = new HashMap<>();
        InventarioLoader.cargarDesdeArchivo(archivoTemp.getAbsolutePath(), inventario);

        assertEquals(2, inventario.size(), "Líneas vacías no deben contarse como productos");
    }

    @Test
    @DisplayName("Debe cargar los 29 productos completos del archivo real del profesor")
    void testCargaArchivoCompleto() throws IOException {
        archivoTemp = crearArchivoTemp(
                "Mueble de terraza | Mesas de jardín\n" +
                        "Mueble de terraza | Sillas de jardín\n" +
                        "Mueble de terraza | Conjuntos mesas y sillas de jardín\n" +
                        "Mueble de terraza | Mesas de Ping Pong exteriores\n" +
                        "Sillones de masaje | Cojines y colchonetas de masaje\n" +
                        "Sillones de masaje | Sillones relax y sofás de masajes\n" +
                        "Sillones de masaje | Sillones de masajes avanzados\n" +
                        "Sillones de masaje | Sofás camas\n" +
                        "Bebidas | Cerveza tibetana Barley\n" +
                        "Bebidas | Té frios\n" +
                        "Bebidas | Coca cola 1 litro\n" +
                        "Bebidas | Coca cola 2 litros\n" +
                        "Condimentos | Sirope de regaliz\n" +
                        "Condimentos | Especies Cajun del chef\n" +
                        "Condimentos | Mezcla Gumbo del chef\n" +
                        "Frutas | Peras secas\n" +
                        "Frutas | Pasas\n" +
                        "Frutas | Manzana roja\n" +
                        "Frutas | Manzana verde\n" +
                        "Carnes | Res\n" +
                        "Carnes | Pollo\n" +
                        "Carnes | Cerdo\n" +
                        "Carnes | Camarones\n" +
                        "Carnes | Pescados\n" +
                        "Lácteos | Queso de cabra\n" +
                        "Lácteos | Queso Manchego\n" +
                        "Lácteos | Leche descremada\n" +
                        "Lácteos | Leche deslactosada\n" +
                        "Lácteos | Leche entera\n"
        );

        Map<String, String> inventario = new HashMap<>();
        InventarioLoader.cargarDesdeArchivo(archivoTemp.getAbsolutePath(), inventario);

        assertEquals(29, inventario.size(), "El archivo completo tiene 29 productos");
    }


    @Test
    @DisplayName("Debe mostrar error si el archivo no existe y dejar el mapa vacío")
    void testArchivoNoExiste() {
        Map<String, String> inventario = new HashMap<>();
        InventarioLoader.cargarDesdeArchivo("archivo_que_no_existe.txt", inventario);

        assertTrue(inventario.isEmpty(),
                "El inventario debe quedar vacío si el archivo no existe");

        String salida = salidaCapturada.toString();
        assertTrue(salida.contains("Error"),
                "Debe mostrar mensaje de error cuando el archivo no existe");
    }

    @Test
    @DisplayName("Debe omitir líneas con formato inválido (sin '|') y continuar")
    void testLineaSinSeparador() throws IOException {
        archivoTemp = crearArchivoTemp(
                "Bebidas | Coca cola 1 litro\n" +
                        "lineamalformada\n" +
                        "Carnes | Pollo\n"
        );

        Map<String, String> inventario = new HashMap<>();
        InventarioLoader.cargarDesdeArchivo(archivoTemp.getAbsolutePath(), inventario);

        assertEquals(2, inventario.size(),
                "La línea malformada debe omitirse; se cargan las 2 válidas");

        String salida = salidaCapturada.toString();
        assertTrue(salida.contains("Advertencia"),
                "Debe advertir sobre la línea malformada");
    }

    @Test
    @DisplayName("Debe omitir líneas donde categoría o producto esté vacío")
    void testCamposVacios() throws IOException {
        archivoTemp = crearArchivoTemp(
                " | Producto sin categoría\n" +
                        "Bebidas | \n" +
                        "Carnes | Res\n"
        );

        Map<String, String> inventario = new HashMap<>();
        InventarioLoader.cargarDesdeArchivo(archivoTemp.getAbsolutePath(), inventario);

        assertEquals(1, inventario.size(),
                "Solo debe cargarse el producto con categoría y nombre válidos");
        assertTrue(inventario.containsKey("Res"));
    }

    @Test
    @DisplayName("Archivo vacío no debe lanzar excepción y deja mapa vacío")
    void testArchivoVacio() throws IOException {
        archivoTemp = crearArchivoTemp("");

        Map<String, String> inventario = new HashMap<>();
        assertDoesNotThrow(() ->
                InventarioLoader.cargarDesdeArchivo(archivoTemp.getAbsolutePath(), inventario));

        assertTrue(inventario.isEmpty());
    }

    @Test
    @DisplayName("No debe sobrescribir entradas existentes con productos duplicados (último gana)")
    void testProductoDuplicadoEnArchivo() throws IOException {
        archivoTemp = crearArchivoTemp(
                "Bebidas  | Coca cola 1 litro\n" +
                        "Lácteos  | Coca cola 1 litro\n"   // mismo producto, otra categoría
        );

        Map<String, String> inventario = new HashMap<>();
        InventarioLoader.cargarDesdeArchivo(archivoTemp.getAbsolutePath(), inventario);

        assertEquals(1, inventario.size());
        assertEquals("Lácteos", inventario.get("Coca cola 1 litro"),
                "La segunda entrada debe sobrescribir la primera (comportamiento de Map.put)");
    }
}
