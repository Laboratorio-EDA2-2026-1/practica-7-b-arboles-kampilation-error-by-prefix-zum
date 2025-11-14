import java.util.ArrayList;

public class ArbolB {
    //Implementa aquí la operación de búsqueda. 
    //Pueden modificar la extensión del documento para que se ajuste al lenguaje de su elección y comentar estas instrucciones.

    Nodo raiz;
    int orden;

    // Constructor de ArbolB
    public ArbolB(int orden) {
        this.orden = orden;
        this.raiz = new Nodo(orden, true); // La raíz empieza como hoja
    }

    class Nodo {
        int n;
        int orden;
        ArrayList<Integer> llaves;
        ArrayList<Nodo> lista_hijos;
        boolean hoja;

        // Constructor de Nodo
        public Nodo(int orden, boolean hoja) {
            this.orden = orden;
            this.hoja = hoja;
            this.n = 0;
            this.llaves = new ArrayList<>();
            this.lista_hijos = new ArrayList<>();
        }
    }

    //======== BUSQUEDA ========

    public class ResultadoBusqueda {
        Nodo nodo;
        int indice;

        public ResultadoBusqueda(Nodo n, int i) {
            this.nodo = n;
            this.indice = i;
        }
    }

    public ResultadoBusqueda buscar(int llave) {
        return buscarRec(raiz, llave);
    }

    private ResultadoBusqueda buscarRec(Nodo x, int llave){
        int i = 0;
        while(i < x.n && llave > x.llaves.get(i)) i++;

        if (i < x.n && llave == x.llaves.get(i)) {
            return new ResultadoBusqueda(x, i); //Si encontramos a k dentro del nodo x, retornamos el nodo y el indice
        }

        if(x.hoja){
            return null; //El nodo actual es hoja y no tiene hijos. No se puede buscar hacia abajo
        }

        return buscarRec(x.lista_hijos.get(i), llave);
    }


    //======== INSERCION ========

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
        Nodo aux2 = new Nodo(m.orden, hijo.hoja);
        aux2.n = orden - 1;

        for(int j = 0; j < orden - 1; j++) {
            aux2.llaves.add(hijo.llaves.remove(orden)); // Movimiento de separar las llaves
        }

        if(!hijo.hoja) {
            for(int j = 0; j < orden; j++){
                aux2.lista_hijos.add(hijo.lista_hijos.remove(orden));
            }
        }

        int llaveMed = hijo.llaves.remove(orden-1);

        // Hacemos las conexiones correspondientes después de la división
        hijo.n = orden - 1;
        m.llaves.add(k, llaveMed);
        m.lista_hijos.add(k+1, aux2);
        m.n++; // Aumentamos el numero de llaves
    }

    public void insertarArbolBNoLleno(Nodo x, int key) {
        // Este método sirve para insertar una llave en un árbol que aún tiene espacio en una hoja
        int p = x.n - 1;

        if(x.hoja){
            int i = x.n - 1;
            while (i >= 0 && key < x.llaves.get(i)) {
                i--;
            }

            x.llaves.add(i + 1, key);
            x.n++;
        }
        else{
            while (p >= 0 && key < x.llaves.get(p)) {
                p--;
            }
            p++; // A partir de acá, ya estamos en la posición donde se debe insertar

            Nodo aux3 = x.lista_hijos.get(p);
            if (aux3.n == (2 * orden) - 1) {
                // Si el hijo está lleno, se divide
                divisionHijoArbolB(x, aux3, p);

                if (key > x.llaves.get(p)) {
                    p++;
                }
            }
            insertarArbolBNoLleno(x.lista_hijos.get(p), key);
        }
    }

    //======== ELIMINACION ========

    public void eliminar(int llave) {
        eliminarRec(raiz, llave);
        // Si la raíz quedó sin llaves y tiene hijos, la promovemos
        if (raiz.n == 0 && !raiz.hoja) {
            raiz = raiz.lista_hijos.get(0);
        }
    }

    private void eliminarRec(Nodo nodo, int llave) {
        int indice = 0;
        // Buscamos la posición de la llave
        while (indice < nodo.n && nodo.llaves.get(indice) < llave) {
            indice++;
        }
        //Vale la pena recordar que hay 3 casos para la eliminacion,
        //asi que debemos estar pendientes a cada uno de llos y tratarlos 'casi' individualmente.
        // Caso 1. La llave está en este nodo, y podemos sacarla de un solo paso
        if (indice < nodo.n && nodo.llaves.get(indice) == llave) {
            if (nodo.hoja) {
                // Eliminación directa en hoja
                nodo.llaves.remove(indice);
                nodo.n--;
            } else {
                // Nodo interno: reemplazar con predecesor o sucesor
                Nodo hijoIzq = nodo.lista_hijos.get(indice);
                Nodo hijoDer = nodo.lista_hijos.get(indice + 1);

                if (hijoIzq.n >= orden) {
                    int pred = getPredecesor(hijoIzq);
                    nodo.llaves.set(indice, pred);
                    eliminarRec(hijoIzq, pred);
                } else if (hijoDer.n >= orden) {
                    int succ = getSucesor(hijoDer);
                    nodo.llaves.set(indice, succ);
                    eliminarRec(hijoDer, succ);
                } else {
                    // Fusionar hijos y eliminar recursivamente
                    fusionar(nodo, indice);
                    eliminarRec(hijoIzq, llave);
                }
            }
        } else {
            // Caso 2. La llave está en un hijo del nodo en que estamos parados
            if (nodo.hoja) {
                // No se encontró. No hay nada para hacer aqui.
                return;
            }

            Nodo hijo = nodo.lista_hijos.get(indice);
            if (hijo.n < orden) {
                // Asegurar que tenga al menos orden llaves

                //Caso 3. Al eliminar, tenemos un hijo con menos llaves que las esperadas
                if (indice != 0 && nodo.lista_hijos.get(indice - 1).n >= orden) {
                    // Pedir prestado del hermano izquierdo
                    prestamoIzquierdo(nodo, indice);
                } else if (indice != nodo.n && nodo.lista_hijos.get(indice + 1).n >= orden) {
                    // Pedir prestado del hermano derecho
                    prestamoDerecho(nodo, indice);
                } else {
                    // Fusionar con un hermano
                    if (indice != nodo.n) {
                        fusionar(nodo, indice);
                    } else {
                        fusionar(nodo, indice - 1);
                        hijo = nodo.lista_hijos.get(indice - 1);
                    }
                }
            }
            eliminarRec(hijo, llave);
        }
    }

    //Usando encapsulamiento, obtenemos los predecesores y sucesores de los nodos
    //mediante getters y setters.
    private int getPredecesor(Nodo nodo) {
        while (!nodo.hoja) nodo = nodo.lista_hijos.get(nodo.n);
        return nodo.llaves.get(nodo.n - 1);
    }

    private int getSucesor(Nodo nodo) {
        while (!nodo.hoja) nodo = nodo.lista_hijos.get(0);
        return nodo.llaves.get(0);
    }

    //Manteniendo el metodo de acceso privado, realizamos los ajustes necesarios al Arbol B,
    //segun vayamos necesitando mover las llaves de los nodos.
    private void fusionar(Nodo m, int indice) {
        Nodo hijo = m.lista_hijos.get(indice);
        Nodo hermano = m.lista_hijos.get(indice + 1);

        // Insertar la llave del m (padre)
        hijo.llaves.add(m.llaves.get(indice));
        // Mover llaves del hermano
        hijo.llaves.addAll(hermano.llaves);
        if (!hermano.hoja) {
            hijo.lista_hijos.addAll(hermano.lista_hijos);
        }
        hijo.n += hermano.n + 1;

        // Ajustar m (padre)
        m.llaves.remove(indice);
        m.lista_hijos.remove(indice + 1);
        m.n--;
    }

    private void prestamoIzquierdo(Nodo m, int indice) {
        // Implementar pedir prestado del hermano izquierdo del nodo.
        Nodo hijo = m.lista_hijos.get(indice);
        Nodo hermanoIzq = m.lista_hijos.get(indice - 1);

        hijo.llaves.add(0, m.llaves.get(indice - 1));

        m.llaves.set(indice - 1, hermanoIzq.llaves.remove(hermanoIzq.n - 1));

        if (!hermanoIzq.hoja) {
            hijo.lista_hijos.add(0, hermanoIzq.lista_hijos.remove(hermanoIzq.n));
        }

        hijo.n++;
        hermanoIzq.n--;
    }

    private void prestamoDerecho(Nodo m, int indice) {
        // Implementar pedir prestado del hermano derecho
        Nodo hijo = m.lista_hijos.get(indice);
        Nodo hermanoDer = m.lista_hijos.get(indice + 1);

        hijo.llaves.add(m.llaves.get(indice));

        m.llaves.set(indice, hermanoDer.llaves.remove(0));

        if (!hermanoDer.hoja) {
            hijo.lista_hijos.add(hermanoDer.lista_hijos.remove(0));
        }

        hijo.n++;
        hermanoDer.n--;
    }

    //Metodo main
    public static void main(String[] args) {
        // Creamos el árbol B con orden 3 (cada nodo puede tener máximo 5 llaves)
        ArbolB arbol = new ArbolB(3);

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
