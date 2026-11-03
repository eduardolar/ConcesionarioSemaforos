package concesionario;

/**
 * Punto de entrada del programa.
 * Lanza 9 clientes (hilos) que intentan probar 4 vehículos.
 */
public class Principal {

    // Constantes del problema (fáciles de cambiar si el enunciado cambia).
    private static final int TOTAL_VEHICULOS = 4;
    private static final int TOTAL_CLIENTES = 9;

    public static void main(String[] args) {
        // El "concesionario" será el gestor de recursos (vehículos) y sincronización (semáforo).
        Concesionario concesionario = new Concesionario(TOTAL_VEHICULOS);

        // Crear y arrancar los hilos de clientes.
        for (int i = 1; i <= TOTAL_CLIENTES; i++) {
            String nombreCliente = "Cliente-" + i;
            Thread hiloCliente = new Thread(new Cliente(nombreCliente, concesionario));
            hiloCliente.start();
        }
    }
}
