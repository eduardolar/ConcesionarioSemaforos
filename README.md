# Concesionario con semáforos

Ejercicio de concurrencia en Java. Simula un concesionario con un número fijo de vehículos de prueba y varios clientes que llegan a la vez, cada uno en su propio hilo.

## Cómo funciona

- Un `Semaphore` justo limita cuántos clientes pueden probar un coche al mismo tiempo: tantos como vehículos.
- Un bloque `synchronized` asigna a cada cliente un vehículo concreto libre y lo libera al terminar.
- Cada cliente (`Cliente`, un `Runnable` que se lanza en su propio hilo) pide un vehículo, lo usa entre medio segundo y dos segundos, y lo devuelve en un `finally`, aunque lo interrumpan.

## Archivos

- `src/concesionario/Concesionario.java`: vehículos, semáforo y asignación.
- `src/concesionario/Cliente.java`: la tarea de cada cliente.
- `src/concesionario/Principal.java`: crea el concesionario y lanza los clientes.

## Cómo ejecutarlo

Con Java 17 o superior:

```bash
javac -d out src/concesionario/*.java
java -cp out concesionario.Principal
```

Hecho durante el ciclo de Desarrollo de Aplicaciones Multiplataforma.
