
=====================================================
    ÁRVORE VAN EMDE BOAS COM TABELA DE DISPERSÃO  
========================================================

Implementação em Java (openjdk 21.0.10) de uma árvore Van emde Boas (VeB) que utiliza Tabela de Dispersão Dinâmica com Table Doubling (ClusterMAP). Garantindo espaço linear O(n) ao armazenar apenas clusters Ativo( Não vazio). A classe ClusterMap, implementa Table Doubling (dobrando a capacidade quando o fator de carga atinge 0.75) e Halving (reduzindo a capacidade quando cai para 0.25). Assim, não vamos utilizar um tamanho de array fixo de 2^16 para um Universo = 32 bits. A cada inclusão e remoção é atualizado o resumo e o valor mínimo mantido fora dos clusters para eficiência.




ESTRUTURA DO PROJETO
--------------------
- ClusterMAP.java          -> Classe responsável por implementar os ajustes no Table Doubling, dobrando a capacidade quando o fator de carga atinge 0.75 e Halving reduzindo a capacidade pela metade quando cai para 0.25 (CONTROLE_CARGA = 0.75)
- EntradaCluster.java	   -> Classe de controle de colisão nos clusters espalhados, vamos inicar com uma tamanho pequeno = 3;
- Main.java                -> Classe principal com leitura do arquivo e execução.
- ArvoreVeB32.java         -> Classe responsável por realizar a árvore (VeB) com os principais métodos de atualização do resumo e do valor mínimo, que corresponde ao menor elemento armazenado 
			       (mantido fora dos clusters para eficiência), além das buscas do SUCESSOR e PREDECESSOR. 
- Makefile                 -> Automação de compilação e execução.
- README.md                -> Documentação do projeto.

DESCRIÇÃO DOS MÉTODOS E DAS VARIAVEIS DAS CLASSES
-------------------------------
1. ClusterMAP (Table Doubling);
   - CONTROLE_CARGA : Controle de carga (0.75);
   - ClusterMAP(): Estanciando o tamanho inicial de um vetor pequeno = 3;
   - hash(long key): Controle da inserção dos valores no vetor;
   - put (long key, ArvoreVeB32 value) Duplicar o tamanho do vetor de cluster;
   - remove(long key) Remove a chave reduz o tamanho da tabela;
   - resize(int newCap) redimensiona a tabela de dispersão, criando uma nova tabela com uma capacidade diferente e reinserindo todos os elementos nela;
    

2. EntradaCluster (long k, ArvoreVeB32 v, EntradaCluster n);
   - Long key    	   : Vamos utilizar essa variavel para armazenar o índice original do cluster, ajudando a identificar exatamente qual cluster está sendo acessado, mesmo quando vários caem 
                            na mesma posição da tabela. (Colisão)
   - ArvoreVeB32 value    : É o valor propriamente dido que queremos armazenar.
   - EntradaCluster next  : Controle de colisão, é uma referencia para o próximo elemento da lista, permitindo continuar a busca pelos pares de chave-valor que compartilham a mesma posição.

3. ArvoreVeB32
   
    - int W						 : valor imutável do tamanho do Universo, vamos deixar esse fixo em (32) só no momento de instaciar a classe ArvoreVeB32 na classe principal.
    - long min = -1, max = -1				 : Iniciando o menor valor e maior valor fora do cluter  (- 1 Siguinifica que a estrutura está vazia)
    - ArvoreVeB32 resumo				 : Classe Arvore ArvoreVeB32
    - ClusterMap clusters				 : Classe ClusterMap
    - public ArvoreVeB32(int bits)                     : Contrutor da classe iniciando os valores e separando o Universo em dois grupos(Superior: Os 16 bits superiores indicam qual cluster o número
                                                         pertence e os 16 bits inferiores indicam a posição de x dentro desse cluster).
    - insert(long x)                                   : Atualizando o valor do Minimo e do Maximo caso sejá necessario
    - contem(long x)					 : Teste booleano para saber se o valor informado existe na estrutura, caso não exista sai do metodo remoção caso exita sai do metodo inserir.
    - delete(long x)                                   : Removendo o valor do cluster, realizando having e atualizando o novo valor do mínimo e do maximo caso sejá necessário.
    - successor(long x)                                : Primeiro analisaremos o minimo. Se x for maior, busca dentro do cluster high(x). Se não houver sucessor lá, consulta o resumo para achar o
    							   próximo cluster oculpado.
    - predecessor(long x)                              : Primeiro analisaremos o maximo. Se x for menor, busca dentro do cluster high(x). Se não houver sucessor lá, consulta o resumo para achar o 								   próximo cluster oculpado.
    - printIMP()                                       : Imprimindo arvore VeB ordenado com os valores oculpados.
    - collect(ArvoreVeB32 node, long offset, List<Long> list): Metodo criado para organizar a fragmentação do vetor, característica da árvore vEB, que não segue uma sequência linear de preenchimento. 							   Assim, os índices dos elementos armazenados são ordenados pelo menor valor, auxiliando o método de impressão na exibição ordenada dos dados.
   

4. Main
   - Lê arquivo de entrada (passado como argumento)
   - Para cada linha identifica operação (INC, REM, SUC, PRED, IMP)
   - Chama método correspondente da árvore ArvoreVeB32
   - Imprime resultados conforme especificado no arquivo de entrada

COMO EXECUTAR
-------------
1. Compilar:
   - Linux: make ou make build
   - Windows: javac Main.java

2. Executar com arquivo de entrada:
   - Linux: java Main "NOME_DO_ARQUIVO".txt ou make run INPUT="NOME_DO_ARQUIVO".txt (por padrão, se não for passado um arquivo INPUT, o comando make run irá executar o arquivo de entrada "entrada.txt")
   - Windows: java Main "NOME_DO_ARQUIVO".txt

3. Limpar arquivos .class:
   make clean

FORMATO DA ENTRADA
------------------
Cada linha contém uma operação:
   INC <int>           -> insere inteiro e atualiza o valor minimo, máximo e atualiza o tamanho da tabela caso sejá necessário
   REM <int>           -> remove inteiro (se existir) e atualiza o valor minimo, máximo e atualiza o tamanho da tabela caso sejá necessario.
   SUC <int>           -> sucessor de x na estrutura
   PRE <int>           -> predecessor de x na estrutura
   IMP                 -> imprime árvore VeB com os clustes não vazios.

FORMATO DA SAÍDA
----------------
- SUC: SUC <x> 
       <resultado>
- PRE: PRE <x> 
       <resultado>
- IMP: IMP 
       Min: <resultado>, C[<resultado>]: <resultado>, (Todos os cluster não vazios e seus elementos em ordem crecente separdos por espaço e ,).

AUTOR/DESENVOLVEDOR
-----------------------
- Erisnaldo

===========================================================
