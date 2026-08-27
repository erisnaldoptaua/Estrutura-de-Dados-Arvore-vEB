/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 
package trabalho02.erisnaldo;
 */
/**
 * Classe de controle de colisão nos clusters espalhados, vamos inicar com uma tamanho pequeno = 3;
 * 
 * @author Erisnaldo Machado
 * @version 1.0
 * 
 */
class EntradaCluster {
    long key;
    ArvoreVeB32 value;
    EntradaCluster next;
    
        public EntradaCluster (long k, ArvoreVeB32 v, EntradaCluster n) {
            this.key = k;
            this.value = v;
            this.next = n;
        }
     
    
}

