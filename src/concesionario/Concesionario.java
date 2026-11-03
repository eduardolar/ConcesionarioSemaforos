package concesionario;

import java.util.concurrent.Semaphore;

/**
 * Gestiona los vehículos disponibles y sincroniza el acceso mediante un semáforo.
 * - El semáforo limita a N clientes simultáneos (N = número de vehículos).
 * - Además, se asigna un vehículo concreto (1..N) a cada cliente.
 */
public class Concesionario {

    // Semáforo: controla cuántos clientes pueden "entrar" a la vez (máximo = vehículos).
    private final Semaphore semaforoVehiculos;

    // Estado de cada vehículo: true = libre, false = ocupado.
    // Índice 0 corresponde al vehículo 1, índice 1 al vehículo 2, etc.
    private final boolean[] vehiculoLibre;

    /**
     * Crea el concesionario con un número fijo de vehículos.
     * @param totalVehiculos número de vehículos disponibles (p.ej. 4)
     */
    public Concesionario(int totalVehiculos) {
        this.semaforoVehiculos = new Semaphore(totalVehiculos, true); // "true" = más justo (FIFO aproximado)
        this.vehiculoLibre = new boolean[totalVehiculos];

        // Inicialmente todos los vehículos están libres.
        for (int i = 0; i < totalVehiculos; i++) {
            vehiculoLibre[i] = true;
        }
    }

    /**
     * Solicita un vehículo:
     * 1) Espera (si hace falta) hasta que haya "hueco" según el semáforo.
     * 2) Asigna un vehículo concreto libre y lo marca como ocupado.
     *
     * @return número de vehículo asignado (1..N)
     * @throws InterruptedException si el hilo es interrumpido mientras espera
     */
    public int solicitarVehiculo() throws InterruptedException {
        // Paso 1: bloquear hasta que haya un vehículo disponible.
        semaforoVehiculos.acquire();

        // Paso 2: asignar el número de vehículo libre.
        synchronized (this) {
            for (int i = 0; i < vehiculoLibre.length; i++) {
                if (vehiculoLibre[i]) {
                    vehiculoLibre[i] = false;   // marcar como ocupado
                    return i + 1;               //  1..N
                }
            }
        }

        // Si llegase aquí, sería un estado incoherente (no debería ocurrir).
        // Liberamos el permiso para no bloquear el sistema.
        semaforoVehiculos.release();
        throw new IllegalStateException("Incoherencia: semáforo concedió permiso pero no hay vehículo libre.");
    }

    /**
     * Devuelve un vehículo:
     * 1) Marca el vehículo como libre.
     * 2) Libera un permiso del semáforo para que otro cliente pueda entrar.
     *
     * @param numeroVehiculo número de vehículo a devolver (1..N)
     */
    public void devolverVehiculo(int numeroVehiculo) {
        // Convertir de 1..N a 0..N-1
        int indice = numeroVehiculo - 1;

        synchronized (this) {
            vehiculoLibre[indice] = true;
        }

        // Avisar al semáforo de que hay un vehículo más disponible.
        semaforoVehiculos.release();
    }
}
