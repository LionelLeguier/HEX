package sources.hex;

import java.util.concurrent.ConcurrentHashMap;


import sources.hex.Pion;

public class Plateau {
	private final static int TAILLE_MAX = 26;
	private final static int NB_JOUEURS = 2;
	private final static int COLONNE1 = 'A'; //Première colonne du plateau
	private final static int LIGNE1 = '1'; //Première ligne du plateau
	private ConcurrentHashMap<XY,Pion> coord;
	
	private Pion[][] t; // tableau qui correspond au plateau
	private Pion [] p; // tableau qui permet de connaitre le joueur actuel
	private int joueur = 0;
	private boolean estFinie;
	private Integer JoueurGagnant ;
	
	private boolean Joueur1;
	private boolean Joueur2;
	private int modedejeu = 0;
	private int nombreCoups = 0;
	
	
	public Plateau(int taille,int mode) {
		assert taille > 0 && taille <= TAILLE_MAX;
		t = new Pion [taille][taille];
		this.JoueurGagnant=null;
		this.estFinie=false;
		
		this.modedejeu=mode;
		this.coord=new ConcurrentHashMap<XY, Pion>();
		for (int lig = 0; lig < taille(); ++lig)
			for (int col = 0; col < taille(); ++col)
				t[col][lig] = Pion.Vide;
		p = new Pion[2];
		p[0] = Pion.Croix;
		p[1]=Pion.Rond;
		if(mode == 1) { //si mode = 1  alors la partie se joue entre un humain et un ordinateur
			this.Joueur1 = true;
			this.Joueur2 = true;
		}
		else if(mode == 2) { //si mode = 2  alors c'est une partie entre un humain et l'ordinateur
			this.Joueur1 = false;
			this.Joueur2 = true;
		}
		else { //sinon c'est un contre un entre deux ordinateurs
			this.Joueur1 = false;
			this.Joueur2 = false;
		
		}
		
		}
	

	
	
	public Plateau(int taille, String pos) {
		assert taille > 0 && taille <= TAILLE_MAX;
		t = new Pion [taille][taille];
		
		String[] lignes = decouper(taille, pos);
		
		for (int ligne = 0; ligne < taille(); ++ligne)
			for (int colonne = 0; colonne < taille(); ++colonne)
				t[colonne][ligne] = 
				  Pion.get(lignes[ligne].charAt(colonne));
		
		if (getNb(Pion.Croix) != getNb(Pion.Rond) &&
			getNb(Pion.Croix) != (getNb(Pion.Rond)+1) &&
					getNb(Pion.Croix) != (getNb(Pion.Rond)-1))
			throw new IllegalArgumentException(
					"position non valide");
	}
	
	
	

	public int getNb(Pion pion) {
		int nb = 0;
		for (Pion [] ligne : t)
			for (Pion p : ligne)
				if (p == pion)
					++nb;
		return nb;
	}

	public int taille() {
		return t.length;
	}
	
	
	private String espaces(int n) {
		String s = "";
		for(int i = 0; i < n; ++i)
			s+= " ";
		return s;
	}
	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < taille(); ++i)
			s+=" "+(char)( 'A' + i);
		s+='\n';
		for (int ligne = 0; ligne < taille(); ++ligne) {
			s+= ligne+1 + espaces (ligne);
			for (int colonne = 0; colonne < taille(); ++colonne)
				s+= " "+ t[colonne][ligne];
			s+='\n';
		}
		return s;
	}

	public static String[] decouper(int taille,
			String pos) {
		assert pos.length() == taille * taille;
		String[] lignes = new String[taille];
		for (int i = 0; i <taille; ++i)
			lignes[i] = pos.substring(i*taille,
					(i+1)*taille);
		return lignes;
		
	}
	
	private int getColonne(String coordonnee) {
			
			return coordonnee.toUpperCase().charAt(0) - COLONNE1; // ex 'D' -'C' == 1
		}

	private int getLigne(String coordonnee) {
		String s= coordonnee.substring(1);
		int lignecourante=Integer.parseInt(s) +'0';
		return  lignecourante - LIGNE1; // ex '3' - '2' == 1
	}
	
	public boolean estPossible(String coordonnee) {// Permet de savoir si le coup est possible
		if ( coordonnee.length() !=2 && coordonnee.length() !=3)
			return false;
		try {
            Double num = Double.parseDouble(coordonnee.substring(1));//Verifie si la coordonne est un int
        } catch (NumberFormatException e) {
            return false;
        }
		int ligne = getLigne(coordonnee);
		int colonne = getColonne (coordonnee);
		if (ligne <0 || ligne >= taille())// Si la ligne n'est pas dans le tableau on retourne faux
			return false;
		if (colonne <0 || colonne >= taille())// Si la colonne n'est pas dans le tableau on retourne faux
			return false;
		
		
		return true;
	}
	public Pion getCase(String coordonne) {
		assert estPossible(coordonne);// Verifie si le coup est possible
		int colonne = getColonne (coordonne);
		int ligne = getLigne(coordonne);
		return t[colonne][ligne];
	}
	public boolean estVide(String coordonnee) { // Permet de savoir si la coordonne correspondante est vide
		if(this.getCase(coordonnee)!=Pion.Vide)
			return false;
		return true;
	}
	
	
	public boolean gagner() { // Permet de savoir si la partie est gagnée 
		for(int x=0;x<this.taille();x++) {
			XY DernierCroix =new XY(x,this.taille()-1);
			if(this.coord.containsKey(DernierCroix) && this.coord.get(DernierCroix)==Pion.Croix) {
				return true;
			}
			XY DernierRond =new XY(this.taille()-1,x);
			if(this.coord.containsKey(DernierRond) && this.coord.get(DernierRond)==Pion.Rond) {
				return true;
			}
		}
		return false;
	}
	public void estFinie() { // permettant de savoir si à chaque coup joué par un joueur, la partie est finie ou non
		Pion pion = p[joueur];
		this.coord.clear();
			if(pion==Pion.Croix) {
					this.ChercherCroix(pion);
				}
				if(pion==Pion.Rond) {
					this.ChercherRond(pion);
				}
		this.chercher();
		if(this.gagner()==true ) {
			this.estFinie=true;
			this.JoueurGagnant=this.joueur+1;
			}
	}
	
	public static class XY {
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			XY other = (XY) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}
		private int x;
		private int y;
		public XY(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	public void ChercherCroix(Pion p){ // Permet de localiser dans la hashmap où se situe les croix
		for(int i=0;i<this.taille();i++) {
			if(this.t[i][0]==p) {
				XY a = new XY(i,0);
				if(!this.coord.containsKey(a)) {
					this.coord.put(a, p);
				}
			}
		}
	}
	
	public void ChercherRond(Pion p){ // Permet de localiser dans la hashmap où se situe les ronds
		for(int i=0;i<this.taille();i++) {
			if(this.t[0][i]==p) {
				XY a = new XY(0,i);
				if(!this.coord.containsKey(a)) {
					this.coord.put(a, p);
					}
				}
			}
	}
	
	public void chercher() { // permet de trouver si l'un des joueurs à gagner grâce aux coups qu'il a réalisé
		ConcurrentHashMap<XY,Pion> tmp=new ConcurrentHashMap<XY, Pion>();
		this.coord.forEach( (key, value) ->{ // Création d'un chemin temporaire
			tmp.put(key, value);
		});
			
			while(!tmp.isEmpty()) {
				tmp.forEach( (key, value) ->{// Recherche dans chaque case autour du pion courant
				for(int i=key.x-1;i<=key.x+1;i++) {
					if(i>=0 && i<=this.taille()-1) {
						if(i==key.x-1) {
							for(int j=key.y;j<=key.y+1;j++) {
								if(j>=0 && j<=this.taille()-1) {
									XY a = new XY(i,j);
									if(this.t[i][j]==value && !this.coord.containsKey(a)) {// Si on trouve le même pion dans une case voisine alors on l'ajoute au chemin temporaire
										tmp.put(a, value);
									}
								}
							}
							
						}
						if(i==key.x) {
							for(int y=key.y-1;y<=key.y+1;y++) {
								if(y>=0 && y<=this.taille()-1 && y!=key.y) {
									XY a = new XY(i,y);
									if(this.t[i][y]==value && !this.coord.containsKey(a)) {
										tmp.put(a, value);
									}
								}
							}
							
						}
						if(i==key.x+1) {
							for(int y=key.y-1;y<=key.y;y++) {
								if(y>=0 && y<=this.taille()-1) {
									XY a = new XY(i,y);
									if(this.t[i][y]==value && !this.coord.containsKey(a)) {
										tmp.put(a, value);
									}
								}
							}
							
						}
					}
						
				}
				this.coord.put(key, value);
				tmp.remove(key);
			});
			}
	}
	private void TourSuivant() {// Permet de passer au tour suivant
		joueur = (joueur +1) % NB_JOUEURS;
	}
	public void JouerTour(String coord) { // Permet de jouer un tour
		Pion pion = p[joueur];
		int colonne = getColonne (coord);
		int ligne = getLigne(coord);
		t[colonne][ligne] = pion;
		this.estFinie();
		TourSuivant();
		nombreCoups++;
		
	}
	public Pion getPionJoueur1() {
		return p[1];// Le joueur 1 joue avec les ronds
	}
	public Pion getPionJoueur2() {
		return p[0]; // Le joueur 2 joue avec les croix
	}
	public boolean getJoueur1() {
		return this.Joueur1;
	}
	public boolean getJoueur2() {
		return this.Joueur2;
	}
	public boolean FIN() {
		return this.estFinie;
		
	}
	
	public int getJoueur() {
		return joueur+1;
	}
	public boolean JoueurActuel() {
		if(joueur==0)return Joueur1;
		return Joueur2;
	}
	public int getGagnant() {
		return JoueurGagnant;
	}
	public boolean ChangerPion() {// Permet d'inveser les pions des joueurs si l'option est choisie
		if(this.getJoueur() == 2 && nombreCoups == 1) {
			p[1] = Pion.Croix;
			p[0] = Pion.Rond;
			TourSuivant();
			return true;
		}
		return false;
	}
	public int getCoups() {
		return nombreCoups;
	}
	public int getMode() {
		return modedejeu;
	}
	public void JouerOrdi() { // Permettant à l'ordianteur de jouer un coup aléatoire dans le plateau
		Pion Pion = p[joueur];
		int colonne,ligne;
		do {
			colonne = (int) (0 + (Math.random() * (this.t.length - 0)));
			ligne = (int) (0 + (Math.random() * (this.t.length - 0)));
		}while(t[colonne][ligne] != Pion.Vide );
		t[colonne][ligne] = Pion;
		this.estFinie();
		TourSuivant();
		nombreCoups++;
	}
}