//Implementa aquí la operación de búsqueda. 
//Pueden modificar la extensión del documento para que se ajuste al lenguaje de su elección y comentar estas instrucciones.
//NOTA: Dado que las instrucciones no piden realizar todo el código, solo haremos lo necesario para que los métodos funcionen


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

public class Busqueda_B_Arboles{
    public int busquedaEnArbolB(Nodo x, int llave){
        int i;
        i = 0; //Consideramos mas apropiado empezar el contador en cero y no en uno.
        while (i < x.n && llave > x.llaves.get(i)){ //Por ende, las condiciones del while tambien cambian a no considerar la igualdad
            i++; //Iteramos todo el ciclo mientras ambas condiciones se cumplan, lo que seria similar
                 //A buscar entre los indices del nodo 'x' hasta que se acaben, o encontremos uno mayor o igual.
        }

        if (i < x.n && llave == x.llaves.get(i)){
            return i; //Si encontramos a k dentro del nodo x, retornamos el indice
        }

        if(x.hoja){
            bandera();
            return -1; //Colocamos una bandera, el nodo actual es hoja, entonces no tiene hijos y no es posible buscar hacia abajo
        }

        else{
            return busquedaEnArbolB(x.lista_hijos.get(i), llave); //En caso contrario, buscamos en el hijo de manera recursiva, siempre y cuando no sea hoja.
        }
    }

    public static void bandera(){
        System.out.println("El valor buscado no fue encontrado en el árbol B. Lo sentimos.");
    }
}
