package main.java;

import java.util.InputMismatchException;
import java.util.Scanner;

import sources.hex.Plateau;



public class main {
	private static void JouerPartie(int taille,int modedejeu) {
		Plateau p = new Plateau(taille,modedejeu);
		int joueur=0;
		String s = "" ;
		while(p.FIN()==false) {
			Integer regleinvPion = null;
			if(p.getCoups()==1 && (p.getMode() == 1 || p.getMode() == 2 ) ){
				do {
					try {
					System.out.println("Entrez 0 si vous voulez changer de pion.");
					System.out.println("Entrez 1 si vous ne voulez pas changer de pion.");
					Scanner sc1 =new Scanner(System.in);
					regleinvPion = sc1.nextInt();
					}catch(InputMismatchException e) {
						
					}
					
				}while(regleinvPion<0 || regleinvPion>1);
				if(regleinvPion==0)p.ChangerPion();
			}
			
			
			joueur=p.getJoueur();
			System.out.println("C est au joueur " + joueur + " de jouer.");
			if(p.JoueurActuel()) {
				do {
					Scanner sc =new Scanner(System.in);
					s= sc.nextLine();
					if(p.estPossible(s)==false || p.estVide(s)==false)
						System.out.println("Cette case est soit déja occupé soit ne fait pas partie du plateau, veuillez en choisir une autre");
				}while(p.estPossible(s)==false || p.estVide(s)==false);
					p.JouerTour(s);
				}
			else{ //sinon c'est au tour de l'ordinateur de jouer
				p.JouerOrdi();
				}
			
					System.out.println(p);	
			}
			System.out.println("La victoire revient au joueur "+p.getGagnant()+". Féliciatations!!!");
		
	}

	public static void main(String[] args) {
		
		int taille=0;
		int ModeDeJeu =0;
		
		do {
			try {
			System.out.println("Veuillez choisir une taille de plateau entre 4 et 26: ");
			Scanner sc1 =new Scanner(System.in);
			taille = sc1.nextInt();
			}catch(InputMismatchException e) {
				
			}
			
		}while(taille<4 || taille>26);
		do {
			try {
			System.out.println("Voici la liste des modes de jeu, sélectionnez le chiffre correspondant au mode de jeu auxquel vous voulez jouer !");
			System.out.println("1 : Humain 1 contre Humain 2");
			System.out.println("2 : Ordinateur 1 contre Humain 1");
			System.out.println("3 : Ordinateur 1 contre Ordinateur 2");
			
			Scanner sc2 =new Scanner(System.in);
			ModeDeJeu = sc2.nextInt();
			}catch(InputMismatchException e) {
				
			}
			
		}while(ModeDeJeu < 1 || ModeDeJeu > 3);
		
		
		JouerPartie(taille,ModeDeJeu);

	}

}