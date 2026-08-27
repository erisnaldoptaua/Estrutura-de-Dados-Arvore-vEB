/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 
package trabalho02.erisnaldo;
  */
/**
 * Classe responsável Classe responsável por implementar os ajustes no Table Doubling, dobrando a capacidade 
 * quando o fator de carga atinge 0.75 e Halving reduzindo a capacidade pela metade quando cai para 0.25 (CONTROLE_CARGA = 0.75)
 * Tabela de Dispersão Dinâmica com Table Doubling.
 * Garante espaço linear O(n) ao armazenar apenas clusters não vazios.
 * 
 * @author Erisnaldo Machado
 * @version 1.0
 * 
 */
import java.util.*;
import java.io.*;


class ClusterMap {
    private EntradaCluster[] table;
    private int size;
    private static final double CONTROLE_CARGA = 0.75;

    public ClusterMap() {
        this.table = new EntradaCluster[3]; // Tamanho inicial pequeno
        this.size = 0;
    }

    private int hash(long key) { return (int) (key % table.length); }

    public void put(long key, ArvoreVeB32 value) {
        if (size >= table.length * CONTROLE_CARGA) redimencionar(table.length * 2);
        int h = hash(key);
        table[h] = new EntradaCluster(key, value, table[h]);
        size++;
    }

    public ArvoreVeB32 get(long key) {
        int h = hash(key);
        for (EntradaCluster e = table[h]; e != null; e = e.next) {
            if (e.key == key) return e.value;
        }
        return null;
    }

    public void remove(long key) {
        int h = hash(key);
        EntradaCluster prev = null;
        for (EntradaCluster e = table[h]; e != null; e = e.next) {
            if (e.key == key) {
                if (prev == null) table[h] = e.next;
                else prev.next = e.next;
                size--;
                // Table Halving (opcional para economia extrema de memória)
                if (size > 8 && size <= table.length / 4) redimencionar(table.length / 2);
                return;
            }
            prev = e;
        }
    }

    private void redimencionar(int newCap) {
        EntradaCluster[] oldTable = table;
        table = new EntradaCluster[newCap];
        size = 0;
        for (EntradaCluster e : oldTable) {
            while (e != null) {
                put(e.key, e.value);
                e = e.next;
            }
        }
    }

    public List<Long> getKeys() {
        List<Long> keys = new ArrayList<>();
        for (EntradaCluster e : table) {
            for (EntradaCluster curr = e; curr != null; curr = curr.next) keys.add(curr.key);
        }
        // Ordena para facilitar a impressão
        Collections.sort(keys); 
        return keys;
    }
    
}
