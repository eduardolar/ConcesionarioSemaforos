package concesionario;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Representa a un cliente que quiere probar un vehículo.
 * Se ejecuta como tarea (Runnable) dentro de un hilo.
 */
public class Cliente implements Runnable {

    private final String nombre;
    private final Concesionario concesionario;

    /**
     * @param nombre nombre identificativo del cliente (p.ej. Cliente-1)
     * @param concesionario referencia al concesionario compartido por todos los hilos
     */
    public Cliente(String nombre, Concesionario concesionario) {
        this.nombre = nombre;
        this.concesionario = concesionario;
    }

    @Override
    public void run() {
        int vehiculoAsignado = -1;

        try {
            // Pedir vehículo (puede bloquear si no hay disponibles).
            vehiculoAsignado = concesionario.solicitarVehiculo();

            // Mensaje de inicio de uso 
            System.out.println(nombre + " empieza a usar el vehiculo " + vehiculoAsignado);

            // Simular el uso del vehículo durante un tiempo aleatorio (0.5s a 2s).
            int milisegundosUso = ThreadLocalRandom.current().nextInt(500, 2001);
            Thread.sleep(milisegundosUso);

            // Mensaje de fin de uso 
            System.out.println(nombre + " termina de usar el vehiculo " + vehiculoAsignado);

        } catch (InterruptedException e) {
            // Si el hilo es interrumpido, restauramos el estado de interrupción.
            Thread.currentThread().interrupt();
            System.out.println(nombre + " fue interrumpido y no pudo completar la prueba.");

        } finally {
            // Si se llegó a asignar un vehículo, hay que devolverlo siempre.
            if (vehiculoAsignado != -1) {
                concesionario.devolverVehiculo(vehiculoAsignado);
            }
        }
    }
}
