package test;

import main.MapFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MapFactory - Patrón Factory para selección de implementación MAP")
public class MapFactoryTest {

    private final MapFactory<String, String> factory = new MapFactory<>();

    @Test
    @DisplayName("getMap(1) debe retornar una instancia de HashMap")
    void testOpcion1RetornaHashMap() {
        Map<String, String> mapa = factory.getMap(1);
        assertNotNull(mapa, "El mapa no debe ser null");
        assertInstanceOf(HashMap.class, mapa,
                "getMap(1) debe retornar HashMap");
    }


    @Test
    @DisplayName("getMap(2) debe retornar una instancia de TreeMap")
    void testOpcion2RetornaTreeMap() {
        Map<String, String> mapa = factory.getMap(2);
        assertNotNull(mapa);
        assertInstanceOf(TreeMap.class, mapa,
                "getMap(2) debe retornar TreeMap");
    }


    @Test
    @DisplayName("getMap(3) debe retornar una instancia de LinkedHashMap")
    void testOpcion3RetornaLinkedHashMap() {
        Map<String, String> mapa = factory.getMap(3);
        assertNotNull(mapa);
        assertInstanceOf(LinkedHashMap.class, mapa,
                "getMap(3) debe retornar LinkedHashMap");
    }

    @Test
    @DisplayName("getMap con opción inválida (0) debe retornar HashMap por defecto")
    void testOpcionInvalidaCero() {
        Map<String, String> mapa = factory.getMap(0);
        assertNotNull(mapa);
        assertInstanceOf(HashMap.class, mapa,
                "Opción inválida 0 debe devolver HashMap por defecto");
    }

    @Test
    @DisplayName("getMap con opción inválida (99) debe retornar HashMap por defecto")
    void testOpcionInvalidaAlta() {
        Map<String, String> mapa = factory.getMap(99);
        assertNotNull(mapa);
        assertInstanceOf(HashMap.class, mapa,
                "Opción inválida 99 debe devolver HashMap por defecto");
    }

    @Test
    @DisplayName("getMap con opción negativa debe retornar HashMap por defecto")
    void testOpcionNegativa() {
        Map<String, String> mapa = factory.getMap(-5);
        assertNotNull(mapa);
        assertInstanceOf(HashMap.class, mapa);
    }


    @Test
    @DisplayName("Cada llamada a getMap debe retornar una nueva instancia independiente")
    void testInstanciasIndependientes() {
        Map<String, String> mapa1 = factory.getMap(1);
        Map<String, String> mapa2 = factory.getMap(1);

        assertNotSame(mapa1, mapa2,
                "Cada llamada debe crear una instancia nueva (no la misma referencia)");

        mapa1.put("Res", "Carnes");
        assertFalse(mapa2.containsKey("Res"),
                "Modificar mapa1 no debe afectar mapa2");
    }

    @Test
    @DisplayName("Todos los mapas retornados deben estar vacíos inicialmente")
    void testMapasInicialmenteVacios() {
        assertTrue(factory.getMap(1).isEmpty(), "HashMap debe estar vacío");
        assertTrue(factory.getMap(2).isEmpty(), "TreeMap debe estar vacío");
        assertTrue(factory.getMap(3).isEmpty(), "LinkedHashMap debe estar vacío");
    }

    @Test
    @DisplayName("TreeMap debe ordenar las claves alfabéticamente")
    void testTreeMapOrdenAlfabetico() {
        Map<String, String> treeMap = factory.getMap(2);
        treeMap.put("Zanahoria", "Verduras");
        treeMap.put("Apio",      "Verduras");
        treeMap.put("Manzana",   "Frutas");

        String primeraLlave = treeMap.keySet().iterator().next();
        assertEquals("Apio", primeraLlave,
                "TreeMap debe devolver 'Apio' como primera clave (orden alfabético)");
    }


    @Test
    @DisplayName("LinkedHashMap debe mantener el orden de inserción")
    void testLinkedHashMapOrdenInsercion() {
        Map<String, String> linked = factory.getMap(3);
        linked.put("Primero",  "A");
        linked.put("Segundo",  "B");
        linked.put("Tercero",  "C");

        String[] claves = linked.keySet().toArray(new String[0]);
        assertEquals("Primero", claves[0]);
        assertEquals("Segundo", claves[1]);
        assertEquals("Tercero", claves[2]);
    }
}
