/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 
package trabalho02.erisnaldo;
*/
/**
 * Classe responsável por realizar a árvore (VeB) com os principais métodos de atualização do resumo e do valor mínimo, 
 * que corresponde ao menor elemento armazenado (mantido fora dos clusters para eficiência), 
 * além das buscas do SUCESSOR e PREDECESSOR.
 * 
 * @author Erisnaldo Machado
 * @version 1.0
 * .
 */
import java.util.*;
import java.io.*;

public class ArvoreVeB32 {
    private final int W;
    public long min = -1, max = -1;
    private ArvoreVeB32 resumo;
    private ClusterMap clusters;
    

    public ArvoreVeB32(int bits) {
        this.W = bits;
        if (bits > 1) { // Base da recursão: 1 bit
            this.resumo = new ArvoreVeB32(bits / 2);
            this.clusters = new ClusterMap();
        }
    }

    // Funções de bits para determinar cluster (high) e índice (low) 
    private long high(long x) { return x >>> (W / 2); }
    private long low(long x) { return x & ((1L << (W / 2)) - 1); }
    private long index(long c, long i) { return (c << (W / 2)) | i; }
    

    public void insert(long x) {
        if (contem(x)) { return; } //Teste para verificar se o valor existe;
        if (min == -1) {
            min = max = x;
            return;
        }
        if (x < min) { long t = x; x = min; min = t; } // Troca: min não vai para clusters
        if (W > 1) {
            long c = high(x), i = low(x);
            if (clusters.get(c) == null) {
                clusters.put(c, new ArvoreVeB32(W / 2));
                resumo.insert(c);
            }
            clusters.get(c).insert(i);
        }
        if (x > max) max = x;
    }
    
    //Metodo de controle da remoção de um valor que não existe.
    
     public boolean contem(long x) {
    // 1. Se a estrutura estiver vazia, o valor não existe 
    if (min == -1) return false;

    // 2. Verifica se o valor é o mínimo ou o máximo do nível atual 
    // O 'min' é armazenado de forma separada dos clusters 
    if (x == min || x == max) return true;

    // 3. Se chegamos na base da recursão (W=1) e não é min/max, não existe
    if (W == 1) return false;

    // 4. Busca recursiva no cluster correspondente 
    long c = high(x);
    long i = low(x);
    ArvoreVeB32 clusterC = clusters.get(c);

    // Se o cluster não existir no mapa de dispersão, o valor não está na árvore 
    if (clusterC == null) return false;

    return clusterC.contem(i);
}   
    
    public void delete(long x) {
        if (!contem(x)) { return; } //Teste para verificar se o valor existe;
        if (min == max) { min = max = -1; return; }
        if (W == 1) { min = max = (x == 0 ? 1 : 0); return; }
        if (x == min) {
            long firstC = resumo.min;
            x = index(firstC, clusters.get(firstC).min);
            min = x; // Novo mínimo promovido 
        }
        long c = high(x), i = low(x);
        ArvoreVeB32 clusterC = clusters.get(c);
        clusterC.delete(i);
        if (clusterC.min == -1) {
            resumo.delete(c);
            clusters.remove(c); // Halving automático no ClusterMap
            if (x == max) {
                if (resumo.max == -1) max = min;
                else max = index(resumo.max, clusters.get(resumo.max).max);
            }
        } else if (x == max) {
            max = index(c, clusterC.max);
        }
    }

    public long successor(long x) {
        if (W == 1) return (x == 0 && max == 1) ? 1 : -1;
        if (min != -1 && x < min) return min;
        long c = high(x), i = low(x);
        ArvoreVeB32 cl = clusters.get(c);
        if (cl != null && i < cl.max) return index(c, cl.successor(i));
        long nextC = resumo.successor(c);
        return (nextC == -1) ? -1 : index(nextC, clusters.get(nextC).min);
    }

    public long predecessor(long x) {
        if (W == 1) return (x == 1 && min == 0) ? 0 : -1;
        if (max != -1 && x > max) return max;
        long c = high(x), i = low(x);
        ArvoreVeB32 cl = clusters.get(c);
        if (cl != null && i > cl.min) return index(c, cl.predecessor(i));
        long prevC = resumo.predecessor(c);
        if (prevC == -1) return (min != -1 && x > min) ? min : -1;
        return index(prevC, clusters.get(prevC).max);
    }

    public void printIMP() {
    // 1. Verifica se a árvore está vazia
    if (min == -1) {
        System.out.println("IMP Estrutura Vazia");
        return;
    }

    // 2. Inicia a string com o mínimo global da estrutura 
    StringBuilder sb = new StringBuilder("IMP\nMin: " + min);

    // 3. Processa os clusters se não estivermos no nível base (W > 1) 
    if (W > 1) {
        // Obtém as chaves dos clusters ativos da tabela de dispersão e as ordena
        List<Long> clusterKeys = clusters.getKeys();
        Collections.sort(clusterKeys);

        for (long cKey : clusterKeys) {
            // Formata o identificador do cluster conforme solicitado: C[indice]:
            sb.append(", C[").append(cKey).append("]: ");

            ArvoreVeB32 clNode = clusters.get(cKey);
            List<Long> localElements = new ArrayList<>();

            // 4. Coleta os elementos internos. Usamos offset 0 para reconstruir
            // os valores LOCAIS ao universo de 16 bits desse cluster.
            collectElements(clNode, 0, localElements);

            // 5. Adiciona os valores locais separados por vírgula
            for (int i = 0; i < localElements.size(); i++) {
                sb.append(localElements.get(i));
                if (i < localElements.size() - 1) {
                    sb.append(", ");
                }
            }
        }
    }
    
    // Imprime o resultado final em uma única linha no terminal
    System.out.println(sb.toString());
}
/**
 * Função auxiliar para coletar as posições locais de forma recursiva.
 * Ao iniciar com offset 0, ela reconstrói o índice 'i' relativo ao cluster pai .
 */
private void collectElements(ArvoreVeB32 node, long offset, List<Long> list) {
    if (node.min == -1) return;

    // Adiciona o valor reconstruído localmente (posição no vetor do cluster).
    list.add(offset | node.min);
    
    if (node.W > 1) {
        List<Long> sorted = new ArrayList<>(node.clusters.getKeys());
        Collections.sort(sorted);
        for (long c : sorted) {
            // Realiza o deslocamento de bits (shift) para compor a posição local 
            // nos níveis inferiores da recursão.
            collectElements(node.clusters.get(c), offset | (c << (node.W / 2)), list);
        }
    }
}
    
    
}
