//Implementa aquí la operación de inserción. 
//Pueden modificar la extensión del documento para que se ajuste al lenguaje de su elección y comentar estas instrucciones.
//NOTA: Dado que las instrucciones no piden realizar todo el código, solo haremos lo necesario para que los métodos funcionen.
public class Insercion_B_Arboles extends ArbolB{
    //Le metemos el constructor
    public Insercion_B_Arboles(int orden){
        super(orden);
    }

    public void insercionBArbol(int llave) {
        if (raiz.n == 0) { // Para esto sirve haber colocado a n como atributo
            raiz.llaves.add(llave);
            raiz.n++; // Aumentamos en uno el valor de n.
        } else {
            if (raiz.n == (2 * orden) - 1) { // Propiedad del grado de ramificación
                // Si caemos en este caso, significa que no hay donde reasignar el nodo en la raíz,
                // tendremos que abrir un espacio propio para alojar a este nuevo nodo.
                Nodo aux = new Nodo(orden, false);

                // Para este caso ya no hay más espacios en la raíz, hay que dividirla
                aux.lista_hijos.add(raiz);
                divisionHijoArbolB(aux, raiz, 0); // Aquí el hijo es la raíz y el padre m es el nuevo nodo

                int q = 0;
                if (aux.llaves.get(q) < llave) {
                    q++;
                }
                insertarArbolBNoLleno(aux, llave); // Cuando el árbol B no está lleno, es más fácil
            } else {
                insertarArbolBNoLleno(raiz, llave);
            }
        }
    }

    // Métodos complementarios para la inserción:
    public void divisionHijoArbolB(Nodo m, Nodo hijo, int k) {
        // Implementamos la división cuando ya no hay espacio en la raíz
        Nodo aux2 = new Nodo(m.orden, m.hoja);
        aux2.n = orden - 1;

        for (int j = 0; j < aux2.n; j++) {
            aux2.llaves.add(hijo.llaves.remove(orden + 1)); // Movimiento de separar las llaves
        }

        if (!hijo.hoja) {
            for (int j = 0; j < orden; j++) {
                aux2.lista_hijos.add(hijo.lista_hijos.remove(orden + 1));
            }
        }

        // Hacemos las conexiones correspondientes después de la división
        m.llaves.add(k, hijo.llaves.remove(orden)); // Insertamos la llave del medio en el nodo padre
        m.lista_hijos.add(k + 1, aux2);
        m.n++; // Aumentamos el número de llaves
    }

    public void insertarArbolBNoLleno(Nodo x, int key) {
        // Este método sirve para insertar una llave en un árbol que aún tiene espacio en una hoja
        int p = x.n - 1;

        if (x.hoja) {
            x.llaves.add(0);
            while (p >= 0 && key < x.llaves.get(p)) {
                if (p + 1 < x.llaves.size()) {
                    x.llaves.set(p + 1, x.llaves.get(p));
                }
                p--;
            }
            x.llaves.set(p + 1, key);
            x.n++;
        } else {
            while (p >= 0 && key < x.llaves.get(p)) {
                p--;
            }
            p++; // A partir de acá, ya estamos en la posición donde se debe insertar

            Nodo aux3 = x.lista_hijos.get(p);
            if (aux3.n == (2 * orden) - 1) {
                // Si el hijo está lleno, se divide
                divisionHijoArbolB(x, aux3, key);

                if (key > x.llaves.get(p)) {
                    p++;
                }
            }
            insertarArbolBNoLleno(x.lista_hijos.get(p), key);
        }
    }


//Metodo main
    public static void main(String[] args) {
        // Creamos el árbol B con orden 3 (cada nodo puede tener máximo 5 llaves)
        Insercion_B_Arboles arbol = new Insercion_B_Arboles(3);

        //Habria que insertar varios valores para que funcione el arbol.
        int[] arreglito = {8, 9, 10, 11, 15, 20, 17};
        for (int v : arreglito) {
            arbol.insercionBArbol(v);
        }

        // Imprimimos las llaves de la raíz
        System.out.println("Llaves de la raíz después de inserciones:");
        for (int k : arbol.raiz.llaves) {
            System.out.print(k + " ");
        }
        System.out.println();

        // Insertamos más valores para probar divisiones de nodos
        int[] masValores = {5, 6, 12, 30, 7};
        for (int v : masValores) {
            arbol.insercionBArbol(v);
        }

        // Imprimimos nuevamente las llaves de la raíz
        System.out.println("Llaves de la raíz después de más inserciones:");
        for (int k : arbol.raiz.llaves) {
            System.out.print(k + " ");
        }
        System.out.println();
    }
}


