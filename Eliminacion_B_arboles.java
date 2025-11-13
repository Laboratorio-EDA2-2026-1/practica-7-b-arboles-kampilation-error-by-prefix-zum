//Implementa aquí la operación de eliminación. 
//Pueden modificar la extensión del documento para que se ajuste al lenguaje de su elección y comentar estas instrucciones.
//NOTA: Dado que las instrucciones no piden realizar todo el código, solo haremos lo necesario para que los métodos funcionen.
public class Eliminacion_B_Arboles extends ArbolB {
    //Le ponemos un constructor.
    public Eliminacion_B_Arboles(int orden) {
        super(orden);
    }

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
        hijo.llaves.add(orden - 1, m.llaves.get(indice));
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
        //Nota: Desconocemos como realizar la implementación de este método.
    }

    private void prestamoDerecho(Nodo m, int indice) {
        // Implementar pedir prestado del hermano derecho
    }
}
