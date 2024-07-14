(ns algo3-tp2-2024c1.core
  (:gen-class)
  (:require [clojure.string :as str]))


(defn crear-tortuga
  "Crea un hash-map que representa una tortuga y sus atributos"
  []
  {:posicionX (int 0) :posicionY (int 0) :angulo (int 270) :pluma true})

(defn mover-tortuga [tortuga distancia]
  (let [angulo-radianes (Math/toRadians (:angulo tortuga))
        nueva-posicionX (+ (:posicionX tortuga) (* distancia (Math/cos angulo-radianes)))
        nueva-posicionY (+ (:posicionY tortuga) (* distancia (Math/sin angulo-radianes)))]
    (assoc tortuga :posicionX nueva-posicionX :posicionY nueva-posicionY)))


(defn derecha
  "Gira según el angulo a la derecha"
  [tortuga angulo]
  (assoc tortuga :angulo (+ (:angulo tortuga) angulo)))

(defn izquierda
  "Gira según el angulo a la derecha"
  [tortuga angulo]
  (assoc tortuga :angulo (- (:angulo tortuga) angulo)))

(defn invertir-direccion [tortuga]
  (let [angulo-invertido (+ (:angulo tortuga) 180)]
    (assoc tortuga :angulo angulo-invertido)))

(defn pluma-arriba
  "Asigna el estado de la pluma a arriba"
  [tortuga]
  (assoc tortuga :pluma false))

(defn apilar-tortuga
  "Se apila una nueva tortuga"
  [pila-tortugas tortuga]
  (conj pila-tortugas tortuga))

(defn desapilar-tortuga
  "Desapila una tortuga"
  [pila-tortugas]
  (rest pila-tortugas))

(defn extraer-datos
  "Recibe la ruta al archivo ingresado, utiliza 'slurp' para abir y leer el archivo completo y devuelve un string con todo el contenido del archivo,
  luego divide el string en una lista de strings donde cada string contiene cada linea del archivo. Se asigna la primer linea al angulo, la segunda al axioma y
  las lineas que sobran a las reglas (el drop 2 lo que hace es ignorar las primeras dos lineas).
  Para finalizar crea el hash de reglas dividiendo las lineas de las reglas por un espacio (ya que conocemos el formato del archivo) y la primer parte se la asigna a la clave de la regla y
  la segunda parte al valor de la regla. Devuelve un vector con el angulo, el axioma y el hash de reglas."
  [ruta-archivo]
  (let [lineas (str/split-lines (slurp ruta-archivo))
        angulo (Double/parseDouble (first lineas))
        axioma (second lineas)
        reglas (drop 2 lineas)
        hash-reglas (reduce (fn [acc linea]
                              (let [linea-spliteada (str/split linea #" ")
                                    clave-regla (first linea-spliteada)
                                    valor-regla (second linea-spliteada)]
                                (assoc acc clave-regla valor-regla)))
                            {}
                            reglas)]
    [angulo axioma hash-reglas]))



(defn transformar-axioma
  "Recibe el axioma con sus reglas y la cantidad de iteraciones. Mientras que las iteraciones no sean 0, si los caracteres de la cadena actual tienen una regla asociada en el hash,
   se reemplaza por ese valor y decrementa el contador de las iteraciones en 1. Cuando llega a 0, devuelve la cadena transformada."
  [axioma reglas iteraciones]
  (let [iteraciones (Integer/parseInt iteraciones)]
  (loop [cadena-actual axioma i iteraciones]
    (if (= i 0)
      cadena-actual
      (recur (apply str (map #(get reglas (str %) (str %)) cadena-actual)) (dec i))))))

(defn caso-desapilar
  "Si se pide desapilar una tortuga, se llama a esta función auxiliar"
  [pila-tortugas lineas]
  (let [nueva-pila (desapilar-tortuga pila-tortugas)
        tope-nueva-pila (first nueva-pila)]
    [tope-nueva-pila nueva-pila lineas]))

(defn caso-apilar
  "Si se pide desapilar una tortuga, se llama a esta función auxiliar"
  [tortuga pila lineas]
  (let [pila-nueva (apilar-tortuga pila tortuga)
        tortuga-nueva (first pila-nueva)]
    [tortuga-nueva pila-nueva lineas]))

(defn actualizar-tope
  "Actualizamos los valores que tiene la tortuga al tope de la pila para mantener coherencia"
  [pila tortuga]
  (let [pila-desapilada (rest pila)
    pila-apilada (apilar-tortuga pila-desapilada tortuga)]
  pila-apilada))


(defn procesar-caracter
  "Procesa un caracter y segun sea actualiza el estado de la tortuga, de la pila y
  agrega nueva lineas a la lista de lineas si corresponde"
  [char tortuga pila-tortugas lineas angulo]
  (cond
    (or (= char \F) (= char \G))
    (let [tortuga-movida (mover-tortuga tortuga 1)]
      (if (:pluma tortuga)
        [tortuga-movida (actualizar-tope pila-tortugas tortuga-movida) (conj lineas {:x1 (:posicionX tortuga) :y1 (:posicionY tortuga) :x2 (:posicionX tortuga-movida) :y2 (:posicionY tortuga-movida)})]
        [tortuga-movida pila-tortugas lineas]))

    (or (= char \f) (= char \g))
    (let [tortuga (pluma-arriba (mover-tortuga tortuga 1))]
      [tortuga (actualizar-tope pila-tortugas tortuga) lineas])

    (= char \+)
    (let [tortuga (derecha tortuga angulo)]
      [tortuga (actualizar-tope pila-tortugas tortuga) lineas])

    (= char \-)
    (let [tortuga (izquierda tortuga angulo)]
      [tortuga (actualizar-tope pila-tortugas tortuga) lineas])

    (= char \|)
    (let [tortuga (invertir-direccion tortuga)]
      [tortuga (actualizar-tope pila-tortugas tortuga) lineas])

    (= char \[)
    (caso-apilar tortuga pila-tortugas lineas)

    (= char \])
    (caso-desapilar pila-tortugas lineas)

    :else
    [tortuga pila-tortugas lineas]))



(defn procesar-cadena
  "Procesa la cadena transformada caracter por caracter y actualiza el estado de la tortuga, de la pila y
   de la lista de lineas segun corresponda."
  [cadena-transformada angulo]
  (let [tortuga-inicial (crear-tortuga)]
    (loop [cadena cadena-transformada
           pila-tortugas (apilar-tortuga [] tortuga-inicial)
           tortuga tortuga-inicial
           lineas []]
      (if (empty? cadena)
        lineas
        (let [char (first cadena)
              caracteres-restantes (rest cadena)
              [tortuga-actualizada pila-actualizada lineas-actualizadas] (procesar-caracter char tortuga pila-tortugas lineas angulo)]
          (recur caracteres-restantes
                 pila-actualizada
                 tortuga-actualizada
                 lineas-actualizadas))))))


(defn calcular-viewbox
  "Calcula los parámetros del viewBox: x-min, y-min, ancho y alto, añadiendo un margen fijo."
  [lineas]
  (let [margen 10
        valores-x (mapcat #(vector (:x1 %) (:x2 %)) lineas)
        valores-y (mapcat #(vector (:y1 %) (:y2 %)) lineas)
        x-min (- (apply min valores-x) margen)
        x-max (+ (apply max valores-x) margen)
        y-min (- (apply min valores-y) margen)
        y-max (+ (apply max valores-y) margen)
        ancho (- x-max x-min)
        alto (- y-max y-min)]
    [x-min y-min ancho alto]))


(defn crear-svg [lineas]
  (let [[x-min y-min ancho alto] (calcular-viewbox lineas)
        primera-linea (str "<svg viewBox=\"" x-min " " y-min " " ancho " " alto "\" xmlns='http://www.w3.org/2000/svg' >\n")
        ultima-linea "</svg>"
        lineas-str (apply str (map #(str "<line x1='" (:x1 %) "' y1='" (:y1 %) "' x2='" (:x2 %) "' y2='" (:y2 %) "' stroke='black' stroke-width='0.1' />\n")
                                  lineas))]
    (str primera-linea lineas-str ultima-linea)))


(defn -main [ruta-archivo iterations archivo-salida]
  (let [[angulo axioma reglas] (extraer-datos ruta-archivo)
        cadena-transformada (transformar-axioma axioma reglas iterations)
        lineas (procesar-cadena cadena-transformada angulo)
        svg (crear-svg lineas)]
    (spit archivo-salida svg)))