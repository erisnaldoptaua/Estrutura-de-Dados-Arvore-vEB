/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 
package trabalho02.erisnaldo;
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;

/**
 * 
 * Classe principal pela leitura do arquivo e execução.
 * 
 * @author Erisnaldo Machado
 * @version 1.0
 * 
 * 
 */
public class Main {
    public static void main(String[] args) {
       if (args.length < 1) {
       	System.err.println("Erro: Precisa informar o arquivo na chamada da classe principal. EX: java Main entrada.txt");
       	return;
           }
        ArvoreVeB32 veb = new ArvoreVeB32(32);
        String filename = args[0];
       // String filename = "entrada.txt";
        try (Scanner sc = new Scanner(new File(filename))) {
            while (sc.hasNextLine()) {
                String Linha = sc.nextLine().trim();
                if (Linha.isEmpty()) continue;
                String[] parts = Linha.split("\\s");
                String op = parts[0].toUpperCase();
                switch (op) {
                    case "INC": 
                        veb.insert(Long.parseLong(parts[1])); 
                        break;
                    case "REM":
                        veb.delete(Long.parseLong(parts[1])); 
                        break;
                    case "SUC":
                        long s = veb.successor(Long.parseLong((parts[1])));
                        System.out.println("SUC " + parts[1]);
                        if (s == -1) {
                                System.out.println("+INF");
                            } else { System.out.println(s);}
                        break;
                    case "PRE":
                        long p = veb.predecessor(Long.parseLong(parts[1]));
                        System.out.println("PRE " + parts[1]);
                        if (p == -1) {
                            System.out.println("+INF");
                            }else {System.out.println(p);}
                        break;
                    case "IMP": 
                        veb.printIMP(); 
                        break;
                        
                    default:
                        System.err.println("Operação desconhecida: " + op);
                }
            }
        } 
        catch (FileNotFoundException e) 
            { 
                System.err.println("Arquivo não encontrado."); 
            }
   }
    
}

