/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactocalogo;

/**
 *
 * @author Royal
 */
public class Game {
    
    public Game(){
        initiateGameBoard();
    }
    int gGameBoard[] = new int[90];
int player = 0;

int botx;

int moveCount = 0;
int gameOver = 0;
int DEPTH_LIMIT = 2;
int VERY_LARGE = 2000000;
int SEUIL = VERY_LARGE/2;

int idx;
int[] COINS = new int[89];

int[] CORNER_INDEXES = {
 	 0,  2,  6,  8,
	10, 12, 16, 18,
	20, 22, 26, 28,
	30, 32, 36, 38,
	40, 42, 46, 48,
	50, 52, 56, 58,
	60, 62, 66, 68,
	70, 72, 77, 78,
	80, 82, 88, 88
};


int[] MILIEU_INDEX = {
	4, 14, 24, 34, 44, 54, 64, 74, 84
};

int MILIEU[] = new int[89];

int[][] GAGNER_SOLUS = {
	{0, 1, 2},
	{3, 4, 5},
	{6, 7, 8},
	{0, 3, 6},
	{1, 4, 7},
	{2, 5, 8},
	{0, 4, 8},
	{2, 4, 6}
};

int[][][] GAGNER_SOLUS_BY_SLOT = {
	{GAGNER_SOLUS[0], GAGNER_SOLUS[3], GAGNER_SOLUS[6]},
	{GAGNER_SOLUS[0], GAGNER_SOLUS[4]},
	{GAGNER_SOLUS[0], GAGNER_SOLUS[5], GAGNER_SOLUS[7]},
	{GAGNER_SOLUS[1], GAGNER_SOLUS[3]},
	{GAGNER_SOLUS[1], GAGNER_SOLUS[4], GAGNER_SOLUS[6], GAGNER_SOLUS[7]},
	{GAGNER_SOLUS[1], GAGNER_SOLUS[5]},
	{GAGNER_SOLUS[2], GAGNER_SOLUS[3], GAGNER_SOLUS[7]},
	{GAGNER_SOLUS[2], GAGNER_SOLUS[4]},
	{GAGNER_SOLUS[2], GAGNER_SOLUS[5], GAGNER_SOLUS[6]}
};
        
int[][] COUP_GAGNANT_SEARCH = {
	{0, 1, 2},
	{3, 4, 5},
	{6, 7, 8},
	{0, 3, 6},
	{1, 4, 7},
	{2, 5, 8},
	{0, 4, 8},
	{2, 4, 6}
};

int[] utility(int[] board,int last_slot,int slot,int depth) {

	int wn = 0; 
	int score = 0;


	int offset = last_slot * 10;
        int idx = GAGNER_SOLUS_BY_SLOT[slot].length;
      
	for (int i = 0; i < idx ;i++) {
		int[] triad = GAGNER_SOLUS_BY_SLOT[slot][i];

		int triad_sum = board[triad[0] + offset]
			+ board[triad[1] + offset] + board[triad[2] + offset];

		switch (triad_sum) {
		case  3:
		case -3:
			score = board[triad[0]+offset] * VERY_LARGE - depth;
			wn = 1;
			break;
		case  2:
			score += 3000; 
			break;
		case -2:
			score -= 3000;
			break;
		case  1:
		case -1:
			if ((board[triad[0] + offset]
				& board[triad[1] + offset]
				& board[triad[2] + offset]) != 0) {
				score -=  triad_sum * 1000;  // blocks a win
			}
			break;
		case  0: 
			score += board[offset + slot];
			break;
		default:
			break;
		}
		if (wn == 1) break;
	}

	if (wn == 0) {
		int move = offset + slot;
		int bonus = 7*MILIEU[move] +  2*COINS[move];
		 score += board[move]*bonus;

	}
        
        int[] value = {score, wn};
	return value;
}

int alphabeta(
	int[] board,int last_slot,int player,int next_player,
	int alpha,int beta,int depth,
	int score_so_far,
	int last_move_won
) {

	if ((last_move_won ==1)|| depth >= DEPTH_LIMIT) {
		return score_so_far;
	}

	int offset = last_slot * 10;

	int score = score_so_far;

	for (int slot = 0; slot < 9; ++slot) {
		int move = offset + slot;
		if (board[move] == 0) {
			board[move] = player;
			int[] rets = utility(board, last_slot, slot, depth);
			int val = alphabeta(board, slot, next_player, player,
				alpha, beta, depth + 1, score_so_far + rets[0], rets[1]);
			board[move] = 0;
			switch (player) {
			case  1:
				if (val > alpha) alpha = val;
				break;
			case -1:
				if (val < beta) beta = val;
				break;
			}
			if (beta <= alpha) break;
		}
	}

	score = beta;
	if (player == 1) 
		score = alpha;

	return score;
}

int x_your_move(int[] board,int last_slot) {


	int my_moves[] = new int[10] ;
	int best_val = -VERY_LARGE - DEPTH_LIMIT;
        int imove = 0;
	int offset = last_slot * 10;

	for (int slot = 0; slot < 9; ++slot) {

		int move = offset + slot;

		if (board[move] == 0) {
			board[move] = 1;
			int[] rets = utility(board, last_slot, slot, 0);
			int val = alphabeta(board, slot, -1, 1,
				-(VERY_LARGE+DEPTH_LIMIT)-1, VERY_LARGE+DEPTH_LIMIT+1,
				1, rets[0], rets[1]
			);
			board[move] = 0;
			if (val > best_val) {
				best_val = val;
				my_moves[0] = move;
                                
			} else if (val == best_val) {
				my_moves[imove] = move;
                                imove++;
			}
		}
	}

	int rv = 0;

        int my_movelength = 0;
        
        for(int i = 0; i < my_moves.length ;i++){
            if(my_moves[i] != 0)
                my_movelength += 1;
        }
	if (my_movelength < 1)
		System.out.println("fail\n");

	else {
		rv = my_moves[0];
		if (my_movelength > 1) {
			int x = (int) Math.round(Math.random() * 1000);
			x = x % my_movelength;
			rv = my_moves[x];
		}

		if (best_val > SEUIL)
			System.out.println("<p><strong>computer wins</strong></p>");
		else if (best_val < -SEUIL)
			System.out.println("<p><strong>human wins.</strong></p>");
	}

	return rv;
}





void winnerNotice(String phrase) {
	gameOver = 1; 
         System.out.println(phrase);
	
}

 void initiateGameBoard() {
	for (int i = 0; i < 89; ++i)
		gGameBoard[i] = 0;
        
        int nb = CORNER_INDEXES[0];
        int i = 0;
        for (idx = 0; idx < 89; ++idx)
        {
            if(nb == idx)
            {
                COINS[idx] = 1;
                i++;
                 nb = CORNER_INDEXES[i];
            }
            else{
                COINS[idx] = 0;  
            }
           
            
        }
        
  
	
        nb = MILIEU_INDEX[0];
        i = 0;
        for (idx = 0; idx < 89; ++idx)
            {
            if(nb == idx)
            {
                MILIEU[idx] = 1;
                if(MILIEU_INDEX.length == (i + 1)){
                     nb = MILIEU_INDEX[i];
                }else{
                    i++;
                 nb = MILIEU_INDEX[i];
                }
                    
            }
            else{
                MILIEU[idx] = 0;  
            }
           
            
        }
	
        

}

Boolean noLegalMoves() {
	if (moveCount >= 81)
		return true;
	return false;
}

void gofirt(){

	 int r;
	 r = 1; //random 
	 computerPlay(r);
	 
}

public int getx()
{
    return botx;
}


public void setx(int x)
{
     botx = x;
}

void computerPlay(int pos){
	
	int subb = pos;
	int slot = pos % 10;
	
	player = 1;
        setx(slot);
	clicked((subb - slot)/10, slot, player);
	DetermineWinner();
}

String valuetab(int subboard,int square){
    
    int i = (subboard*10)+square;
    if(gGameBoard[i] == -1)
             return "O";
           else if(gGameBoard[i] == 0 && subboard != getx())
             return ".";
            else if(gGameBoard[i] == 0 && subboard == getx())
             return "_";
           else if(gGameBoard[i] == 1)
             return"x" ;
    return "";
}
void clicked(int subboard,int square,int player){
	++moveCount;
    
	gGameBoard[(subboard*10)+square] = player;
        int n = 0;
        for(int i = 0;i != 3;++i)
        {
            for(int i2 = 0;i2 != 3;++i2)
           {
             System.out.println(valuetab(i*3,i2*3) + valuetab(i*3,((i2*3)+1)) +valuetab(i*3,((i2*3)+2))
                     +" "+valuetab((i*3)+1,((i2*3)))+valuetab(((i*3)+1),((i2*3)+1))+valuetab(((i*3)+1),((i2*3)+2) )
             +" "+valuetab(((i*3)+2),((i2*3)))+valuetab(((i*3)+2),((i2*3)+1))+valuetab(((i*3)+2),((i2*3)+2)));
            
           }
          System.out.println("");
        }
         System.out.println("------------------------------------------------------------");
        
}

int DetermineWinner(){
	int who_won = 0;
        int idxx = 72;
        int value = -10;
	for (int i = 0;i < idxx;i++) {
		int[] triad = COUP_GAGNANT_SEARCH[i%8];
                if((i % 8) == 0){
                    value = value + 10;
                }
		int triadSum = gGameBoard[triad[0] +value ] + gGameBoard[triad[1] +value]
			+ gGameBoard[triad[2]+ value ];
		if (triadSum == 3 || triadSum == -3) {
			who_won = gGameBoard[triad[0]+value];
			break;
		}
	}
	if (who_won == -1) winnerNotice("human wins");
	else if (who_won == 1) winnerNotice("computer won");
	else if (noLegalMoves()) winnerNotice("egaliter");
	else who_won = 2;  
	return who_won;
	
}


void humanClick(int subboard,int slot) {
    
	if (gameOver == 0) {
		player = -1;
		clicked(subboard, slot, -1);
		if (DetermineWinner() == 2) {
			computerPlay(x_your_move(gGameBoard, slot));
		}
	}
}
}
