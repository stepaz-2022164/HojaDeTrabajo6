import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class MapFactory<K, V> {

    public Map<K, V> getMap(int opcion) {
        switch (opcion) {
            case 1:
                return new HashMap<>();
            case 2:
                return new TreeMap<>();
            case 3:
                return new LinkedHashMap<>();
            default:
                System.out.println("Opción no válida, se usará HashMap por defecto.");
                return new HashMap<>();
        }
    }
}