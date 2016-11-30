/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package tictactocalogo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Royal
 */
public class  TictacTocAlogo {
    
    
    
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        Game game = new Game();
        
        // game.gofirt();
        Boolean boucle = true;
      //  game.humanClick(0, 1);
        int x;
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("choisir la case entre 1 a 9 :");
        
        try{
             x = Integer.parseInt(br.readLine()) - 1;
             game.setx(x);
            br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("vous jouer dans la case : " + game.getx());
            x = game.getx();
            System.out.print("choisir entre 1 a 9 :");
            try{
                int y = Integer.parseInt(br.readLine());
                
                game.humanClick(x, y-1);
            }catch(NumberFormatException nfe){
                System.err.println("Invalid Format!");
            }
        }catch(NumberFormatException nfe){
            System.err.println("Invalid Format!");
        }
        
        
        while(boucle){
            if(game.DetermineWinner() == 1 || game.DetermineWinner() == -1)
            {
                boucle = false;
            }else{
                
                
                br = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("vous jouer dans la case : " + game.getx());
                x = game.getx();
                System.out.print("choisir entre 1 a 9 :");
                try{
                    int y = Integer.parseInt(br.readLine());
                    
                    game.humanClick(x, y-1);
                }catch(NumberFormatException nfe){
                    System.err.println("Invalid Format!");
                }
                
                
                
            }
        }
        
    }
    
}
