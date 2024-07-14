# L - Systems

![arboles](https://github.com/user-attachments/assets/3505656c-edda-4db1-a995-e772cbac7b17)


Programa en **Clojure** utilizando Leiningen que permite generar imágenes fractales, mediante un algoritmo basado en **sistemas-L**, una simulación de **gráficos tortuga** y el formato de imágenes estándar **SVG**. 

### Gráficos tortuga:

**Gráficos tortuga** es un método para generar imágenes usando un cursor (la tortuga) relativo a unas coordenadas cartesianas.

La tortuga tiene tres atributos:

- Una **posición** en el plano (que puede expresado en coordenadas cartesianas con dos números reales (x, y)). La tortuga comienza en la posición (0, 0).
- Una **orientación** (expresada como un ángulo).
- Una **pluma**, teniendo atributos como color, ancho y un indicador de pluma arriba y abajo. La tortuga comienza con la pluma abajo.

### Sistemas-L:

Un **sistema-L** o un sistema de **Lindenmayer** es un conjunto de símbolos y reglas que tiene una naturaleza recursiva y que permite, entre otras cosas, generar imágenes fractales.

Un Sistema-L está formado por:

- Un **alfabeto**: el conjunto de todos los símbolos válidos.
- Un **axioma**: Una cadena de símbolos del alfabeto que define el estado inicial del sistema.
- Un **conjunto** de reglas de transformación: Cada regla de transformación consiste de una cadena predecesora y una cadena sucesora.


### El Programa:
Para ejecutar el programa utilizando Leiningen se deben recibir tres parámetros por la línea de comandos (que se recibirán como parámetros en la función -main), de la forma:

1. El nombre del archivo que contiene la descripción del sistema-L
2. La cantidad de iteraciones a procesar
3.  El nombre del archivo SVG a escribir

Por ejemplo:

```sh
$ lein run arbol.sl 3 arbol.svg
```
